package com.example.auth.service.auth;

import com.example.auth.model.User;
import com.example.auth.model.dto.LoginRequest;
import com.example.auth.model.dto.RegistrationRequest;
import org.keycloak.representations.AccessTokenResponse;

import java.util.UUID;

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

    /**
     * Авторизует пользователя.
     *
     * @param request запрос на авторизацию, содержащий информацию о пользователе
     * @return токен пользователя
     */
    AccessTokenResponse login(LoginRequest request);

    /**
     * Подтверждает регистрацию пользователя.
     *
     * @param userId идентификатор пользователя
     */
    void verifyRegistration(UUID userId);

    /**
     * Выходит из системы текущего пользователя.
     */
    void logout();

    /**
     * Получить текущего авторизованного пользователя.
     *
     * @return текущий авторизованный пользователь
     */
    User getAuthenticatedUser();

}
