package com.example.auth.api.rest;

import com.example.auth.api.dto.LoginRequest;
import com.example.auth.api.dto.RegistrationRequest;
import com.example.auth.config.AppConfig;
import com.example.auth.service.auth.AuthService;
import exceptions.UserAlreadyExistsException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

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

    private final AppConfig appConfig;

    /**
     * Конструктор класса RegistrationController.
     *
     * @param authService сервис для аутентификации пользователей
     */
    public AuthController(AuthService authService,
                          AppConfig appConfig) {
        this.authService = authService;
        this.appConfig = appConfig;
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
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed (check password): " + e.getMessage());
        }
    }

    @GetMapping("/verify-email/{userId}")
    public RedirectView verifyEmail(@PathVariable UUID userId) {
        authService.verifyRegistration(userId);
        String redirectUrl = appConfig.getPaths().getFrontend().get("login");
        return new RedirectView(redirectUrl);
    }

}