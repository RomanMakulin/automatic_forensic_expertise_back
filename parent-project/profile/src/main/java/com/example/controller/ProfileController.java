package com.example.controller;

import com.example.mapper.*;
import com.example.model.*;

import com.example.model.dto.*;
import com.example.service.*;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;


    private final ProfileMapper profileMapper;
    private final DirectionMapper directionMapper;
    private final FileMapper fileMapper;

    public ProfileController(ProfileService profileService, ProfileMapper profileMapper, DirectionMapper directionMapper, FileMapper fileMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
        this.directionMapper = directionMapper;
        this.fileMapper = fileMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProfileDTO>> getAll() {
        List<Profile> profiles = profileService.getAllProfiles();
        List<ProfileDTO> profileDTOS = profileMapper.toDto(profiles);
        return ResponseEntity.ok(profileDTOS);
    }

    @GetMapping("/get-by-profile-id")
    public ResponseEntity<ProfileDTO> getProfileByProfileId(@RequestParam UUID id) {
        Optional<Profile> optionalProfile = profileService.getProfileById(id);

        if (optionalProfile.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Profile profile = optionalProfile.get();
        ProfileDTO profileDTO = profileMapper.toDto(profile);
        return ResponseEntity.ok(profileDTO);
    }

    @GetMapping("/get-by-user-id")
    public ResponseEntity<ProfileDTO> getProfileByUserId(@RequestParam UUID id) {
        Optional<Profile> optionalProfile = profileService.getProfileByUserId(id);

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
                                     @RequestPart("passport") MultipartFile passport,
                                     @RequestPart("diplom") MultipartFile diplom,
                                     @RequestPart("files") List<MultipartFile> files
    ) {

        if (photo == null || photo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("А где фотка, умник? Иди фоткой рожу свою, потом еще раз попробуешь");
        }

        if (files == null || files.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("А где сканы документов? Иди делай, потом вернешься");
        }

        profileService.createProfile(profileCreateDTO, photo, passport, diplom, files);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateProfile(@RequestPart("profile") ProfileCreateDTO profileCreateDTO,
                                                    @RequestPart("photo") MultipartFile photo,
                                                    @RequestPart("passport") MultipartFile passport,
                                                    @RequestPart("diplom") MultipartFile diplom,
                                                    @RequestPart("files") List<MultipartFile> files) {
        profileService.update(profileCreateDTO, photo, passport, diplom, files);
        return ResponseEntity.ok().build();
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
    @GetMapping("/unverified")
    public ResponseEntity<List<Profile>> getUnverifiedProfiles() {
        List<Profile> profiles = profileService.getUnverifiedProfilesWithOutDTO();
        return ResponseEntity.ok(profiles);
    }

    /**
     * Подтверждает профиль пользователя
     *
     * @param profileId идентификатор профиля
     */
    @GetMapping("/verify")
    public ResponseEntity<Void> validateProfile(@RequestParam String profileId) {
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
        Optional<Profile> optionalProfile = profileService.getProfileById(profileCancel.getProfileId());

        if (optionalProfile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Profile profile = optionalProfile.get();

        if (!profileCancel.getDirections().isEmpty()) {
            List<String> directions = profileCancel.getDirections();
            for (String direction : directions) {
                profile.getDirections().removeIf(direction::equals);
            }
        }

        if (!profileCancel.getFiles().isEmpty()) {
            List<String> filesIds = profileCancel.getFiles();
            for (String fileId : filesIds) {
                profile.getFiles().removeIf(file -> file.getId().equals(fileId));
            }
        }

        profile.getStatus().setVerificationResult(Status.VerificationResult.NEED_REMAKE);
        profileService.save(profile);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update-plan")
    public ResponseEntity<Void> updatePlan(@RequestParam UUID profileId, @RequestParam UUID planId) {
        Optional<Profile> optionalProfile = profileService.getProfileById(profileId);

        if (optionalProfile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Profile profile = optionalProfile.get();
        profileService.updatePlan(profile, planId);

        return ResponseEntity.ok().build();
    }


}


