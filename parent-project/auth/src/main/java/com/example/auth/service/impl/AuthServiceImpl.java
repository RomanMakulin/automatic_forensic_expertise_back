package com.example.auth.service.impl;

import com.example.auth.model.dto.LoginRequest;
import com.example.auth.model.dto.RegistrationRequest;
import com.example.auth.service.AuthService;
import com.example.auth.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    private final AuthService authService;

    /**
     * Конструктор класса AuthServiceImpl.
     *
     * @param registrationService сервис для регистрации пользователей
     */
    public AuthServiceImpl(RegistrationService registrationService,
                           AuthService authService) {
        this.registrationService = registrationService;
        this.authService = authService;
    }

    /**
     * Регистрация пользователя в системе.
     *
     * @param request данные для регистрации
     */
    public void register(RegistrationRequest request) {
        registrationService.register(request);
    }

    @Override
    public ResponseEntity<?> login(LoginRequest request) {
        return authService.login(request);
    }

}
