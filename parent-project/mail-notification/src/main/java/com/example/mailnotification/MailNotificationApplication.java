package com.example.mailnotification;

import com.example.mailnotification.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class MailNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailNotificationApplication.class, args);
    }

}
