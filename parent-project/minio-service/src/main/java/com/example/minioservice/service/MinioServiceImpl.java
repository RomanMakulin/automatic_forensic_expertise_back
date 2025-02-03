package com.example.minioservice.service;

import com.example.minioservice.dto.FileDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с файлами в MinIO.
 */
@Service
public class MinioServiceImpl implements MinioService {

    private static final Logger log = LoggerFactory.getLogger(MinioServiceImpl.class);

    private final MinioHelper minioHelper;
    private final FileValidator fileValidator;
    private final FileNameBuilder fileNameBuilder;

    @Value("${minio.buckets.avatars}")
    private String bucketAvatars;

    @Value("${minio.buckets.files}")
    private String bucketFiles;

    @Value("${minio.buckets.templates}")
    private String bucketTemplates;

    public MinioServiceImpl(MinioHelper minioHelper,
                            FileValidator fileValidator,
                            FileNameBuilder fileNameBuilder) {
        this.minioHelper = minioHelper;
        this.fileValidator = fileValidator;
        this.fileNameBuilder = fileNameBuilder;
    }

    /**
     * Загружает все файлы для профиля.
     *
     * @param profileId идентификатор профиля
     * @param avatar    файл аватара
     * @param template  файл шаблона
     * @param files     список файлов
     * @return список DTO файлов
     */
    @Override
    public List<FileDto> uploadAllFiles(UUID profileId, MultipartFile avatar, MultipartFile template, List<MultipartFile> files) {
        log.info("Загрузка всех файлов для profileId: {}", profileId);

        fileValidator.validateFiles(profileId, avatar, template, files);

        minioHelper.upload(bucketAvatars, avatar, fileNameBuilder.buildAvatarObjectName(profileId));
        minioHelper.upload(bucketTemplates, template, fileNameBuilder.buildTemplateObjectName(profileId));

        return uploadFiles(profileId, files);
    }

    /**
     * Загружает фотографию профиля.
     *
     * @param profileId идентификатор профиля
     * @param avatar    файл аватара
     */
    @Override
    public void uploadPhoto(UUID profileId, MultipartFile avatar) {
        fileValidator.validateFile(profileId, avatar);



        minioHelper.upload(bucketAvatars, avatar, fileNameBuilder.buildAvatarObjectName(profileId));
    }

    /**
     * Загружает шаблон профиля.
     *
     * @param profileId идентификатор профиля
     * @param template  файл шаблона
     */
    @Override
    public void uploadTemplate(UUID profileId, MultipartFile template) {
        fileValidator.validateFile(profileId, template);
        minioHelper.upload(bucketTemplates, template, fileNameBuilder.buildTemplateObjectName(profileId));
    }

    /**
     * Загружает файл.
     *
     * @param profileId идентификатор профиля
     * @param file      файл
     * @return DTO файла
     */
    @Override
    public FileDto uploadFile(UUID profileId, MultipartFile file) {
        fileValidator.validateFile(profileId, file);

        UUID fileId = UUID.randomUUID();
        String filePath = fileNameBuilder.buildFileObjectName(profileId, fileId);

        minioHelper.upload(bucketFiles, file, filePath);

        return new FileDto(fileId, filePath, LocalDateTime.now());
    }

    /**
     * Загружает список файлов.
     *
     * @param profileId идентификатор профиля
     * @param files     список файлов
     * @return список DTO файлов
     */
    @Override
    public List<FileDto> uploadFiles(UUID profileId, List<MultipartFile> files) {
        fileValidator.validateFileList(profileId, files);

        List<FileDto> fileDtos = new ArrayList<>();
        for (MultipartFile file : files) {
            fileDtos.add(uploadFile(profileId, file));
        }
        return fileDtos;
    }

    /**
     * Получает фотографию профиля.
     *
     * @param profileId идентификатор профиля
     * @return ресурс фотографии
     */
    @Override
    public Resource getPhoto(UUID profileId) {
        return new InputStreamResource(minioHelper.getObject(bucketAvatars, fileNameBuilder.buildAvatarObjectName(profileId)));
    }

    /**
     * Получает шаблон профиля.
     *
     * @param profileId идентификатор профиля
     * @return ресурс шаблона
     */
    @Override
    public Resource getTemplate(UUID profileId) {
        return new InputStreamResource(minioHelper.getObject(bucketTemplates, fileNameBuilder.buildTemplateObjectName(profileId)));
    }

    /**
     * Получает список файлов профиля.
     *
     * @param profileId идентификатор профиля
     * @return список ресурсов файлов
     */
    @Override
    public List<Resource> getFiles(UUID profileId) {
        List<Resource> resources = new ArrayList<>();
        for (String objectName : minioHelper.listObjects(bucketFiles, profileId.toString())) {
            resources.add(new InputStreamResource(minioHelper.getObject(bucketFiles, objectName)));
        }
        return resources;
    }

    /**
     * Удаляет фотографию профиля.
     *
     * @param profileId идентификатор профиля
     */
    @Override
    public void deletePhoto(UUID profileId) {
        fileValidator.validateProfileId(profileId);
        minioHelper.delete(bucketAvatars, fileNameBuilder.buildAvatarObjectName(profileId));
    }

    /**
     * Удаляет шаблон профиля.
     *
     * @param profileId идентификатор профиля
     */
    @Override
    public void deleteTemplate(UUID profileId) {
        fileValidator.validateProfileId(profileId);
        minioHelper.delete(bucketTemplates, fileNameBuilder.buildTemplateObjectName(profileId));
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
        minioHelper.delete(bucketFiles, path);
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
            minioHelper.delete(bucketFiles, path);
        }
    }
}


