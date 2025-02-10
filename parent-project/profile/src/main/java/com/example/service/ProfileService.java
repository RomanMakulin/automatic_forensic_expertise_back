package com.example.service;

import com.example.integration.mail.MailIntegration;
import com.example.integration.mail.dto.MailRequest;
import com.example.model.AppUser;
import com.example.model.Profile;
import com.example.model.Status;
import com.example.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileService {

    private static final Logger log = LoggerFactory.getLogger(ProfileService.class);
    private final ProfileRepository profileRepository;
    private final MailService mailService;




    public ProfileService(ProfileRepository profileRepository,
                          MailService mailService) {
        this.profileRepository = profileRepository;
        this.mailService = mailService;
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Optional<Profile> getProfileById(UUID id) {
        return profileRepository.findById(id);
    }

    public Profile save(Profile profile) {
        try {
            Profile profileSaved = profileRepository.save(profile);
            mailService.sendMailToAdmins(profileSaved);
            return profileSaved;
        } catch (Exception e) {
            log.error("Ошибка при сохранении профиля: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка сохранения профиля", e);
        }
    }

    public Profile saveAndFlush(Profile profile) {
        return profileRepository.saveAndFlush(profile);
    }

    public Profile update(Profile profile) {
        Profile dataBaseProfile = profileRepository.findById(profile.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Profile not found with id: " + profile.getId()));

        // Если данные редактирует не верифицированный пользователь - необходимо ему сменить статус и отправить рассылку админам
        if (dataBaseProfile.getStatus().getVerificationResult() == Status.VerificationResult.NEED_REMAKE) {
            dataBaseProfile.getStatus().setVerificationResult(Status.VerificationResult.NEED_VERIFY);
            mailService.sendMailToAdmins(profile);
        }
        return profileRepository.save(profile);
    }

    public void delete(UUID id) {
        profileRepository.deleteById(id);
    }

    public List<Profile> getUnverifiedProfiles() {
        return profileRepository.findAllByStatus_VerificationResult(Status.VerificationResult.NEED_VERIFY);
    }


}
