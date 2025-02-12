package com.example.tariff_plans.controller;

import com.example.tariff_plans.model.Plan;
import com.example.tariff_plans.model.PlanDto;
import com.example.tariff_plans.service.ProfilePlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/profile-plans")
public class ProfilePlanController {

    private final ProfilePlanService profilePlanService;


    public ProfilePlanController(ProfilePlanService profilePlanService) {
        this.profilePlanService = profilePlanService;
    }

    /**
     * Получить список всех доступных тарифных планов.
     *
     * @return Список тарифных планов
     */
    @GetMapping
    public ResponseEntity<List<PlanDto>> getAllPlans() {

        List<PlanDto> plans = profilePlanService.getAllPlans();
        return ResponseEntity.ok(plans);
    }

    /**
     * Установить тарифный план для пользователя.
     *
     * @param userId ID пользователя
     * @param planId ID тарифа
     * @return ResponseEntity с кодом 200 при успешном обновлении
     */
    @PutMapping("/{userId}/select-plan/{planId}")
    public ResponseEntity<Void> selectPlan(@PathVariable UUID userId, @PathVariable UUID planId) {
        profilePlanService.selectPlan(userId, planId);
        return ResponseEntity.ok().build();
    }


}