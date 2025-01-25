package com.example.auth.service.impl;

import com.example.auth.model.dto.LoginRequest;
import com.example.auth.model.dto.RegistrationRequest;
import com.example.auth.service.AuthService;
import com.example.auth.service.LoginService;
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
    private final LoginService loginService;

    /**
     * Конструктор класса AuthServiceImpl.
     *
     * @param registrationService сервис для регистрации пользователей
     */
    public AuthServiceImpl(RegistrationService registrationService,
                           LoginService loginService) {
        this.registrationService = registrationService;
        this.loginService = loginService;
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
     * @return ответ сервера, содержащий результат аутентификации
     */
    @Override
    public ResponseEntity<?> login(LoginRequest request) {
        return loginService.login(request);
    }

}
