package com.example.auth.service;

import com.example.auth.model.dto.RegistrationRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    private final RegistrationService registrationService;

    public AuthServiceImpl(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * Регистрация пользователя в системе
     *
     * @param request данные для регистрации
     */
    public void register(RegistrationRequest request) {
        registrationService.register(request);
    }

}
