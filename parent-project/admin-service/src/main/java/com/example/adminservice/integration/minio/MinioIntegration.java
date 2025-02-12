package com.example.adminservice.integration.minio;

import com.example.adminservice.api.dto.profile.FilesDto;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс для работы с Minio
 */
public interface MinioIntegration {

    /**
     * Получение файла по идентификатору профиля
     *
     * @param endpoint  путь к файлу
     * @return ссылка на файл
     */
    String getFile(String endpoint, Map<String, String> params);

    /**
     * Получение всех файлов профиля
     *
     * @param profileId id профиля
     * @return список ссылок на файлы
     */
    List<String> getFiles(String profileId);

}
