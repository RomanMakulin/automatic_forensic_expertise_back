package com.example.controller;

import com.example.mapper.DirectionMapper;
import com.example.mapper.FileMapper;
import com.example.mapper.LocationMapper;
import com.example.model.*;
import com.example.model.dto.FileDTO;
import com.example.model.dto.ProfileCreateDTO;
import com.example.repository.ProfileRepository;
import com.example.repository.StatusRepository;
import com.example.service.AppUserService;
import com.example.service.FileService;
import com.example.service.MinIOFileService;
import com.example.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);
    private final ProfileService profileService;

    private final LocationMapper locationMapper;

    private final DirectionMapper directionMapper;

    private final FileMapper fileMapper;

    private final AppUserService appUserService;

    private final MinIOFileService minIOFileService;

    private final FileService fileService;

    private final StatusRepository statusRepository;

    private final ProfileRepository profileRepository;

    public ProfileController(ProfileService profileService, LocationMapper locationMapper, DirectionMapper directionMapper, FileMapper fileMapper, AppUserService appUserService, MinIOFileService minIOFileService, FileService fileService, StatusRepository statusRepository, ProfileRepository profileRepository) {
        this.profileService = profileService;
        this.locationMapper = locationMapper;
        this.directionMapper = directionMapper;
        this.fileMapper = fileMapper;
        this.appUserService = appUserService;
        this.minIOFileService = minIOFileService;
        this.fileService = fileService;
        this.statusRepository = statusRepository;
        this.profileRepository = profileRepository;
    }

    @GetMapping
    public ResponseEntity<List<Profile>> getAll() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfile(@PathVariable UUID id) {
        return profileService.getProfileById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

    }

    /**
     * Создание профиля
     */
    @Transactional
    @PostMapping("/create")
    public ResponseEntity<?> saveAll(
            @RequestPart("profile") ProfileCreateDTO profileCreateDTO,
            @RequestPart("photo") MultipartFile photo,
            @RequestPart("files") List<MultipartFile> files
    ) {
        try {
            // Проверка входных данных
            if (photo == null || photo.isEmpty()) {
                return ResponseEntity.badRequest().body("Фото обязательно для загрузки.");
            }
            if (files == null || files.isEmpty()) {
                return ResponseEntity.badRequest().body("Сканы документов обязательны.");
            }

            // Получение аутентифицированного пользователя
            AppUser appUser = getAuthenticatedUser();

            // Создаём профиль
            Profile profile = new Profile();
            profile.setId(UUID.randomUUID()); // Генерируем ID
            profile.setPhone(profileCreateDTO.getPhone());
            profile.setAppUser(appUser);
            profile.setStatus(new Status()); // Устанавливаем дефолтный статус
            profile.setLocation(locationMapper.toEntity(profileCreateDTO.getLocationDTO()));
            Set<Direction> directions = directionMapper.toEntity(profileCreateDTO.getDirectionDTOList());
            directions.forEach(direction -> direction.setProfile(profile));
            profile.setDirections(directions);
            profile.setFiles(new HashSet<>());

            // Сохраняем фото и файлы
            List<FileDTO> fileDTOS = minIOFileService.savePhotoTemplateFiles(profile.getId(), photo, files);

            for (FileDTO fileDTO : fileDTOS) {
                File fileEntity = new File();
                fileEntity.setId(fileDTO.getId());
                fileEntity.setPath(fileDTO.getPath());
                fileEntity.setUploadDate(fileDTO.getCratedAt());

                fileEntity.setProfile(profile);
                profile.getFiles().add(fileEntity);
            }

            profileRepository.save(profile);

            return ResponseEntity.ok("Профиль успешно создан.");
        } catch (ObjectOptimisticLockingFailureException e) {

            log.error("Конфликт при обновлении данных: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Конфликт данных. Пожалуйста, попробуйте снова.");
        } catch (Exception e) {
            log.error("Ошибка при создании профиля: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Произошла ошибка. Попробуйте позже.");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Profile> updateProfile(@RequestBody Profile profile) {
        return ResponseEntity.ok(profileService.update(profile));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable UUID id) {
        profileService.delete(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * Возвращает список всех профилей, которые не прошли проверку администратором
     *
     * @return список профилей
     */
    @GetMapping("/get-unverified-profiles")
    public ResponseEntity<List<Profile>> getUnverifiedProfiles() {
        // TODO logic
        List<Profile> profiles = profileService.getUnverifiedProfiles();
        return ResponseEntity.ok(profiles);
    }

    /**
     * Подтверждает профиль пользователя
     *
     * @param profileId идентификатор профиля
     */
    @GetMapping("/validate-profile/{profileId}")
    public ResponseEntity<Void> validateProfile(@PathVariable("profileId") String profileId) {
        Optional<Profile> optionalProfile = profileService.getProfileById(UUID.fromString(profileId));

        if (optionalProfile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Profile profile = optionalProfile.get();
        profile.getStatus().setVerificationResult(Status.VerificationResult.APPROVED);
        profileService.save(profile);
        return ResponseEntity.ok().build();
    }

    /**
     * Отменяет подтверждение профиля пользователя
     *
     * @param profileCancel dto с неподходящими данными
     */
    @PostMapping("/cancel-validation")
    public ResponseEntity<Void> cancelValidationProfile(@RequestBody ProfileCancel profileCancel) {
        Optional<Profile> optionalProfile = profileService.getProfileById(UUID.fromString(profileCancel.getProfileId()));

        if (optionalProfile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Profile profile = optionalProfile.get();

        if (!profileCancel.getDirections().isEmpty()) {
            for (String directionId : profileCancel.getDirections()) {
                profile.getDirections().remove(UUID.fromString(directionId));
            }
        }

        if (!profileCancel.getFiles().isEmpty()) {
            for (String fileId : profileCancel.getFiles()) {
                profile.getFiles().remove(UUID.fromString(fileId));
            }
        }

        profile.getStatus().setVerificationResult(Status.VerificationResult.NEED_REMAKE);
        profileService.save(profile);
        return ResponseEntity.ok().build();
    }

    public AppUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Jwt jwt = (Jwt) authentication.getPrincipal();

        String email = jwt.getClaim("email"); // Email пользователя
        return appUserService.getAppUserByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found"));
    }


}
