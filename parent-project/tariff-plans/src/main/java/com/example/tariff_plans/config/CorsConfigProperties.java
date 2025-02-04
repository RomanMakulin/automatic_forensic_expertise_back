package com.example.tariff_plans.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Настройки конфигурации CORS
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsConfigProperties {

    /**
     * Разрешенные домены.
     */
    private List<String> allowedOrigins;

}
