package com.example.tariff_plans;

import com.example.tariff_plans.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class TariffPlansApplication {

	public static void main(String[] args) {
		SpringApplication.run(TariffPlansApplication.class, args);
	}

}
