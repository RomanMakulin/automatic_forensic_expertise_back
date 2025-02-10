package com.example.controller;

import com.example.mapper.ProfileMapper;
import com.example.model.*;
import com.example.model.dto.FileDTO;
import com.example.model.dto.ProfileCreateDTO;
import com.example.model.dto.ProfileDTO;
import com.example.repository.ProfileRepository;
import com.example.service.*;
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


    private final ProfileMapper profileMapper;

    public ProfileController(ProfileService profileService, ProfileMapper profileMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
    }

    @GetMapping
    public ResponseEntity<List<ProfileDTO>> getAll() {
        List<Profile> profiles = profileService.getAllProfiles();
        List<ProfileDTO> profileDTOS = profileMapper.toDto(profiles);
        return ResponseEntity.ok(profileDTOS);
    }

    @GetMapping("/")
    public ResponseEntity<ProfileDTO> getProfile(@RequestParam UUID id) {
        Optional<Profile> optionalProfile = profileService.getProfileById(id);

        if (optionalProfile.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Profile profile = optionalProfile.get();
        ProfileDTO profileDTO = profileMapper.toDto(profile);
        return ResponseEntity.ok(profileDTO);
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

        profileService.createProfile(profileCreateDTO, photo, files);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<ProfileDTO> updateProfile(@RequestBody Profile profile) {
        Profile updatedProfile = profileService.update(profile);
        ProfileDTO profileDTO = profileMapper.toDto(updatedProfile);
        return ResponseEntity.ok(profileDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable UUID id) {
        profileService.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Возвращает список всех профилей, которые не прошли проверку администратором
     *
     * @return список профилей
     */
    @GetMapping("/get-unverified-profiles")
    public ResponseEntity<List<ProfileDTO>> getUnverifiedProfiles() {
        // TODO logic
        List<ProfileDTO> profiles = profileService.getUnverifiedProfiles();
        return ResponseEntity.ok(profiles);
    }

    /**
     * Подтверждает профиль пользователя
     *
     * @param profileId идентификатор профиля
     */
    @GetMapping("/verify")
    public ResponseEntity<Void> validateProfile(@RequestParam("profileId") String profileId) {
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
