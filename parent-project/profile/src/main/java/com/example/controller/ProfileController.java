package com.example.controller;

import com.example.mapper.LocationMapper;
import com.example.mapper.ProfileMapper;
import com.example.mapper.StatusMapper;
import com.example.model.*;

import com.example.model.dto.LocationDTO;
import com.example.model.dto.ProfileCreateDTO;
import com.example.model.dto.ProfileDTO;
import com.example.model.dto.StatusDTO;
import com.example.service.*;
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

    private final LocationMapper locationMapper;

    private final StatusMapper statusMapper;

    public ProfileController(ProfileService profileService, ProfileMapper profileMapper, LocationMapper locationMapper, StatusMapper statusMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
        this.locationMapper = locationMapper;
        this.statusMapper = statusMapper;
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

        System.out.println(profile.getStatus());
        System.out.println(profile.getLocation());

        StatusDTO statusDTO = statusMapper.toDto(profile.getStatus());
        LocationDTO locationDTO = locationMapper.toDto(profile.getLocation());

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

        //todo долелать сохранения пасспорта и диплома
        profileService.createProfile(profileCreateDTO, photo, passport, diplom, files);

        return ResponseEntity.ok().build();
    }

    //todo @RequestPart("profile") ProfileCreateDTO profileCreateDTO,
    //                                     @RequestPart("photo") MultipartFile photo,
    //                                     @RequestPart("passport") MultipartFile passport,
    //
    //                                     @RequestPart("files") List<MultipartFile> files

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
