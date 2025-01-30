package com.example.auth.model.dto;

/**
 * Объект DTO для смены пароля
 */
public class ResetPassword {

    /**
     * Токен для смены пароля
     */
    private String token;

    /**
     * Новый пароль
     */
    private String password;

    public ResetPassword(String token, String password) {
        this.token = token;
        this.password = password;
    }

    public ResetPassword() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
