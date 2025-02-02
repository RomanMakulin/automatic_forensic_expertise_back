package com.example.tariff_plans.controller;


import com.example.tariff_plans.model.Plan;
import com.example.tariff_plans.service.AdminPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/plans")
@PreAuthorize("hasRole('ADMIN')") // Все методы доступны только администраторам
public class AdminTariffPlanController {

    private final AdminPlanService adminPlanService;

    public AdminTariffPlanController(AdminPlanService adminPlanService) {
        this.adminPlanService = adminPlanService;
    }

    /**
     * Получение списка всех тарифных планов.
     */
    @GetMapping
    public ResponseEntity<List<Plan>> getAllPlans() {
        return ResponseEntity.ok(adminPlanService.getAllPlans());
    }

    /**
     * Получение конкретного тарифного плана по его ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Plan> getPlanById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminPlanService.getPlanById(id));
    }

    /**
     * Создание нового тарифного плана.
     */
    @PostMapping
    public ResponseEntity<Plan> createPlan(@RequestBody Plan plan) {
        return ResponseEntity.ok(adminPlanService.createPlan(plan));
    }

    /**
     * Обновление существующего тарифного плана.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Plan> updatePlan(@PathVariable UUID id, @RequestBody Plan plan) {
        return ResponseEntity.ok(adminPlanService.updatePlan(id, plan));
    }

    /**
     * Удаление тарифного плана.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable UUID id) {
        adminPlanService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }
}