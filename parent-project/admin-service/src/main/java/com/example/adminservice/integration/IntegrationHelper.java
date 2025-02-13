package com.example.adminservice.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * Вспомогательный сервис для интеграций с другими сервисам
 */
@Service
public class IntegrationHelper {

    private static final Logger log = LoggerFactory.getLogger(IntegrationHelper.class);
    private final RestTemplate restTemplate;

    public IntegrationHelper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Создание HTTP-заголовков с Bearer-токеном для авторизации
     */
    public HttpHeaders createAuthHeaders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalStateException("Невозможно получить JWT из SecurityContext");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt.getTokenValue());
        return headers;
    }

    /**
     * Создание URL с параметрами
     */
    public String urlBuilder(String baseUrl, Map<String, String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        if (params != null) {
            params.forEach(builder::queryParam);
        }

        return builder.toUriString();
    }

    /**
     * Универсальный GET запрос на получение данных из другого сервиса
     *
     * @param requestUrl   url запроса
     * @param responseType тип возвращаемого объекта
     * @return объект данных
     */
    public <T> T simpleGetRequest(String requestUrl, Class<T> responseType) {
        return executeGetRequest(requestUrl, responseType, null);
    }

    /**
     * Универсальный GET запрос на получение данных из другого сервиса
     *
     * @param requestUrl   url запроса
     * @param responseType тип возвращаемого объекта
     * @param <T>          тип возвращаемого объекта
     * @return объект данных
     */
    public <T> T simpleGetRequest(String requestUrl, ParameterizedTypeReference<T> responseType) {
        return executeGetRequest(requestUrl, null, responseType);
    }

    /**
     * Универсальный GET запрос на получение данных из другого сервиса
     *
     * @param requestUrl   url запроса
     * @param responseType тип возвращаемого объекта
     * @return объект данных
     */
    private <T> T executeGetRequest(String requestUrl, Class<T> responseType, ParameterizedTypeReference<T> typeReference) {
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<T> response;
            if (responseType != null) {
                response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, responseType);
            } else if (typeReference != null) {
                response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, typeReference);
            } else {
                throw new IllegalArgumentException("Не указан тип ответа");
            }

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                log.error("Ошибка запроса к {}: статус {}, тело ответа {}", requestUrl, response.getStatusCode(), response.getBody());
                throw new RuntimeException("Ошибка получения данных: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            log.error("Ошибка при вызове {}: {}", requestUrl, e.getMessage());
            throw new RuntimeException("Ошибка соединения с сервисом", e);
        }
    }

    /**
     * Универсальный POST запрос на отправку данных в другой сервис
     *
     * @param requestUrl   url запроса
     * @param requestBody  тело запроса
     * @param responseType тип возвращаемого объекта
     * @return объект данных
     */
    public <T, R> R simplePostRequest(String requestUrl, T requestBody, Class<R> responseType) {
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<T> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<R> response = restTemplate.exchange(requestUrl, HttpMethod.POST, entity, responseType);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("Ошибка POST запроса к {}: статус {}, тело ответа {}", requestUrl, response.getStatusCode(), response.getBody());
                throw new RuntimeException("Ошибка получения данных: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            log.error("Ошибка при вызове {}: {}", requestUrl, e.getMessage());
            throw new RuntimeException("Ошибка соединения с сервисом", e);
        }
    }


}
