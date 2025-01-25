package com.example.auth.service;

import com.example.auth.model.dto.LoginRequest;
import org.springframework.http.ResponseEntity;

/**
 * Интерфейс для сервиса аутентификации.
 */
public interface LoginService {

    /**
     * Метод для аутентификации пользователя.
     *
     * @param request запрос на аутентификацию, содержащий информацию о пользователе
     * @return ответ сервера, содержащий результат аутентификации
     */
    ResponseEntity<?> login(LoginRequest request);

}
