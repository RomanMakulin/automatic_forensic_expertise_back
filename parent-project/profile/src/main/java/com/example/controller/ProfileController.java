package com.example.controller;

import com.example.mapper.DirectionMapper;
import com.example.mapper.FileMapper;
import com.example.mapper.LocationMapper;
import com.example.model.*;
import com.example.model.dto.FileDTO;
import com.example.model.dto.ProfileCreateDTO;
import com.example.repository.ProfileRepository;
import com.example.service.AppUserService;
import com.example.service.FileService;
import com.example.service.MinIOFileService;
import com.example.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final ProfileService profileService;

    private final LocationMapper locationMapper;

    private final DirectionMapper directionMapper;

    private final FileMapper fileMapper;

    private final AppUserService appUserService;

    private final MinIOFileService minIOFileService;

    private final FileService fileService;

    private final ProfileRepository profileRepository;

    public ProfileController(ProfileService profileService, LocationMapper locationMapper, DirectionMapper directionMapper, FileMapper fileMapper, AppUserService appUserService, MinIOFileService minIOFileService, FileService fileService, ProfileRepository profileRepository) {
        this.profileService = profileService;
        this.locationMapper = locationMapper;
        this.directionMapper = directionMapper;
        this.fileMapper = fileMapper;
        this.appUserService = appUserService;
        this.minIOFileService = minIOFileService;
        this.fileService = fileService;
        this.profileRepository = profileRepository;
    }

    @GetMapping
    public ResponseEntity<List<Profile>> getAll() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    @GetMapping("/")
    public ResponseEntity<Profile> getProfile(@RequestParam UUID id) {
        System.out.println(id);
        return profileService.getProfileById(id).map(ResponseEntity::ok).orElse(ResponseEntity.ok().build());

    }

    @Transactional
    @PostMapping("/create")
    public ResponseEntity<?> saveAll(@RequestPart("profile") ProfileCreateDTO profileCreateDTO,
                                     @RequestPart("photo") MultipartFile photo,
//                                        @RequestPart("template") MultipartFile template, //todo пока не понятно что с шаблоном
                                     @RequestPart("files") List<MultipartFile> files
    ) {

        if (photo == null || photo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("А где фотка, умник? Иди фоткой рожу свою, потом еще раз попробуешь");
        }

//        if (template == null || template.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)                          //todo пока не понятно что с шаблоном
//                    .body("А где, блять, шаблон? Это шутка какая-то? Смешно, да?");
//        }

        if (files == null || files.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("А где сканы документов? Иди делай, потом вернешься");
        }

        AppUser appUser = getAuthenticatedUser();

        Set<Direction> directions = directionMapper.toEntity(profileCreateDTO.getDirectionDTOList());
        Location location = locationMapper.toEntity(profileCreateDTO.getLocationDTO());

        Profile profile = new Profile();
        profile.setId(UUID.randomUUID()); // Генерируем ID
        profile.setPhone(profileCreateDTO.getPhone());
        profile.setAppUser(appUser);  // подставляем юзера
        profile.setStatus(new Status());  // создаем дефолтный статус
        profile.setDirections(directions);
        profile.setLocation(location);

        for (Direction direction : directions) {
            direction.setProfile(profile);
        }

        List<FileDTO> fileDTOS = minIOFileService.savePhotoTemplateFiles(profile.getId(), photo, files);

        for (FileDTO fileDTO : fileDTOS) {
            File file = fileMapper.toEntity(fileDTO);
            file.setProfile(profile);

            profile.getFiles().add(file);
        }

        profileService.save(profile);

        return ResponseEntity.ok().build();
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
