package com.example.auth.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Пути сервисов из конфига
 */
@Configuration
@ConfigurationProperties(prefix = "services")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceUrls {

    /**
     * Сервис отправки почтовых уведомлений
     */
    private String notification;

    /**
     * Сервис авторизации
     */
    private String auth;

    /**
     * Сервис фронтенда
     */
    private String frontend;

}