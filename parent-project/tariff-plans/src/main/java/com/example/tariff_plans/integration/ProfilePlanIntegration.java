package com.example.tariff_plans.integration;

import com.example.tariff_plans.model.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProfilePlanIntegration {
    private static final Logger log = LoggerFactory.getLogger(ProfilePlanIntegration.class);
    private final RestTemplate restTemplate;
    private static final String PROFILE_SERVICE_URL = "http://localhost:8090/api/profile";

    public ProfileDto getProfile(UUID userId) {
        String url = String.format("%s?id=%s", PROFILE_SERVICE_URL, userId); // формируем правильный URL
        HttpEntity<Void> requestEntity = new HttpEntity<>(createAuthHeaders());

        try {
            ResponseEntity<ProfileDto> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ProfileDto.class);
            return response.getBody();
        } catch (RestClientException e) {
            log.error("Ошибка при получении профиля {}: {}", userId, e.getMessage());
            throw new RuntimeException("Не удалось получить профиль пользователя", e);
        }
    }

    public void updateProfile(ProfileDto profileDto) {
        String url = String.format("%s/update", PROFILE_SERVICE_URL);
        HttpEntity<ProfileDto> requestEntity = new HttpEntity<>(profileDto, createAuthHeaders());

        try {
            restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
            log.info("Профиль {} успешно обновлен", profileDto.getId());
        } catch (RestClientException e) {
            log.error("Ошибка при обновлении профиля {}: {}", profileDto.getId(), e.getMessage());
            throw new RuntimeException("Не удалось обновить профиль пользователя", e);
        }
    }

    private HttpHeaders createAuthHeaders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalStateException("Невозможно получить JWT из SecurityContext");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt.getTokenValue());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
