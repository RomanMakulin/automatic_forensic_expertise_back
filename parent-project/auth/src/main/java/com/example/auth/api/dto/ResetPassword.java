package com.example.auth.api.dto;

import lombok.*;

/**
 * Объект DTO для смены пароля
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResetPassword {

    /**
     * Токен для смены пароля
     */
    private String token;

    /**
     * Новый пароль
     */
    private String password;

}
