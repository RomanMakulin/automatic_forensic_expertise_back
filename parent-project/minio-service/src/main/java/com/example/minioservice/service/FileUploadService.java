package com.example.minioservice.service;

import com.example.minioservice.api.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для загрузки файлов.
 */
public interface FileUploadService {

    /**
     * Загружает все файлы, включая аватар и шаблон, для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param avatar    аватар пользователя
     * @param passport  паспорт
     * @param diplom    диплом
     * @param template  шаблон файла
     * @param files     список файлов для загрузки
     * @return список идентификаторов загруженных файлов
     */
    List<FileDto> uploadAllFiles(UUID profileId, MultipartFile avatar, MultipartFile passport, MultipartFile diplom, List<MultipartFile> files);

    /**
     * Загружает фотографию для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param avatar    фотография пользователя
     */
    void uploadPhoto(UUID profileId, MultipartFile avatar);

    /**
     * Загружает паспорт для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param passport  паспорт пользователя
     */
    void uploadPassport(UUID profileId, MultipartFile passport);

    /**
     * Загружает диплом для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param diplom    диплом пользователя
     */
    void uploadDiplom(UUID profileId, MultipartFile diplom);

    /**
     * Загружает файл для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param file      файл для загрузки
     * @return идентификатор загруженного файла
     */
    FileDto uploadFile(UUID profileId, MultipartFile file);

    /**
     * Загружает шаблон для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param template  шаблон файла
     */
    void uploadTemplate(UUID profileId, MultipartFile template);

    /**
     * Загружает список файлов для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param files     список файлов для загрузки
     * @return список идентификаторов загруженных файлов
     */
    List<FileDto> uploadFiles(UUID profileId, List<MultipartFile> files);

}
