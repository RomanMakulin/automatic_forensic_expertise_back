package com.example.tariff_plans.controller;

import com.example.tariff_plans.model.Plan;
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
     * Покупка нового тарифного плана пользователем.
     */
    @PostMapping("/{profileId}/buy/{planId}")
    public ResponseEntity<Plan> buyPlan(@PathVariable UUID profileId, @PathVariable UUID planId) {
        Plan plan = profilePlanService.buyPlan(profileId, planId);
        return ResponseEntity.ok(plan);
    }

    /**
     * Получение активного тарифного плана пользователя.
     */
    @GetMapping("/{profileId}/active")
    public ResponseEntity<Plan> getActivePlan(@PathVariable UUID profileId) {
        Plan activePlan = profilePlanService.getActivePlan(profileId);
        return ResponseEntity.ok(activePlan);
    }

    /**
     * Оплата текущего активного тарифного плана.
     */
    @PostMapping("/{profileId}/pay")
    public ResponseEntity<String> payForPlan(@PathVariable UUID profileId) {
        profilePlanService.payForPlan(profileId);
        return ResponseEntity.ok("Тариф успешно оплачен");
    }

    /**
     * Проверка статуса оплаты тарифного плана.
     */
    @GetMapping("/{profileId}/status")
    public ResponseEntity<Boolean> isPlanPaid(@PathVariable UUID profileId) {
        boolean isPaid = profilePlanService.isPlanPaid(profileId);
        return ResponseEntity.ok(isPaid);
    }

    /**
     * Получение списка доступных тарифных планов.
     */
    @GetMapping("/available")
    public ResponseEntity<List<Plan>> getAvailablePlans() {
        List<Plan> plans = profilePlanService.getAvailablePlans();
        return ResponseEntity.ok(plans);
    }
}