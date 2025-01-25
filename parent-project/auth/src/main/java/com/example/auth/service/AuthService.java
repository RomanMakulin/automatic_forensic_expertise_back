package com.example.auth.service;

import com.example.auth.model.dto.RegistrationRequest;

/**
 * Интерфейс для сервиса аутентификации.
 */
public interface AuthService {

    /**
     * Регистрирует нового пользователя.
     *
     * @param request запрос на регистрацию, содержащий информацию о пользователе
     */
    void register(RegistrationRequest request);

}
