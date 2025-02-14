package com.example.auth.integrations.profile;

import com.example.auth.api.dto.profile.ProfileDTO;
import com.example.auth.config.AppConfig;
import com.example.auth.integrations.IntegrationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

/**
 * Реализация сервиса интеграций с модулем профиля
 */
@Service
public class ProfileIntegrationImpl implements ProfileIntegration {

    private static final Logger log = LoggerFactory.getLogger(ProfileIntegrationImpl.class);
    private final RestTemplate restTemplate;
    private final AppConfig appConfig;
    private final IntegrationHelper integrationHelper;

    public ProfileIntegrationImpl(RestTemplate restTemplate,
                                  AppConfig appConfig,
                                  IntegrationHelper integrationHelper) {
        this.restTemplate = restTemplate;
        this.appConfig = appConfig;
        this.integrationHelper = integrationHelper;
    }

    /**
     * Получает профиль пользователя
     *
     * @param userId идентификатор пользователя
     * @return профиль пользователя
     */
    @Override
    public ProfileDTO getProfileRequest(UUID userId, String token) {
        String baseUrl = appConfig.getPaths().getProfile().get("get-by-user-id");
        String requestUrl = integrationHelper.urlBuilder(baseUrl, Map.of("id", userId.toString()));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ProfileDTO> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, ProfileDTO.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Ошибка при получении профиля: {}", e.getMessage(), e);
            return null;
        }
    }


}
