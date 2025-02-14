package com.example.auth.integrations.profile;

import com.example.auth.api.dto.profile.ProfileDTO;

import java.util.UUID;

/**
 * Интерфейс интеграции с модулем профиля
 */
public interface ProfileIntegration {

    /**
     * Получает профиль пользователя
     *
     * @param userId идентификатор пользователя
     * @return профиль пользователя
     */
    ProfileDTO getProfileRequest(UUID userId, String token);

}
