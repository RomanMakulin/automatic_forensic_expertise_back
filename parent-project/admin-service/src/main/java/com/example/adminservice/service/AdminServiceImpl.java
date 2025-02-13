package com.example.adminservice.service;

import com.example.adminservice.api.dto.profile.ProfileDto;
import com.example.adminservice.api.dto.profile.original.OriginalProfileDto;
import com.example.adminservice.api.dto.profileCancel.ProfileCancelForProfile;
import com.example.adminservice.api.dto.profileCancel.ProfileCancelFromFront;
import com.example.adminservice.integration.minio.MinioIntegration;
import com.example.adminservice.integration.profile.ProfileIntegration;
import com.example.adminservice.mapper.ProfileMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Реализация сервиса работы с админкой
 */
@Service
public class AdminServiceImpl implements AdminService {

    private final ProfileIntegration profileIntegration;
    private final ProfileMapper profileMapper;
    private final MinioIntegration minioIntegration;

    public AdminServiceImpl(ProfileIntegration profileIntegration, ProfileMapper profileMapper, MinioIntegration minioIntegration) {
        this.profileIntegration = profileIntegration;
        this.profileMapper = profileMapper;
        this.minioIntegration = minioIntegration;
    }

    /**
     * Получение всех неподтвержденных профилей
     *
     * @return список объектов ProfileDto
     */
    @Override
    public List<ProfileDto> getUnverifiedProfiles() {
        List<OriginalProfileDto> originalProfileDtoList = profileIntegration.requestForUnverifiedProfiles();
        return profileMapper.toProfileDtoList(originalProfileDtoList);
    }

    /**
     * Получение всех профилей
     *
     * @return список объектов ProfileDto
     */
    @Override
    public List<ProfileDto> getAllProfiles() {
        List<OriginalProfileDto> originalProfileDtoList = profileIntegration.requestForAllProfiles();
        return profileMapper.toProfileDtoList(originalProfileDtoList);
    }

    /**
     * Подтверждение валидации профиля
     *
     * @param profileId идентификатор профиля
     */
    @Override
    public void verifyProfile(String profileId) {
        profileIntegration.requestForVerifyProfile(profileId);
    }

    /**
     * Отмена валидации профиля
     *
     * @param profileDto объект с некорректными данными профиля
     */
    @Override
    public void cancelValidationProfile(ProfileCancelFromFront profileDto) {
        if (profileDto == null) {
            throw new IllegalArgumentException("Profile DTO cannot be null");
        }

        String profileId = profileDto.getProfileId();
        ProfileCancelForProfile profileCancelForProfile = new ProfileCancelForProfile(profileId, profileDto.getFiles(), profileDto.getDirections());

        // Удаление диплома, если указано
        Optional.ofNullable(profileDto.getNeedDiplomDelete())
                .filter(Boolean::booleanValue)
                .ifPresent(need -> minioIntegration.deleteFile("delete-diplom", Map.of("profileId", profileId)));

        // Удаление паспорта, если указано
        Optional.ofNullable(profileDto.getNeedPassportDelete())
                .filter(Boolean::booleanValue)
                .ifPresent(need -> minioIntegration.deleteFile("delete-passport", Map.of("profileId", profileId)));

        // Удаление файлов, если список не пуст
        Optional.ofNullable(profileDto.getFiles())
                .filter(files -> !files.isEmpty())
                .ifPresent(files -> minioIntegration.deleteFiles("delete-files", files));

        // Запрос на отмену валидации профиля
        profileIntegration.requestForCancelVerifyProfile(profileCancelForProfile);
    }

}
