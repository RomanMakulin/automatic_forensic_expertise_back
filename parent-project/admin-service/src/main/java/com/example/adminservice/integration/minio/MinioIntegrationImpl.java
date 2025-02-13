package com.example.adminservice.integration.minio;

import com.example.adminservice.config.AppConfig;
import com.example.adminservice.integration.IntegrationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(MinioIntegrationImpl.class);
    private final IntegrationHelper integrationHelper;
    private final AppConfig appConfig;

    public MinioIntegrationImpl(IntegrationHelper integrationHelper, AppConfig appConfig) {
        this.integrationHelper = integrationHelper;
        this.appConfig = appConfig;
    }

    /**
     * Запрос на получение файла из minIO
     *
     * @param endpoint путь к файлу
     * @return ссылка на файл
     */
    @Override
    public String getFile(String endpoint, Map<String, String> params) {
        String baseUrl = appConfig.getPaths().getMinio().get(endpoint);
        String requestUrl = integrationHelper.urlBuilder(baseUrl, params);

        return integrationHelper.sendRequest(
                requestUrl,
                HttpMethod.GET,
                null,
                String.class);
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

        return integrationHelper.sendRequest(
                requestUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {
                });
    }

    /**
     * Запрос на удаление файла из minIO
     *
     * @param endpoint путь к файлу
     */
    @Override
    public void deleteFile(String endpoint, Map<String, String> params) {
        String baseUrl = appConfig.getPaths().getMinio().get(endpoint);
        String requestUrl = integrationHelper.urlBuilder(baseUrl, params);

        integrationHelper.sendRequest(
                requestUrl,
                HttpMethod.POST,
                null,
                Void.class);
    }

    @Override
    public void deleteFiles(String endpoint, List<String> pathList) {
        String baseUrl = appConfig.getPaths().getMinio().get(endpoint);

        integrationHelper.sendRequest(
                baseUrl,
                HttpMethod.POST,
                pathList,
                Void.class);
    }

}
