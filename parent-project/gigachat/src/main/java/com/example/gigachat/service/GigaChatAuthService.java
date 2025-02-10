package com.example.gigachat.service;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

public class GigaChatAuthService {

    private static final String TOKEN_URL = "https://ngw.devices.sberbank.ru:9443/api/v2/oauth";
    private static final String AUTH_HEADER = "Bearer MDM2MjYzMWEtZmI1MS00NGZkLTk2MzQtYmY3YzE3ODZlZmZhOmI3YzQ3NjFlLTdkM2MtNDEyYi04OWQ4LTM0MzRhZTNkYWVlZA==";

    private static final RestTemplate restTemplate;

    // Статический блок инициализации
    static {
        restTemplate = new RestTemplate();
    }

    public static String getAuthToken() {
        // Создаем заголовки
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("RqUID", UUID.randomUUID().toString()); // Делаем уникальным
        headers.set("Authorization", AUTH_HEADER);

        // Формируем тело запроса
        String body = "scope=GIGACHAT_API_PERS";

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        // Выполняем POST-запрос
        ResponseEntity<String> response = restTemplate.exchange(
                TOKEN_URL, HttpMethod.POST, requestEntity, String.class);

        return extractToken(response.getBody());
    }

    // Извлекаем токен из JSON-ответа
    private static String extractToken(String json) {
        return json.split("\"access_token\":\"")[1].split("\"")[0];
    }
}
