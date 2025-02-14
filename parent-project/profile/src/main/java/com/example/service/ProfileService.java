package com.example.service;

import com.example.mapper.DirectionMapper;
import com.example.mapper.FileMapper;
import com.example.mapper.LocationMapper;
import com.example.mapper.ProfileMapper;
import com.example.model.*;
import com.example.model.dto.FileDTO;
import com.example.model.dto.ProfileCreateDTO;
import com.example.model.dto.ProfileDTO;
import com.example.repository.PlanRepository;
import com.example.repository.ProfileRepository;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ProfileService {

    private final LocationMapper locationMapper;

    private final DirectionMapper directionMapper;

    private final FileMapper fileMapper;

    private final AppUserService appUserService;

    private final MinIOFileService minIOFileService;

    private final FileService fileService;

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;

    private final MailService mailService;

    private final PlanRepository planRepository;


    public ProfileService(LocationMapper locationMapper, DirectionMapper directionMapper, FileMapper fileMapper, AppUserService appUserService, MinIOFileService minIOFileService, FileService fileService, ProfileRepository profileRepository, ProfileMapper profileMapper, MailService mailService, PlanRepository planRepository) {
        this.locationMapper = locationMapper;
        this.directionMapper = directionMapper;
        this.fileMapper = fileMapper;
        this.appUserService = appUserService;
        this.minIOFileService = minIOFileService;
        this.fileService = fileService;
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
        this.mailService = mailService;
        this.planRepository = planRepository;
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Optional<Profile> getProfileById(UUID id) {
        return profileRepository.findById(id);
    }


    public Optional<Profile> getProfileByUserId(UUID id) {
        return profileRepository.findByAppUser_Id(id);
    }

    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }

    public Profile saveAndFlush(Profile profile) {
        return profileRepository.saveAndFlush(profile);
    }

    public void update(ProfileCreateDTO profileCreateDTO,
                             MultipartFile photo,
                             MultipartFile passport,
                             MultipartFile diplom,
                             List<MultipartFile> files
    ) {
        AppUser appUser = getAuthenticatedUser();

        Profile profile = profileRepository.findByAppUser_Id(appUser.getId())
                .orElseThrow(() ->
                new EntityNotFoundException("Profile not found"));

        Set<Direction> directions = directionMapper.toEntity(profileCreateDTO.getDirectionDTOList());
        Location location = locationMapper.toEntity(profileCreateDTO.getLocationDTO());

        for (Direction d : directions) {
            d.setProfile(profile);
        }

        profile.getDirections().addAll(directions);
        profile.setLocation(location);
        profile.setPhone(profileCreateDTO.getPhone());

//        if (photo != null && !photo.isEmpty()) {
//            minIOFileService.savePhoto(profile.getId(), photo);
//        }
//
//        if (passport != null && !passport.isEmpty()) {
//            minIOFileService.savePassport(profile.getId(), passport);
//        }
//
//        if (!files.isEmpty()) {
//            List<FileDTO> filesDtos = minIOFileService.saveFiles(profile.getId(), files);
//            List<File> fileList = fileMapper.toEntity(filesDtos);
//            profile.getFiles().addAll(fileList);
//        }

        profile.getStatus().setVerificationResult(Status.VerificationResult.NEED_VERIFY);

        minIOFileService.saveAllFilesForProfile(profile.getId(), photo, passport, diplom, files);

        profileRepository.save(profile);
    }

    public void delete(UUID id) {
        profileRepository.deleteById(id);
    }

    public List<ProfileDTO> getUnverifiedProfiles() {
        List<Profile> profiles = profileRepository.findAllByStatus_VerificationResult(Status.VerificationResult.NEED_VERIFY);

        List<ProfileDTO> profileDTOS = profileMapper.toDto(profiles);

        for (ProfileDTO dto : profileDTOS) {
            fillFilesPath(dto, UUID.fromString(dto.getId()));
        }

        return profileDTOS;
    }

    //для админки
    public List<Profile> getUnverifiedProfilesWithOutDTO() {
        List<Profile> profiles = profileRepository.findAllByStatus_VerificationResult(Status.VerificationResult.NEED_VERIFY);
        return profiles;
    }


    public void createProfile(ProfileCreateDTO profileCreateDTO,
                              MultipartFile photo,
                              MultipartFile passport,
                              MultipartFile diplom,
                              List<MultipartFile> files) {
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

        List<FileDTO> fileDTOS = minIOFileService.saveAllFilesForProfile(profile.getId(), photo, passport, diplom, files);

        for (FileDTO fileDTO : fileDTOS) {
            File file = fileMapper.toEntity(fileDTO);
            file.setProfile(profile);

            profile.getFiles().add(file);
        }

        save(profile);
        mailService.sendMailToAdminsForVerification(profile); // разослать администратором сообщение, что данного пользователя необходимо проверить
    }

    public AppUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Jwt jwt = (Jwt) authentication.getPrincipal();

        String email = jwt.getClaim("email"); // Email пользователя
        return appUserService.getAppUserByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found"));
    }

    private ProfileDTO fillFilesPath(ProfileDTO profileDTO, UUID id) {
        String photo = minIOFileService.getPhotoUrl(id);
        String passport = minIOFileService.getPassportUrl(id);
        String diplom = minIOFileService.getDiplomUrl(id);

        profileDTO.setPhoto(photo);
        profileDTO.setPassport(passport);
        profileDTO.setDiplom(diplom);
        return profileDTO;
    }

    public void updatePlan(Profile profile, UUID planId) {
        Optional<Plan> optionalPlan = planRepository.findById(planId);
        Plan plan = optionalPlan.orElseThrow(() -> new EntityNotFoundException("Plan not found"));
        profile.setPlan(plan);
        profileRepository.save(profile);
    }
}
