package com.example.tariff_plans.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {
        "com.example.tariff_plans.model",  // Где находится `Plan`
        "com.example.model"  // Где находится `Profile`
})
@EnableJpaRepositories(basePackages = {
        "com.example.tariff_plans.repository",
        "com.example.repository" // Добавляем репозитории из модуля profile
})
public class TariffPersistenceConfig {
}
