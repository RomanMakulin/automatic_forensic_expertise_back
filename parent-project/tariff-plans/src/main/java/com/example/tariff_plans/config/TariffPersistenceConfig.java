package com.example.tariff_plans.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = {"com.example.model", "com.example.tariff_plans.model"})
public class TariffPersistenceConfig {
}