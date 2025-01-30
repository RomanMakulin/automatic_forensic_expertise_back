package com.example.auth.api.rest;

import com.example.auth.api.dto.ResetPassword;
import com.example.auth.service.auth.PasswordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    /**
     * Отправляет запрос на восстановление пароля пользователя на почту с генерацией ссылки на восстановление.
     *
     * @param email адрес пользователя
     * @return ответ сервера, содержащий результат восстановления пароля
     */
    @GetMapping("/reset-password/{email}")
    public ResponseEntity<?> resetPasswordRequest(@PathVariable String email) {
        try {
            passwordService.resetPasswordRequest(email);
            return ResponseEntity.ok("Запрос на восстановление пароля успешно направлен на почту " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Password reset failed: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPassword resetPassword) {
        try {
            passwordService.resetPassword(resetPassword);
            return ResponseEntity.ok("Пароль успешно изменен");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Password reset failed: " + e.getMessage());

        }
    }

}