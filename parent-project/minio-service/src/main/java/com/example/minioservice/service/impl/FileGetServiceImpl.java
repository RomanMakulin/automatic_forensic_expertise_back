package com.example.minioservice.service.impl;

import com.example.minioservice.service.FileGetService;
import com.example.minioservice.service.util.FileNameBuilder;
import com.example.minioservice.service.util.MinioHelper;
import com.example.minioservice.service.util.ValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Реализация сервиса для получения файлов из minIO
 */
@Service
public class FileGetServiceImpl implements FileGetService {

    private static final Logger log = LoggerFactory.getLogger(FileGetServiceImpl.class);

    private final MinioHelper minioHelper;

    private final FileNameBuilder fileNameBuilder;

    private final ValidatorService validatorService;

    public FileGetServiceImpl(MinioHelper minioHelper,
                              FileNameBuilder fileNameBuilder,
                              ValidatorService validatorService) {
        this.minioHelper = minioHelper;
        this.fileNameBuilder = fileNameBuilder;
        this.validatorService = validatorService;
    }

    /**
     * Получает фотографию профиля.
     *
     * @param profileId идентификатор профиля
     * @return ресурс фотографии
     */
    @Override
    public String getPhoto(UUID profileId) {
        validatorService.validateId(profileId);
        return minioHelper.getObjectUrl(minioHelper.bucketAvatars, fileNameBuilder.buildAvatarObjectName(profileId));
    }

    /**
     * Получает паспорт пользователя (ссылка на документ)
     *
     * @param profileId идентификатор профиля
     * @return паспорт пользователя
     */
    @Override
    public String getPassport(UUID profileId) {
        validatorService.validateId(profileId);
        return minioHelper.getObjectUrl(minioHelper.bucketPassports, fileNameBuilder.buildFileObjectName(profileId));
    }

    /**
     * Получает диплом пользователя (ссылка на документ)
     *
     * @param profileId идентификатор профиля
     * @return диплом пользователя
     */
    @Override
    public String getDiplom(UUID profileId) {
        validatorService.validateId(profileId);
        return minioHelper.getObjectUrl(minioHelper.bucketDiploms, fileNameBuilder.buildFileObjectName(profileId));
    }

    /**
     * Получает шаблон профиля.
     *
     * @param profileId идентификатор профиля
     * @return ресурс шаблона
     */
    @Override
    public String getTemplate(UUID profileId) {
        validatorService.validateId(profileId);
        return minioHelper.getObjectUrl(minioHelper.bucketTemplates, fileNameBuilder.buildTemplateObjectName(profileId));
    }

    /**
     * Получает список файлов профиля.
     *
     * @param profileId идентификатор профиля
     * @return список ресурсов файлов
     */
    @Override
    public List<String> getFiles(UUID profileId) {
        validatorService.validateId(profileId);
        List<String> resources = new ArrayList<>();
        for (String objectName : minioHelper.listObjects(minioHelper.bucketFiles, profileId.toString())) {
            resources.add(minioHelper.getObjectUrl(minioHelper.bucketFiles, objectName));
        }
        return resources;
    }

    /**
     * Получает файл.
     *
     * @param path путь к файлу
     * @return ресурс файла (ссылка)
     */
    @Override
    public String getFile(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Путь к файлу не может быть пустым");
        }
        return minioHelper.getObjectUrl(minioHelper.bucketFiles, path);
    }

}
