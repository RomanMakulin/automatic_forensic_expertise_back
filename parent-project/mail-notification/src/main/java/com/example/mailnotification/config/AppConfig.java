package com.example.mailnotification.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Общая конфигурация приложения
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    private Map<String, String> services;  // URL сервисов
    private CorsConfig cors;               // CORS конфигурация
    private ApiPaths paths;                // Пути API

    @Data
    public static class CorsConfig {
        private List<String> allowedOrigins;
    }

    @Data
    public static class ApiPaths {
        private Map<String, String> notification;
        private Map<String, String> auth;
        private Map<String, String> frontend;
        private Map<String, String> profile;
        private Map<String, String> minio;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}