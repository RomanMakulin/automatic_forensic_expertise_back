package com.example.auth.service;

import com.example.auth.model.dto.ResetPassword;

import java.util.UUID;

/**
 * Интерфейс для сервиса сброса пароля.
 */
public interface PasswordService {

    /**
     * Запрос на сброс пароля по электронной почте.
     *
     * @param email электронная почта
     */
    void resetPasswordRequest(String email);

    /**
     * Сброс пароля.
     *
     * @param resetPassword  объект DTO с данными для сброса пароля
     */
    void resetPassword(ResetPassword resetPassword);

}
