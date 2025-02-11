package com.example.minioservice.service.impl;

import com.example.minioservice.service.FileDeleteService;
import com.example.minioservice.service.util.FileNameBuilder;
import com.example.minioservice.service.util.MinioHelper;
import com.example.minioservice.service.util.ValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Реализация интерфейса удаления файлов из minIO
 */
@Service
public class FileDeleteServiceImpl implements FileDeleteService {

    private static final Logger log = LoggerFactory.getLogger(FileDeleteServiceImpl.class);

    private final MinioHelper minioHelper;

    private final FileNameBuilder fileNameBuilder;

    private final ValidatorService validatorService;

    public FileDeleteServiceImpl(MinioHelper minioHelper, FileNameBuilder fileNameBuilder, ValidatorService validatorService) {
        this.minioHelper = minioHelper;
        this.fileNameBuilder = fileNameBuilder;
        this.validatorService = validatorService;
    }

    /**
     * Удаляет фотографию профиля.
     *
     * @param profileId идентификатор профиля
     */
    @Override
    public void deletePhoto(UUID profileId) {
        validatorService.validateId(profileId);
        minioHelper.delete(minioHelper.bucketAvatars, fileNameBuilder.buildAvatarObjectName(profileId));
    }

    /**
     * Удаляет шаблон профиля.
     *
     * @param profileId идентификатор профиля
     */
    @Override
    public void deleteTemplate(UUID profileId) {
        validatorService.validateId(profileId);
        minioHelper.delete(minioHelper.bucketTemplates, fileNameBuilder.buildTemplateObjectName(profileId));
    }

    /**
     * Удаляет файл.
     *
     * @param path путь к файлу
     */
    @Override
    public void deleteFile(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Путь к файлу не может быть пустым");
        }
        minioHelper.delete(minioHelper.bucketFiles, path);
    }

    /**
     * Удаляет список файлов.
     *
     * @param pathList список путей к файлам
     */
    @Override
    public void deleteFiles(List<String> pathList) {
        if (pathList == null || pathList.isEmpty()) {
            throw new IllegalArgumentException("Список путей к файлам не может быть пустым");
        }
        for (String path : pathList) {
            minioHelper.delete(minioHelper.bucketFiles, path);
        }
    }

    /**
     * Удаляет паспорт пользователя
     *
     * @param profileId идентификатор профиля
     */
    @Override
    public void deletePassport(UUID profileId) {
        validatorService.validateId(profileId);
        minioHelper.delete(minioHelper.bucketPassports, fileNameBuilder.buildFileObjectName(profileId));
    }

    /**
     * Удаляет диплом пользователя
     *
     * @param profileId идентификатор профиля
     */
    @Override
    public void deleteDiplom(UUID profileId) {
        validatorService.validateId(profileId);
        minioHelper.delete(minioHelper.bucketDiploms, fileNameBuilder.buildFileObjectName(profileId));
    }

}
