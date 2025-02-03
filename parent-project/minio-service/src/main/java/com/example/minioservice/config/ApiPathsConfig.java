package com.example.minioservice.config;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Пути API
 */
@Configuration
@ConfigurationProperties(prefix = "paths")
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ApiPathsConfig {

    /**
     * Пути сервиса почтовых рассылок
     */
    private Map<String, String> notification;

    /**
     * Пути сервиса авторизации
     */
    private Map<String, String> auth;

    /**
     * Пути сервиса фронтенда
     */
    private Map<String, String> frontend;

    /**
     * Пути сервиса профилей
     */
    private Map<String, String> profile;

    // Сеттеры (Lombok не генерирует сеттеры для Map автоматически)
    public void setNotification(Map<String, String> notification) {
        this.notification = notification;
    }

    public void setAuth(Map<String, String> auth) {
        this.auth = auth;
    }

    public void setFrontend(Map<String, String> frontend) {
        this.frontend = frontend;
    }

    public void setProfile(Map<String, String> profile) {
        this.profile = profile;
    }
}