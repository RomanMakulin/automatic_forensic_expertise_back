package com.example.tariff_plans.service;

import com.example.tariff_plans.model.Plan;

import java.util.List;
import java.util.UUID;

public interface AdminPlanService {

    List<Plan> getAllPlans();

    Plan getPlanById(UUID id);

    Plan createPlan(Plan plan);

    Plan updatePlan(UUID id, Plan plan);

    void deletePlan(UUID id);

}