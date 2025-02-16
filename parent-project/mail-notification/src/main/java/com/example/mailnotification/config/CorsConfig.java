package com.example.mailnotification.config;


import com.example.mailnotification.util.CorsConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Конфигурация CORS.
 */
@Configuration
public class CorsConfig {

    /**
     * Настройки конфигурации CORS.
     */
    private final CorsConfigProperties corsConfigProperties;

    public CorsConfig(CorsConfigProperties corsConfigProperties) {
        this.corsConfigProperties = corsConfigProperties;
    }

    /**
     * Конфигурация CORS.
     *
     * @return источник конфигурации CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsConfigProperties.getAllowedOrigins()); // Разрешенные домены
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Разрешенные HTTP-методы
        configuration.setAllowedHeaders(List.of("*")); // Разрешенные заголовки
        configuration.setAllowCredentials(true); // Разрешить отправку cookie

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Применить ко всем эндпоинтам
        return source;
    }

}
