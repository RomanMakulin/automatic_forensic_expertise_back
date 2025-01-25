package com.example.auth.service;

import com.example.auth.model.dto.LoginRequest;
import com.example.auth.model.dto.RegistrationRequest;
import org.springframework.http.ResponseEntity;

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
     * @return ответ сервера
     */
    ResponseEntity<?> login(LoginRequest request);

}
