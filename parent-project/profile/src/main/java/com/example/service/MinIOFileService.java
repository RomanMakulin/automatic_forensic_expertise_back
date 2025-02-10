package com.example.service;

import com.example.config.ApiPathsConfig;
import com.example.integration.IntegrationHelper;
import com.example.model.dto.FileDTO;
import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
public class MinIOFileService {

    private final RestTemplate restTemplate;

    private final ApiPathsConfig apiPathsConfig;

    private final IntegrationHelper integrationHelper;

    public MinIOFileService(RestTemplate restTemplate, ApiPathsConfig apiPathsConfig, IntegrationHelper integrationHelper) {
        this.restTemplate = restTemplate;
        this.apiPathsConfig = apiPathsConfig;
        this.integrationHelper = integrationHelper;
    }

    /**
     * Получить фото из minOP по идентификатору пользователя
     *
     * @param profileId идентификатор пользователя
     * @return ссылка на фото
     */
    public String getPhotoUrl(UUID profileId) {
        String url = "http://localhost:8030/api/files/get-photo" + "?profileId=" + profileId;

        // Создаем заголовки с токеном авторизации
        HttpHeaders headers = integrationHelper.createAuthHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Отправляем GET-запрос с заголовками
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Ошибка получения фото: " + response.getStatusCode());
        }

    }

    /**
     * Получить файлы из minOP по идентификатору пользователя
     *
     * @param profileId идентификатор пользователя
     * @return список ссылок на файлы
     */
    public List<String> getFiles(UUID profileId) {
        String url = "http://localhost:8030/api/files/get-files" + "?profileId=" + profileId;

        // Создаем заголовки с токеном авторизации
        HttpHeaders headers = integrationHelper.createAuthHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Отправляем GET-запрос с заголовками
        ResponseEntity<List<String>> response = restTemplate.exchange(
                url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<>() {
                } // Используется для обработки списка строк
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Ошибка получения файлов: " + response.getStatusCode());
        }
    }

    /**
     * Получить файл по пути (Название файла)
     *
     * @param path путь к файлу
     * @return ссылка на файл
     */
    public String getFile(String path) {
        String url = "http://localhost:8030/api/files/get-file" + "?path=" + path;

        // Создаем заголовки с токеном авторизации
        HttpHeaders headers = integrationHelper.createAuthHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Отправляем GET-запрос с заголовками
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody(); // Возвращает URL или содержимое файла
        } else {
            throw new RuntimeException("Ошибка получения файла: " + response.getStatusCode());
        }
    }

    /**
     * Сохраниить фото
     */
    @SneakyThrows
    public String savePhoto(UUID profileId, MultipartFile photo) {

        // Добавляем поддержку multipart/form-data
        restTemplate.setMessageConverters(List.of(
                new FormHttpMessageConverter(),
                new ByteArrayHttpMessageConverter()
        ));

        // Добавляем поддержку текстовых ответов
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));

        String url = "http://localhost:8030/api/files/upload-photo";
        HttpHeaders headers = integrationHelper.createAuthHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Формируем тело запроса
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("profileId", profileId.toString());  // 👈 Передаем как строку
        body.add("avatar", convertMultipartFileToResource(photo));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> photoUrl = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        return photoUrl.getBody();
    }

    /**
     * Сохраниить все файлы для профиля (фото, шаблон, файлы)
     */
    @SneakyThrows
    public List<FileDTO> saveAllFilesForProfile(UUID profileId,
                                                MultipartFile photo,
                                                MultipartFile passport,
                                                MultipartFile diplom,
                                                List<MultipartFile> files) {

        // Добавляем поддержку multipart/form-data
        restTemplate.setMessageConverters(List.of(
                new FormHttpMessageConverter(),
                new ByteArrayHttpMessageConverter()
        ));

        // Добавляем поддержку текстовых ответов
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        String url = "http://localhost:8030/api/files/upload-all";
        HttpHeaders headers = integrationHelper.createAuthHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Формируем тело запроса
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("profileId", profileId.toString());  // 👈 Передаем как строку
        body.add("avatar", convertMultipartFileToResource(photo));
        body.add("passport", convertMultipartFileToResource(passport));
        body.add("diplom", convertMultipartFileToResource(diplom));

        for (MultipartFile file : files) {
            body.add("files", convertMultipartFileToResource(file));
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<List<FileDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        System.out.println(response.getBody());

        return response.getBody();
    }

    private ByteArrayResource convertMultipartFileToResource(MultipartFile file) throws IOException {
        return new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
    }

}
