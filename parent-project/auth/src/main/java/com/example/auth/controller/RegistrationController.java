package com.example.auth.controller;

import com.example.auth.model.dto.LoginRequest;
import com.example.auth.model.dto.RegistrationRequest;
import com.example.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для регистрации и аутентификации пользователей.
 */
@RestController
@RequestMapping("/public/auth")
public class RegistrationController {

    /**
     * Сервис для аутентификации пользователей.
     */
    private final AuthService authService;

    /**
     * Конструктор класса RegistrationController.
     *
     * @param authService сервис для аутентификации пользователей
     */
    public RegistrationController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param request запрос на регистрацию, содержащий информацию о пользователе
     * @return ответ сервера, содержащий результат регистрации
     */
    @GetMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegistrationRequest request) {
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    /**
     * Аутентификация пользователя.
     *
     * @param request запрос на аутентификацию, содержащий информацию о пользователе
     * @return ответ сервера, содержащий результат аутентификации
     */
    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

}

