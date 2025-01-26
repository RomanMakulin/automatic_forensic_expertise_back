package com.example.auth.service.impl;

import com.example.auth.model.User;
import com.example.auth.model.dto.LoginRequest;
import com.example.auth.model.dto.RegistrationRequest;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.AuthService;
import com.example.auth.service.KeycloakAdminService;
import com.example.auth.service.LoginService;
import com.example.auth.service.RegistrationService;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Сервис для аутентификации пользователей.
 */
@Service
public class AuthServiceImpl implements AuthService {

    /**
     * Сервис для регистрации пользователей.
     */
    private final RegistrationService registrationService;

    /**
     * Сервис для аутентификации пользователей.
     */
    private final LoginService loginService;

    /**
     * Репозиторий для работы с пользователями.
     */
    private final UserRepository userRepository;

    /**
     * Сервис для работы с админкой Keycloak.
     */
    private final KeycloakAdminService keycloakAdminService;

    /**
     * Конструктор класса AuthServiceImpl.
     *
     * @param registrationService сервис для регистрации пользователей
     */
    public AuthServiceImpl(RegistrationService registrationService,
                           LoginService loginService,
                           UserRepository userRepository,
                           @Lazy KeycloakAdminService keycloakAdminService) {
        this.registrationService = registrationService;
        this.loginService = loginService;
        this.userRepository = userRepository;
        this.keycloakAdminService = keycloakAdminService;
    }

    /**
     * Регистрация пользователя в системе.
     *
     * @param request данные для регистрации
     */
    public void register(RegistrationRequest request) {
        registrationService.register(request);
    }

    /**
     * Аутентификация пользователя.
     *
     * @param request запрос на аутентификацию, содержащий информацию о пользователе
     * @return токен пользователя
     */
    @Override
    public AccessTokenResponse login(LoginRequest request) {
        return loginService.login(request);
    }

    /**
     * Выход пользователя из системы.
     */
    @Override
    public void logout() {
        keycloakAdminService.logoutUserById(getAuthenticatedUser().getKeycloakId());
    }

    /**
     * Получение аутентифицированного пользователя.
     *
     * @return аутентифицированный пользователь
     */
    @Override
    public User getAuthenticatedUser() {
        // Извлекаем Authentication из SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Извлекаем токен JWT
        Jwt jwt = (Jwt) authentication.getPrincipal();

        // Извлекаем данные из токена
        String email = jwt.getClaim("email"); // Email пользователя
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

}
