package com.example.gigachat.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.UUID;

@Service
public class GigaChatService {

    private static final String API_URL = "https://gigachat.devices.sberbank.ru/api/v1/chat/completions";

    private static final RestTemplate restTemplate;

    // Статический блок инициализации
    static {
        restTemplate = new RestTemplate();
    }

    public static String sendMessage(String message, String token) {
        // Подготавливаем текст запроса
        String answer = "API (Application programming interface) — это контракт, который предоставляет программа. «Ко мне можно обращаться так и так, я обязуюсь делать то и это».";
        message = "Переформулируй текст и сделай его уникальным, чтобы смысл и логика не терялись: " + answer;

        // Создаём заголовки
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        headers.set("X-Request-ID", UUID.randomUUID().toString());
        headers.set("X-Session-ID", UUID.randomUUID().toString()); // Можно фиксировать для одной сессии
        headers.set("X-Client-ID", "b6874da0-bf06-410b-a150-fd5f9164a0b2");

        // Формируем JSON-запрос
        String jsonBody = "{ \"model\": \"GigaChat\", \"stream\": false, \"update_interval\": 0, \"messages\": ["
                + "{ \"role\": \"system\", \"content\": \"Отвечай как научный сотрудник\" },"
                + "{ \"role\": \"user\", \"content\": \"" + message + "\" }"
                + "] }";

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

        // Отправляем запрос
        ResponseEntity<String> response = restTemplate.exchange(
                API_URL, HttpMethod.POST, requestEntity, String.class);

        return response.getBody();
    }
}
