package com.example.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация безопасности приложения.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Конфигурация безопасности
     *
     * @param http объект для конфигурации безопасности
     * @return объект для конфигурации безопасности
     * @throws Exception ошибка
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Отключение CSRF, так как мы используем JWT
                .cors(Customizer.withDefaults()) // включаем CORS
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/register", "/reset-password/**").anonymous()
                        .requestMatchers("/api/auth/**").anonymous()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults()) // Проверка JWT токена с учетом issuer-uri
                );

        return http.build();
    }

}
