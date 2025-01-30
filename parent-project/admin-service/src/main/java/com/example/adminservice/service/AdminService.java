package com.example.adminservice.service;

import com.example.adminservice.api.dto.profile.ProfileDto;

import java.util.List;

/**
 * Интерфейс сервиса администратора
 */
public interface AdminService {

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
     * Изменить профиль пользователя (в том числе статус его верификации)
     *
     * @param profileDto профиль пользователя
     */
    void updateProfile(ProfileDto profileDto);
}
