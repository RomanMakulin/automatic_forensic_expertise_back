package com.example.auth.service.auth;

import com.example.auth.api.dto.AuthUserDetailsResponse;
import com.example.auth.api.dto.LoginRequest;
import org.keycloak.representations.AccessTokenResponse;

/**
 * Интерфейс для сервиса аутентификации.
 */
public interface LoginService {

    /**
     * Метод для аутентификации пользователя.
     *
     * @param request запрос на аутентификацию, содержащий информацию о пользователе
     * @return токен пользователя
     */
    AuthUserDetailsResponse login(LoginRequest request);

}
