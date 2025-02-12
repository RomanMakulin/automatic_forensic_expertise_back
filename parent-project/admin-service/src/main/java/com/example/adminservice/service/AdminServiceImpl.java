package com.example.adminservice.service;

import com.example.adminservice.api.dto.profile.ProfileDto;
import com.example.adminservice.api.dto.profile.original.OriginalProfileDto;
import com.example.adminservice.api.dto.profileCancel.ProfileCancel;
import com.example.adminservice.integration.profile.ProfileIntegration;
import com.example.adminservice.mapper.ProfileMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реализация сервиса работы с админкой
 */
@Service
public class AdminServiceImpl implements AdminService {

    private final ProfileIntegration profileIntegration;
    private final ProfileMapper profileMapper;

    public AdminServiceImpl(ProfileIntegration profileIntegration, ProfileMapper profileMapper) {
        this.profileIntegration = profileIntegration;
        this.profileMapper = profileMapper;
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
    public void cancelValidationProfile(ProfileCancel profileDto) {
        profileIntegration.requestForCancelVerifyProfile(profileDto);
    }
}
