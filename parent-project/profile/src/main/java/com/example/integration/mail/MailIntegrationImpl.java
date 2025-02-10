package com.example.integration.mail;

import com.example.config.ApiPathsConfig;
import com.example.integration.IntegrationHelper;
import com.example.integration.mail.dto.MailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Сервис для интеграции с сервисом отправки писем.
 */
@Service
public class MailIntegrationImpl implements MailIntegration{

    private static final Logger log = LoggerFactory.getLogger(MailIntegrationImpl.class);

    private final RestTemplate restTemplate;

    private final ApiPathsConfig apiPathsConfig;

    private final IntegrationHelper integrationHelper;

    /**
     * Конструктор класса MailIntegration.
     *
     * @param restTemplate       клиент для выполнения HTTP-запросов
     * @param apiPathsConfig     конфигурация путей API
     * @param integrationHelper  хелпер для работы с интеграцией
     */
    public MailIntegrationImpl(RestTemplate restTemplate,
                               ApiPathsConfig apiPathsConfig,
                               IntegrationHelper integrationHelper) {
        this.restTemplate = restTemplate;
        this.apiPathsConfig = apiPathsConfig;
        this.integrationHelper = integrationHelper;
    }

    /**
     * Отправляет email.
     *
     * @param mailRequest запрос на отправку письма
     */
    @Override
    public void sendMail(MailRequest mailRequest) {
        try {
            String mailApi = apiPathsConfig.getNotification().get("send-mail");

            // Получаем заголовки с токеном авторизации
            HttpHeaders headers = integrationHelper.createAuthHeaders();

            // Создаем HTTP-запрос с заголовками и телом
            HttpEntity<MailRequest> requestEntity = new HttpEntity<>(mailRequest, headers);

            ResponseEntity<Void> response = restTemplate.exchange(mailApi, HttpMethod.POST, requestEntity, Void.class);

            checkResponseAnswer(response); // Проверяем ответ от сервиса отправки писем
            log.info("Mail sent successfully");
        } catch (Exception e) {
            log.error("Failed to send email to notification service: {}", mailRequest, e);
            throw new RuntimeException("Registration failed with send email to notification service: " + mailRequest, e);
        }
    }

    /**
     * Проверяет ответ от сервиса отправки писем.
     * Если ответ от сервиса отправки писем не 200, то выбрасывает исключение.
     *
     * @param response ответ от сервиса отправки писем
     */
    private void checkResponseAnswer(ResponseEntity<Void> response) {
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Error while checking response answer");
        }
    }


}


