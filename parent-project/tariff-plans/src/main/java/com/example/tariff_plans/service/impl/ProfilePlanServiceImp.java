package com.example.tariff_plans.service.impl;

import com.example.model.Profile;

import com.example.repository.ProfileRepository;
import com.example.tariff_plans.model.Plan;
import com.example.tariff_plans.repository.PlanRepository;
import com.example.tariff_plans.service.ProfilePlanService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ProfilePlanServiceImp implements ProfilePlanService {

    private final PlanRepository planRepository;
    private final ProfileRepository profileRepository;



    public ProfilePlanServiceImp(PlanRepository planRepository, ProfileRepository profileRepository) {
        this.planRepository = planRepository;
        this.profileRepository = profileRepository;
    }

    @Transactional
    @Override
    public Plan buyPlan(UUID profileId, UUID planId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Профиль не найден"));

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Тарифный план не найден"));

        // Проверяем, не куплен ли уже этот тариф
        Plan activePlan = getActivePlan(profileId);
        if (activePlan != null) {
            throw new RuntimeException("У пользователя уже есть активный тарифный план");
        }

        // Создаём новый экземпляр тарифа для профиля
        Plan purchasedPlan = Plan.builder()
                .id(UUID.randomUUID()) // Генерируем новый ID
                .profile(profile)
                .name(plan.getName())
                .description(plan.getDescription())
                .price(plan.getPrice())
                .storageLimit(plan.getStorageLimit())
                .startDate(LocalDate.now()) // Начинаем тариф с текущей даты
                .endDate(LocalDate.now().plusMonths(1)) // Например, срок тарифа — 1 месяц
                .build();

        return planRepository.save(purchasedPlan);
    }

    @Override
    public Plan getActivePlan(UUID profileId) {
        return planRepository.findByProfileIdAndEndDateAfter(profileId, LocalDate.now())
                .orElse(null);
    }

    @Transactional
    @Override
    public void payForPlan(UUID profileId) {
        Plan activePlan = getActivePlan(profileId);
        if (activePlan == null) {
            throw new RuntimeException("У пользователя нет активного тарифного плана для оплаты");
        }

        // Здесь должна быть интеграция с модулем оплаты
        // Например, вызываем метод оплаты из PaymentService

        System.out.println("Оплата тарифа " + activePlan.getName() + " для профиля " + profileId);
    }

    @Override
    public boolean isPlanPaid(UUID profileId) {
        Plan activePlan = getActivePlan(profileId);
        if (activePlan == null) {
            return false;
        }

        // Здесь можно проверить статус оплаты в отдельной системе платежей
        // Пока просто возвращаем true, если у пользователя есть активный тариф
        return true;
    }

    @Override
    public List<Plan> getAvailablePlans() {
        return planRepository.findAll();
    }
}