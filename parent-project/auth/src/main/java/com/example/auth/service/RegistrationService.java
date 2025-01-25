package com.example.auth.service;

import com.example.auth.model.dto.RegistrationRequest;

/**
 * Интерфейс для сервиса регистрации пользователей.
 */
public interface RegistrationService {

    /**
     * Регистрирует нового пользователя.
     *
     * @param request запрос на регистрацию, содержащий информацию о пользователе
     */
    void register(RegistrationRequest request);

}
