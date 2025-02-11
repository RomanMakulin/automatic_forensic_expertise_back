package com.example.minioservice.service.impl;

import com.example.minioservice.api.dto.FileDto;
import com.example.minioservice.service.util.FileNameBuilder;
import com.example.minioservice.service.FileUploadService;
import com.example.minioservice.service.util.MinioHelper;
import com.example.minioservice.service.util.ValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Реализация сервиса для загрузки файлов в minIO
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger log = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    private final MinioHelper minioHelper;

    private final FileNameBuilder fileNameBuilder;

    private final ValidatorService validatorService;


    public FileUploadServiceImpl(MinioHelper minioHelper,
                                 FileNameBuilder fileNameBuilder,
                                 ValidatorService validatorService) {
        this.minioHelper = minioHelper;
        this.fileNameBuilder = fileNameBuilder;
        this.validatorService = validatorService;
    }

    /**
     * Загружает все файлы для профиля.
     *
     * @param profileId идентификатор профиля
     * @param avatar    файл аватара
     * @param passport  паспорт
     * @param diplom    диплом
//     * @param template  файл шаблона
     * @param files     список файлов
     * @return список DTO файлов
     */
    @Override
    public List<FileDto> uploadAllFiles(UUID profileId, MultipartFile avatar, MultipartFile passport, MultipartFile diplom, List<MultipartFile> files) {
        log.info("Загрузка всех файлов для profileId: {}", profileId);

        if (profileId == null || avatar == null || files == null || files.isEmpty()) {
            throw new IllegalArgumentException("Некорректные параметры");
        }

        minioHelper.upload(minioHelper.bucketAvatars, avatar, fileNameBuilder.buildAvatarObjectName(profileId));
        minioHelper.upload(minioHelper.bucketPassports, passport, fileNameBuilder.buildFileObjectName(profileId));
        minioHelper.upload(minioHelper.bucketDiploms, diplom, fileNameBuilder.buildFileObjectName(profileId));
//        minioHelper.upload(bucketTemplates, template, fileNameBuilder.buildTemplateObjectName(profileId));
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
        validatorService.validateIdAndFile(profileId, avatar);
        minioHelper.upload(minioHelper.bucketAvatars, avatar, fileNameBuilder.buildAvatarObjectName(profileId));
    }

    /**
     * Загружает паспорт для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param passport  паспорт пользователя
     */
    @Override
    public void uploadPassport(UUID profileId, MultipartFile passport) {
        validatorService.validateIdAndFile(profileId, passport);
        minioHelper.upload(minioHelper.bucketPassports, passport, fileNameBuilder.buildFileObjectName(profileId));
    }

    /**
     * Загружает диплом для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param diplom    диплом пользователя
     */
    @Override
    public void uploadDiplom(UUID profileId, MultipartFile diplom) {
        validatorService.validateIdAndFile(profileId, diplom);
        minioHelper.upload(minioHelper.bucketDiploms, diplom, fileNameBuilder.buildFileObjectName(profileId));
    }

    /**
     * Загружает шаблон профиля.
     *
     * @param profileId идентификатор профиля
     * @param template  файл шаблона
     */
    @Override
    public void uploadTemplate(UUID profileId, MultipartFile template) {
        validatorService.validateIdAndFile(profileId, template);
        minioHelper.upload(minioHelper.bucketTemplates, template, fileNameBuilder.buildTemplateObjectName(profileId));
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
        validatorService.validateIdAndFile(profileId, file);

        UUID fileId = UUID.randomUUID();
        String filePath = fileNameBuilder.buildFileObjectName(profileId, fileId);

        minioHelper.upload(minioHelper.bucketFiles, file, filePath);

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
        if (profileId == null || files == null || files.isEmpty()) {
            throw new IllegalArgumentException("Некорректные параметры");
        }

        List<FileDto> fileDtos = new ArrayList<>();

        for (MultipartFile file : files) {
            fileDtos.add(uploadFile(profileId, file));
        }
        return fileDtos;
    }

}
