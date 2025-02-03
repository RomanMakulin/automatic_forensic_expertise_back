package com.example.minioservice.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы с файлами в Minio.
 */
public interface MinioService {

    /**
     * Загружает все файлы, включая аватар и шаблон, для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param avatar аватар пользователя
     * @param template шаблон файла
     * @param files список файлов для загрузки
     * @return список идентификаторов загруженных файлов
     */
    List<UUID> uploadAllFiles(UUID profileId, MultipartFile avatar, MultipartFile template, List<MultipartFile> files);

    /**
     * Загружает фотографию для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param avatar фотография пользователя
     */
    void uploadPhoto(UUID profileId, MultipartFile avatar);

    /**
     * Загружает файл для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param file файл для загрузки
     * @return идентификатор загруженного файла
     */
    UUID uploadFile(UUID profileId, MultipartFile file);

    /**
     * Загружает шаблон для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param template шаблон файла
     */
    void uploadTemplate(UUID profileId, MultipartFile template);

    /**
     * Загружает список файлов для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param files список файлов для загрузки
     * @return список идентификаторов загруженных файлов
     */
    List<UUID> uploadFiles(UUID profileId, List<MultipartFile> files);

    /**
     * Возвращает фотографию для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @return ресурс фотографии
     */
    Resource getPhoto(UUID profileId);

    /**
     * Возвращает список файлов для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @return список ресурсов файлов
     */
    List<Resource> getFiles(UUID profileId);

    /**
     * Возвращает шаблон для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @return ресурс шаблона
     */
    Resource getTemplate(UUID profileId);

    /**
     *  Удаляет фотографию для указанного профиля.
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
     * @param profileId идентификатор профиля
     * @param fileId идентификатор файла
     */
    void deleteFile(UUID profileId, UUID fileId);

    /**
     * Удаляет список файлов для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param fileIds список идентификаторов файлов
     */
    void deleteFiles(UUID profileId, List<UUID> fileIds);

}
