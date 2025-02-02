package com.example.tariff_plans.service.impl;

import com.example.tariff_plans.model.Plan;
import com.example.tariff_plans.repository.PlanRepository;
import com.example.tariff_plans.service.PlanService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;

    public PlanServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    @Override
    public Plan getPlanById(UUID id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tariff plan not found"));
    }

    @Override
    public Plan createPlan(Plan plan) {
        return planRepository.save(plan);
    }

    @Override
    public Plan updatePlan(UUID id, Plan plan) {
        Plan existingPlan = getPlanById(id);
        existingPlan.setName(plan.getName());
        existingPlan.setDescription(plan.getDescription());
        existingPlan.setPrice(plan.getPrice());
        existingPlan.setStorageLimit(plan.getStorageLimit());
        existingPlan.setStartDate(plan.getStartDate());
        existingPlan.setEndDate(plan.getEndDate());
        return planRepository.save(existingPlan);
    }

    @Override
    public void deletePlan(UUID id) {
        planRepository.deleteById(id);
    }
}