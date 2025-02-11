package com.example.minioservice.service;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс для работы с файлами - получение файлов
 */
public interface FileGetService {

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

}
