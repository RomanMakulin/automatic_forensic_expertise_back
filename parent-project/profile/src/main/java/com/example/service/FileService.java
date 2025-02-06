package com.example.service;

import com.example.config.ApiPathsConfig;
import com.example.model.dto.FileDTO;
import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    private final RestTemplate restTemplate;

    private final ApiPathsConfig apiPathsConfig;

    public FileService(RestTemplate restTemplate, ApiPathsConfig apiPathsConfig) {
        this.restTemplate = restTemplate;
        this.apiPathsConfig = apiPathsConfig;
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
        HttpHeaders headers = createAuthHeaders();
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
                new ParameterizedTypeReference<String>() {}
        );

        return photoUrl.getBody();
    }

    /**
     * Сохраниить все файлы для профиля (фото, шаблон, файлы)
     */
    @SneakyThrows
    public List<FileDTO> savePhotoTemplateFiles(UUID profileId, MultipartFile photo, MultipartFile template, List<MultipartFile> files) {

        // Добавляем поддержку multipart/form-data
        restTemplate.setMessageConverters(List.of(
                new FormHttpMessageConverter(),
                new ByteArrayHttpMessageConverter()
        ));

        // Добавляем поддержку текстовых ответов
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        String url = "http://localhost:8030/api/files/upload-all";
        HttpHeaders headers = createAuthHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Формируем тело запроса
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("profileId", profileId.toString());  // 👈 Передаем как строку
        body.add("avatar", convertMultipartFileToResource(photo));
        body.add("template", convertMultipartFileToResource(template));

        for (MultipartFile file : files) {
            body.add("files", convertMultipartFileToResource(file));
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<List<FileDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
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

    /**
     * Создание HTTP-заголовков с Bearer-токеном для авторизации
     */
    private HttpHeaders createAuthHeaders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalStateException("Невозможно получить JWT из SecurityContext");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt.getTokenValue());
        return headers;
    }

}
