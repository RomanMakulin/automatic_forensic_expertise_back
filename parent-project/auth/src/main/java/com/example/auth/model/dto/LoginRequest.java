package com.example.auth.model.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO для авторизации пользователя
 */
public class LoginRequest {

    @NotBlank(message = "Почта пользователя не может быть пустой")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginRequest() {
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
