package com.example.adminservice.service;

import com.example.adminservice.api.dto.profile.ProfileDto;
import com.example.adminservice.api.dto.profileCancel.ProfileCancel;

import java.util.List;

/**
 * Интерфейс сервиса администратора
 */
public interface ProfileManageService {

    /**
     * Получить профили всех пользователей
     *
     * @return список пользователей
     */
    List<ProfileDto> getAllProfiles();

    /**
     * Получить профили всех пользователей, которые не прошли проверку администратором
     *
     * @return список пользователей
     */
    List<ProfileDto> getNotVerifiedProfiles();

    /**
     * Подтверждает профиль пользователя
     *
     * @param profileId идентификатор профиля
     */
    void verifyProfile(String profileId);

    /**
     * Отклонить профиль пользователя
     *
     * @param profileDto объект с некорректными данными профиля
     */
    void cancelProfile(ProfileCancel profileDto);

}
