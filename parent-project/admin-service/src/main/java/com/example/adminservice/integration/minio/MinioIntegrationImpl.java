package com.example.adminservice.integration.minio;

import com.example.adminservice.config.AppConfig;
import com.example.adminservice.integration.IntegrationHelper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Реализация сервиса для работы с Minio
 */
@Service
public class MinioIntegrationImpl implements MinioIntegration {

    private final IntegrationHelper integrationHelper;
    private final AppConfig appConfig;
    private final RestTemplate restTemplate;

    public MinioIntegrationImpl(IntegrationHelper integrationHelper,
                                AppConfig appConfig, RestTemplate restTemplate) {
        this.integrationHelper = integrationHelper;
        this.appConfig = appConfig;
        this.restTemplate = restTemplate;
    }

    /**
     * Запрос на получение файла из minIO
     *
     * @param endpoint  путь к файлу
     * @return ссылка на файл
     */
    @Override
    public String getFile(String endpoint, Map<String, String> params) {
        String baseUrl = appConfig.getPaths().getMinio().get(endpoint);
        String requestUrl = integrationHelper.urlBuilder(baseUrl, params);

        return integrationHelper.simpleGetRequest(requestUrl, String.class);
    }

    /**
     * Запрос на получение списка файлов из minIO
     *
     * @param profileId идентификатор профиля
     * @return список ссылок на файлы
     */
    @Override
    public List<String> getFiles(String profileId) {
        String baseUrl = appConfig.getPaths().getMinio().get("get-files");
        String requestUrl = integrationHelper.urlBuilder(baseUrl, Map.of("profileId", profileId));

        ResponseEntity<List<String>> response = restTemplate.exchange(
                requestUrl,
                HttpMethod.GET,
                new HttpEntity<>(integrationHelper.createAuthHeaders()),
                new ParameterizedTypeReference<>() {
                }
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Ошибка получения списка файлов: " + response.getStatusCode());
        }
    }

}
