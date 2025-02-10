package com.example.minioservice.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Класс FileNameBuilder предоставляет методы для построения имен файлов.
 */
@Component
public class FileNameBuilder {

    /**
     * Построение имени файла аватара.
     *
     * @param profileId идентификатор профиля
     * @return имя файла аватара
     */
    public String buildAvatarObjectName(UUID profileId) {
        return profileId + ".jpg";
    }

    /**
     * Построение имени файла шаблона.
     *
     * @param profileId идентификатор профиля
     * @return имя файла шаблона
     */
    public String buildTemplateObjectName(UUID profileId) {
        return profileId + ".odt";
    }

    /**
     * Построение имени файла.
     *
     * @param profileId идентификатор профиля
     * @param fileId    идентификатор файла
     * @return имя файла
     */
    public String buildFileObjectName(UUID profileId, UUID fileId) {
        return profileId + "_" + fileId + ".pdf";
    }

    /**
     * Построение имени файла
     *
     * @param profileId идентификатор профиля
     * @return имя файла
     */
    public String buildFileObjectName(UUID profileId) {
        return profileId + ".pdf";
    }

}
