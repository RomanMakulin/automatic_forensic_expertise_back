package com.example.tariff_plans.service.impl;

import com.example.tariff_plans.mapper.PlanMapper;
import com.example.tariff_plans.model.Plan;
import com.example.tariff_plans.model.PlanDto;
import com.example.tariff_plans.repository.PlanRepository;
import com.example.tariff_plans.service.ProfilePlanService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class ProfilePlanServiceImp implements ProfilePlanService {

    private final PlanRepository planRepository;

    public ProfilePlanServiceImp(PlanRepository planRepository) {
        this.planRepository = planRepository;
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
}