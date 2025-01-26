package com.example.auth.controller;

import com.example.auth.model.dto.LoginRequest;
import com.example.auth.model.dto.RegistrationRequest;
import com.example.auth.service.AuthService;
import exceptions.UserAlreadyExistsException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Контроллер для регистрации и аутентификации пользователей.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * Сервис для аутентификации пользователей.
     */
    private final AuthService authService;

    /**
     * Конструктор класса RegistrationController.
     *
     * @param authService сервис для аутентификации пользователей
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param request запрос на регистрацию, содержащий информацию о пользователе
     * @return ответ сервера, содержащий результат регистрации
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        try {
            authService.register(request);
            return ResponseEntity.ok("User registered successfully.");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed: " + e.getMessage());
        }
    }

    /**
     * Аутентификация пользователя.
     *
     * @param request запрос на аутентификацию, содержащий информацию о пользователе
     * @return ответ сервера, содержащий результат аутентификации
     */
    @GetMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // TODO реализовать
//    @PutMapping("/reset-password")
//    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
//        try {
//            authService.resetPassword(request);
//            return ResponseEntity.ok("Password reset successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Password reset failed: " + e.getMessage());
//
//
//        }
//    }




}