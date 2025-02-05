package com.example.controller;

import com.example.mapper.DirectionMapper;
import com.example.mapper.LocationMapper;
import com.example.model.*;
import com.example.model.dto.ProfileCreateDTO;
import com.example.model.dto.ProfileDTO;
import com.example.model.dto.ProfileFullDTO;
import com.example.repository.AppUserRepository;
import com.example.service.AppUserService;
import com.example.service.LocationService;
import com.example.service.ProfileService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    private final LocationMapper locationMapper;

    private final DirectionMapper directionMapper;

    private final AppUserService appUserService;

    public ProfileController(ProfileService profileService, LocationMapper locationMapper, DirectionMapper directionMapper, AppUserService appUserService) {
        this.profileService = profileService;
        this.locationMapper = locationMapper;
        this.directionMapper = directionMapper;
        this.appUserService = appUserService;

    }

    @GetMapping
    public ResponseEntity<List<Profile>> getAll() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfile(@PathVariable UUID id) {
        return profileService.getProfileById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

    }

    @PostMapping("/save")
    public ResponseEntity<Profile> save(@RequestBody Profile profile) {
        AppUser appUser = new AppUser();
    //    Location location = locationService.save(new Location());
        Status status = new Status();
        Role role = new Role();
        appUser.setRole(role);
        System.out.println(profile);
     //   profile.setLocation(location);
        profile.setStatus(status);
        profile.setAppUser(appUser);
        return ResponseEntity.ok(profileService.save(profile));
    }

    /**
     * Создание профиля
     */
    @PostMapping("/create")
    public ResponseEntity<?> saveAll(@RequestPart("profile") ProfileCreateDTO profileCreateDTO,
                                           @RequestPart("photo") MultipartFile photo
//            ,
//                                           @RequestPart("files") List<MultipartFile> files
    ) throws ChangeSetPersister.NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //todo где брать пользователя
//
//        if (authentication == null && !(authentication.getPrincipal() instanceof Jwt)) {
//            Jwt jwt = (Jwt) authentication.getPrincipal();
//
//            UUID userId = (UUID) jwt.getClaims().get("id");
//
//
//        }
//        if (authentication == null || !(authentication.getPrincipal() instanceof AppUser)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Увы... Нет авторизованного пользователя");
//        }

        AppUser appUser = appUserService.getAppUserById(UUID.fromString("ddbb5a76-cf35-4a75-919b-9e2a0bc1194d")).orElseThrow(() ->
                new ChangeSetPersister.NotFoundException());

        Set<Direction> directions = directionMapper.toEntity(profileCreateDTO.getDirectionDTOList());
        Location location = locationMapper.toEntity(profileCreateDTO.getLocationDTO());

        Profile profile = new Profile();
        profile.setPhone(profileCreateDTO.getPhone());
        profile.setAppUser(appUser);  // подставляем юзера
        profile.setStatus(new Status());  // создаем дефолтный статус
        profile.setDirections(directions);
        profile.setLocation(location);

        for (Direction direction : directions) {
            direction.setProfile(profile);
        }

        profile.setPhoto("загалушечка"); //todo дописать реализацию

        if (photo != null && !photo.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("А где фотка, умник? Иди фоткой рожу свою, потом еще раз попробуешь");
        }

        System.out.println(photo.getName());
        System.out.println(photo.getContentType());

//        if (files != null && !files.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("А где сканы документов? Иди делай, потом вернешься");
//        }

//        fileService.save(photo);
//        fileService.save(files);


        return ResponseEntity.ok(profileService.save(profile));
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


}
