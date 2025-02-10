package com.example.gigachat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApiConfig {

    // Ваш API ключ
    @Value("${gigachat.api.key}")
    private String apiKey;

    @Bean
    public String gigachatApiKey() {
        return apiKey;
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}