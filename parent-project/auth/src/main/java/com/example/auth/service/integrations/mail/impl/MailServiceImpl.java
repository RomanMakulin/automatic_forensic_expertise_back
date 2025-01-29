package com.example.auth.service.integrations.mail.impl;

import com.example.auth.model.dto.MailRequest;
import com.example.auth.service.integrations.mail.MailService;
import com.example.auth.util.ApiPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public MailServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Отправляет email
     *
     * @param mailRequest запрос на отправку письма
     */
    @Override
    public void sendMail(MailRequest mailRequest) {
        try {
            restTemplate.postForEntity(ApiPaths.NotificationService.SEND_MAIL, mailRequest, Void.class);
        } catch (Exception e) {
            log.error("Failed to send email to notification service: {}", mailRequest, e);
            throw new RuntimeException(e.getMessage());
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
            restTemplate.postForEntity(ApiPaths.NotificationService.PUBLIC_SEND_MAIL, mailRequest, Void.class);
            log.debug("Email request successfully sent to notification service: {}", mailRequest);
        } catch (Exception e) {
            log.error("Failed to send email to notification service: {}", mailRequest, e);
        }
    }

}
