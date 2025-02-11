package com.example.minioservice;

import com.example.minioservice.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class MinioServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinioServiceApplication.class, args);
    }

}
