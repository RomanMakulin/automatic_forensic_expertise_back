package com.example.tariff_plans.service;

import com.example.tariff_plans.model.Plan;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public interface ProfilePlanService {
    @Transactional
    Plan buyPlan(UUID profileId, UUID planId);

    Plan getActivePlan(UUID profileId);

    @Transactional
    void payForPlan(UUID profileId);

    boolean isPlanPaid(UUID profileId);

    List<Plan> getAvailablePlans();
}
