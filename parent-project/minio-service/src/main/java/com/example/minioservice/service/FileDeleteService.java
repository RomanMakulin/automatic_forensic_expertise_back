package com.example.minioservice.service;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс для удаления файлов.
 */
public interface FileDeleteService {

    /**
     * Удаляет фотографию для указанного профиля.
     *
     * @param profileId идентификатор профиля
     */
    void deletePhoto(UUID profileId);

    /**
     * Удаляет шаблон для указанного профиля.
     *
     * @param profileId идентификатор профиля
     */
    void deleteTemplate(UUID profileId);

    /**
     * Удаляет файл для указанного профиля.
     *
     * @param path путь к файлу
     */
    void deleteFile(String path);

    /**
     * Удаляет список файлов для указанного профиля.
     *
     * @param pathList список путей к файлам
     */
    void deleteFiles(List<String> pathList);

    /**
     * Удаляет паспорт для указанного профиля.
     *
     * @param profileId идентификатор профиля
     */
    void deletePassport(UUID profileId);

    /**
     * Удаляет диплом для указанного профиля.
     *
     * @param profileId идентификатор профиля
     */
    void deleteDiplom(UUID profileId);

}
