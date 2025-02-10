package com.example.auth.integrations.mail.impl;

import com.example.auth.config.ApiPathsConfig;
import com.example.auth.api.dto.MailRequest;
import com.example.auth.integrations.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Реализация сервиса отправки писем
 */
@Service
public class MailServiceImpl implements MailService {

    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    private final RestTemplate restTemplate;

    private final ApiPathsConfig apiPathsConfig;

    public MailServiceImpl(RestTemplate restTemplate,
                           ApiPathsConfig apiPathsConfig) {
        this.restTemplate = restTemplate;
        this.apiPathsConfig = apiPathsConfig;
    }

    /**
     * Отправляет email
     *
     * @param mailRequest запрос на отправку письма
     */
    @Override
    public void sendMail(MailRequest mailRequest) {
        try {
            String mailApi = apiPathsConfig.getNotification().get("send-mail");
            restTemplate.postForEntity(mailApi, mailRequest, Void.class);
        } catch (Exception e) {
            log.error("Failed to send email to notification service: {}", mailRequest, e);
            throw new RuntimeException("Registration failed with send email to notification service: " + mailRequest, e);
        }
    }

    /**
     * Отправляет email (запрос для неавторизованного пользователя)
     *
     * @param mailRequest запрос на отправку письма
     */
    @Override
    public void publicSendMail(MailRequest mailRequest) {
        try {
            String mailApi = apiPathsConfig.getNotification().get("public-send-mail");
            ResponseEntity<Void> response = restTemplate.postForEntity(mailApi, mailRequest, Void.class);
            checkResponseAnswer(response);
            log.debug("Email request successfully sent to notification service: {}", mailRequest);
        } catch (Exception e) {
            log.error("Failed to send email to notification service: {}", mailRequest, e);
            throw new RuntimeException("Registration failed with send email to notification service: " + mailRequest, e);
        }
    }

    /**
     * Проверяет ответ от сервиса отправки писем
     *
     * @param response ответ от сервиса отправки писем
     */
    private void checkResponseAnswer(ResponseEntity<Void> response){
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Registration failed with send email to notification service");
        }
    }

}
