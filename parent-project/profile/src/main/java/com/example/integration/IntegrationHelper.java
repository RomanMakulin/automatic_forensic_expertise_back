package com.example.integration;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

/**
 * Интеграционный сервис для работы с JWT
 */
@Service
public class IntegrationHelper {

    /**
     * Создание HTTP-заголовков с Bearer-токеном для авторизации
     */
    public HttpHeaders createAuthHeaders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalStateException("Невозможно получить JWT из SecurityContext");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt.getTokenValue());
        return headers;
    }

}
