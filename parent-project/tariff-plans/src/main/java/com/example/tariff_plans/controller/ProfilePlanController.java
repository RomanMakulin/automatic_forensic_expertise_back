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


}