package com.example.minioservice.service.util;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Сервис валидации данных
 */
@Service
public class ValidatorService {

    /**
     * Валидация базовых аргументов
     *
     * @param profileId идентификатор профиля
     * @param file      файл
     */
    public void validateIdAndFile(UUID profileId, MultipartFile file) {
        if (profileId == null || file == null) {
            throw new IllegalArgumentException("Некорректные параметры");
        }
    }

    /**
     * Валидация идентификатора
     *
     * @param profileId идентификатор профиля
     */
    public void validateId(UUID profileId) {
        if (profileId == null) {
            throw new IllegalArgumentException("Некорректные параметры");
        }
    }

}
