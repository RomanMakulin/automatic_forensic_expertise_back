package com.example.tariff_plans.service.impl;

import com.example.tariff_plans.config.AppConfig;
import com.example.tariff_plans.integration.ProfilePlanIntegration;
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



@Service
public class ProfilePlanServiceImp implements ProfilePlanService {

    private static final Logger log = LoggerFactory.getLogger(ProfilePlanServiceImp.class);

    private final PlanRepository planRepository;
    private final ProfilePlanIntegration profilePlanIntegration;

    public ProfilePlanServiceImp(PlanRepository planRepository, ProfilePlanIntegration profilePlanIntegration) {
        this.planRepository = planRepository;
        this.profilePlanIntegration = profilePlanIntegration;
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
        log.info("Начинаем выбор тарифа для пользователя с ID: {}", userId);

        // 1. Получаем профиль пользователя
        ProfileDto profile = profilePlanIntegration.getProfile(userId);
        if (profile == null) {
            log.error("Профиль пользователя с ID {} не найден", userId);
            throw new RuntimeException("Профиль пользователя не найден");
        }
        log.info("Профиль пользователя с ID {} успешно получен", userId);

        // 2. Получаем тариф по ID
        log.info("Получаем тариф с ID {}", planId);
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> {
                    log.error("Тариф с ID {} не найден", planId);
                    return new RuntimeException("Тариф с ID " + planId + " не найден");
                });
        log.info("Тариф с ID {} успешно получен", planId);

        // 3. Проверяем, активен ли тариф
        if (!plan.getActive()) {
            log.warn("Тариф с ID {} не активен", planId);
            throw new RuntimeException("Этот тариф не активен");
        }
        log.info("Тариф с ID {} активен", planId);

        // 4. Проверяем, если у пользователя уже есть активный тариф
        if (profile.getPlanId() != null) {
            log.warn("У пользователя с ID {} уже есть выбранный тариф с ID {}", userId, profile.getPlanId());
            throw new RuntimeException("У пользователя уже есть выбранный тариф");
        }
        log.info("У пользователя с ID {} нет активного тарифа, можно выбрать новый", userId);

        // 5. Обновляем профиль пользователя, выбираем новый тариф
        log.info("Обновляем профиль пользователя с ID {} и выбираем тариф с ID {}", userId, plan.getId());
        profile.setPlanId(plan.getId());
        profilePlanIntegration.updateProfile(profile);

        log.info("Профиль пользователя с ID {} успешно обновлен с новым тарифом с ID {}", userId, plan.getId());
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