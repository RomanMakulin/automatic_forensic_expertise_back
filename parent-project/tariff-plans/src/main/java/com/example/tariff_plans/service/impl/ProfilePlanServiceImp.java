package com.example.tariff_plans.service.impl;

import com.example.tariff_plans.config.AppConfig;
import com.example.tariff_plans.mapper.PlanMapper;
import com.example.tariff_plans.model.Plan;
import com.example.tariff_plans.model.PlanDto;
import com.example.tariff_plans.model.ProfileDto;
import com.example.tariff_plans.repository.PlanRepository;
import com.example.tariff_plans.service.ProfilePlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Service
public class ProfilePlanServiceImp implements ProfilePlanService {

    private static final Logger log = LoggerFactory.getLogger(ProfilePlanServiceImp.class);

    private final PlanRepository planRepository;
    private final RestTemplate restTemplate;


    public ProfilePlanServiceImp(PlanRepository planRepository, RestTemplate restTemplate) {
        this.planRepository = planRepository;
        this.restTemplate = restTemplate;


    }


    @Override
    public List<PlanDto> getAllPlans() {
        List<Plan> plans = planRepository.findAll();
        return plans.stream()
                .map(PlanMapper.INSTANCE::planToPlanDto)
                .collect(Collectors.toList());
    }

    @Override
    public void selectPlan(UUID userId, UUID planId) {
        log.info("Обновление тарифа {} для пользователя {}", planId, userId);

        // Создаем заголовки для авторизации
        HttpHeaders headers = createAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // URL сервиса профиля
        String profileServiceUrl = "http://localhost:8090/api/profile";

        // Получаем текущий профиль пользователя через REST
        String url = String.format("%s/%s", profileServiceUrl, userId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            // Получаем профиль пользователя
            ResponseEntity<ProfileDto> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ProfileDto.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ProfileDto profile = response.getBody();

                // Обновляем только planId
                profile.setPlanId(planId);

                // Отправляем обновление профиля в профильный сервис
                String updateUrl = String.format("%s/update", profileServiceUrl);
                HttpEntity<ProfileDto> updateEntity = new HttpEntity<>(profile, headers);
                restTemplate.exchange(updateUrl, HttpMethod.PUT, updateEntity, Void.class);

                log.info("Тарифный план {} успешно обновлен для пользователя {}", planId, userId);
            } else {
                log.warn("Не удалось найти профиль для пользователя {}", userId);
            }
        } catch (RestClientException e) {
            log.error("Ошибка при вызове API профиля для обновления тарифа: {}", e.getMessage());
            throw new RuntimeException("Не удалось обновить тариф", e);
        }
    }


    @Override
    public PlanDto getCurrentPlan(UUID userId) {
        return null;
    }

    @Override
    public void renewPlan(UUID userId) {

    }

    @Override
    public void cancelPlan(UUID userId) {

    }

    @Override
    public void notifyUserAboutPlan(UUID userId) {

    }

    /**
     * Создание HTTP-заголовков с Bearer-токеном для авторизации
     *
     * @return HttpHeaders с заголовком Authorization
     */
    private HttpHeaders createAuthHeaders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalStateException("Невозможно получить JWT из SecurityContext");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt.getTokenValue());
        return headers;
    }


}