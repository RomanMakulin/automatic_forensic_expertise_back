package com.example.service;

import com.example.model.Profile;
import com.example.model.Status;
import com.example.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Optional<Profile> getProfileById(UUID id) {
        return profileRepository.findById(id);
    }

    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }

    public Profile saveAndFlush(Profile profile) {
        return profileRepository.saveAndFlush(profile);
    }

    public Profile update(Profile profile) {
        profileRepository.findById(profile.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Profile not found with id: " + profile.getId()));

        return profileRepository.save(profile);
    }

    public void delete(UUID id) {
        profileRepository.deleteById(id);
    }

    public List<Profile> getUnverifiedProfiles() {
        return profileRepository.findAllByStatus_VerificationResult(Status.VerificationResult.NEED_VERIFY);
    }



}
