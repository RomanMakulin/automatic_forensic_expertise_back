package com.example.minioservice.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Класс для валидации файлов перед загрузкой в MinIO
 */
@Component
public class FileValidator {

    /**
     * Валидация одного файла
     *
     * @param file файл
     */
    public void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Файл не может быть null или пустым");
        }
    }

    /**
     * Валидация наличия идентификатора профиля
     *
     * @param profileId идентификатор профиля
     */
    public void validateProfileId(UUID profileId) {
        if (profileId == null) {
            throw new IllegalArgumentException("Profile ID не может быть null");
        }
    }

    /**
     * Валидация файла и профиля
     *
     * @param profileId идентификатор профиля
     * @param file      файл
     */
    public void validateFile(UUID profileId, MultipartFile file) {
        validateProfileId(profileId);
        validateFile(file);
    }

    /**
     * Валидация двух файлов (например, аватар и шаблон)
     *
     * @param profileId идентификатор профиля
     * @param file      первый файл
     * @param template  второй файл
     */
    public void validateFiles(UUID profileId, MultipartFile file, MultipartFile template) {
        validateProfileId(profileId);
        validateFile(file);
        validateFile(template);
    }

    /**
     * Валидация списка файлов
     *
     * @param profileId идентификатор профиля
     * @param fileList  список файлов
     */
    public void validateFileList(UUID profileId, List<MultipartFile> fileList) {
        validateProfileId(profileId);
        if (fileList == null || fileList.isEmpty()) {
            throw new IllegalArgumentException("Список файлов не может быть null или пустым");
        }
        for (MultipartFile file : fileList) {
            validateFile(file);
        }
    }

    /**
     * Валидация трех файлов (аватар, шаблон и список файлов)
     *
     * @param profileId идентификатор профиля
     * @param file      первый файл (аватар)
     * @param template  второй файл (шаблон)
     * @param fileList  список файлов
     */
    public void validateFiles(UUID profileId, MultipartFile file, MultipartFile template, List<MultipartFile> fileList) {
        validateFiles(profileId, file, template);  // Проверяет аватар и шаблон
        validateFileList(profileId, fileList);    // Проверяет список файлов
    }
}
