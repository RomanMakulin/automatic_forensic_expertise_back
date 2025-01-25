package com.example.auth.service.impl;

import com.example.auth.model.dto.RegistrationRequest;
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
     * Конструктор класса AuthServiceImpl.
     *
     * @param registrationService сервис для регистрации пользователей
     */
    public AuthServiceImpl(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * Регистрация пользователя в системе.
     *
     * @param request данные для регистрации
     */
    public void register(RegistrationRequest request) {
        registrationService.register(request);
    }

}
