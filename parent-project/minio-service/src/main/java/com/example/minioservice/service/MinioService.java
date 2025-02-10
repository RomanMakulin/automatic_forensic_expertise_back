package com.example.minioservice.service;

import com.example.minioservice.dto.FileDto;
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

    /**
     * Возвращает ссылку на аватар для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @return ресурс фотографии
     */
    String getPhoto(UUID profileId);

    /**
     * Возвращает ссылку на паспорт для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @return ресурс паспорта
     */
    String getPassport(UUID profileId);

    /**
     * Возвращает ссылку на диплом для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @return ресурс диплома
     */
    String getDiplom(UUID profileId);

    /**
     * Возвращает ссылки на файлы для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @return список ресурсов файлов
     */
    List<String> getFiles(UUID profileId);

    /**
     * Возвращает ссылку на файл для указанного профиля.
     *
     * @param path путь к файлу
     * @return ресурс файла
     */
    String getFile(String path);

    /**
     * Возвращает ссылку на шаблон для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @return ресурс шаблона
     */
    String getTemplate(UUID profileId);

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
