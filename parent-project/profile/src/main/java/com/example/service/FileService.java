package com.example.service;

import com.example.config.ApiPathsConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    public String savePhoto() {

        //todo

        return null;
    }


}
