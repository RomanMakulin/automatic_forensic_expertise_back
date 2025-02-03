package com.example.minioservice.service;

import io.minio.*;
import io.minio.messages.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Сервис для работы с файлами в MinIO.
 */
@Service
public class MinioServiceImpl implements MinioService {

    private static final Logger log = LoggerFactory.getLogger(MinioServiceImpl.class);
    private final MinioClient minioClient;

    @Value("${minio.buckets.avatars}")
    private String bucketAvatars;

    @Value("${minio.buckets.files}")
    private String bucketFiles;

    @Value("${minio.buckets.templates}")
    private String bucketTemplates;

    public MinioServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * Загружает все файлы, включая аватар и шаблон, для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param avatar    аватар пользователя
     * @param template  шаблон файла
     * @param files     список файлов для загрузки
     * @return список идентификаторов загруженных файлов
     */
    @Override
    public List<UUID> uploadAllFiles(UUID profileId, MultipartFile avatar, MultipartFile template, List<MultipartFile> files) {

        log.info("Загрузка файлов в MinIO, profileId: {}", profileId);

        // Проверка на null
        if (avatar == null || files == null || files.isEmpty() || template == null || template.isEmpty() || profileId == null) {
            throw new IllegalArgumentException("Ошибка загрузки файлов в MinIO (Не все файлы загружены)");
        }

        // Генерируем список UUID для файлов пользователя
        List<UUID> ids = generateUUIDs(files.size());

        uploadProcess(bucketAvatars, avatar, buildAvatarObjectName(profileId));
        uploadProcess(bucketTemplates, template, buildTemplateObjectName(profileId));


        for (int i = 0; i < files.size(); i++) {
            uploadProcess(bucketFiles, files.get(i), buildFileObjectName(profileId, ids.get(i)));
        }

        return ids;
    }

    /**
     * Загружает фотографию для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param avatar    фотография пользователя
     */
    @Override
    public void uploadPhoto(UUID profileId, MultipartFile avatar) {
        if (avatar == null || avatar.isEmpty() || profileId == null) {
            throw new IllegalArgumentException("Ошибка загрузки фото в MinIO (Не все файлы загружены)");
        }
        uploadProcess(bucketAvatars, avatar, buildAvatarObjectName(profileId));
    }

    /**
     * Загружает файл для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param file      файл для загрузки
     * @return идентификатор загруженного файла
     */
    @Override
    public UUID uploadFile(UUID profileId, MultipartFile file) {
        if (file == null || file.isEmpty() || profileId == null) {
            throw new IllegalArgumentException("Ошибка загрузки файла в MinIO (Не все файлы загружены)");
        }

        UUID id = UUID.randomUUID();
        uploadProcess(bucketFiles, file, buildFileObjectName(profileId, id));
        return id;
    }

    @Override
    public void uploadTemplate(UUID profileId, MultipartFile template) {
        if (template == null || template.isEmpty() || profileId == null) {
            throw new IllegalArgumentException("Ошибка загрузки шаблона в MinIO (Не все файлы загружены)");
        }
        uploadProcess(bucketTemplates, template, buildTemplateObjectName(profileId));
    }

    /**
     * Загружает список файлов для указанного профиля.
     *
     * @param profileId идентификатор профиля
     * @param files     список файлов для загрузки
     * @return список идентификаторов загруженных файлов
     */
    @Override
    public List<UUID> uploadFiles(UUID profileId, List<MultipartFile> files) {
        if (files == null || files.isEmpty() || profileId == null) {
            throw new IllegalArgumentException("Ошибка загрузки файлов в MinIO (Не все файлы загружены)");
        }
        List<UUID> ids = generateUUIDs(files.size());

        for (int i = 0; i < files.size(); i++) {
            uploadProcess(bucketFiles, files.get(i), buildFileObjectName(profileId, ids.get(i)));
        }
        return ids;
    }

    /**
     * Генерирует список из UUID заданного размера
     *
     * @param size размер списка
     * @return список UUID
     */
    private List<UUID> generateUUIDs(int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> UUID.randomUUID())
                .collect(Collectors.toList());
    }

    /**
     * Процесс и логика загрузки конкретного файла в MinIO
     *
     * @param bucketName название бакета
     * @param file       файл
     * @param objectName название файла
     */
    private void uploadProcess(String bucketName, MultipartFile file, String objectName) {
        try {
            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
                log.info("Файл успешно загружен в MinIO, bucketName: {}, objectName: {}", bucketName, objectName);
            }
        } catch (Exception e) {
            log.error("Ошибка загрузки файла в MinIO, bucketName: {}, objectName: {}", bucketName, objectName);
            throw new RuntimeException("Ошибка загрузки файла в MinIO", e);
        }
    }

    /**
     * Получает фото из MinIO.
     *
     * @param profileId идентификатор профиля
     * @return ресурс фотографии
     */
    public Resource getPhoto(UUID profileId) {
        String objectName = buildAvatarObjectName(profileId);
        InputStream inputStream = getObjectProcess(bucketAvatars, objectName);
        return new InputStreamResource(inputStream);
    }

    /**
     * Получает список файлов из MinIO по конкретному профилю
     *
     * @param profileId UUID профиля
     * @return список файлов из MinIO
     */
    public List<Resource> getFiles(UUID profileId) {
        List<Resource> resources = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketFiles)
                            .prefix(profileId + "_")
                            .build()
            );

            for (Result<Item> result : results) {
                Item item = result.get();
                InputStream is = getObjectProcess(bucketFiles, item.objectName());
                resources.add(new InputStreamResource(is));
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении списка файлов из MinIO", e);
        }
        return resources;
    }

    /**
     * Получает шаблон из MinIO.
     *
     * @param profileId идентификатор профиля
     * @return ресурс шаблона
     */
    @Override
    public Resource getTemplate(UUID profileId) {
        String objectName = buildTemplateObjectName(profileId);
        InputStream inputStream = getObjectProcess(bucketTemplates, objectName);
        return new InputStreamResource(inputStream);
    }

    /**
     * Общая реализация получения файла из MinIO.
     *
     * @param bucket     название бакета
     * @param objectName название файла
     * @return файл из MinIO
     */
    private InputStream getObjectProcess(String bucket, String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении файла из MinIO", e);
        }
    }

    /**
     * Удаляет фото из MinIO по конкретному профилю.
     *
     * @param profileId идентификатор профиля
     */
    @Override
    public void deletePhoto(UUID profileId) {
        if (profileId == null) {
            throw new IllegalArgumentException("ProfileId не может быть null");
        }
        String objectName = buildAvatarObjectName(profileId);
        deleteObject(bucketAvatars, objectName);
    }

    /**
     * Удаляет шаблон из MinIO по конкретному профилю.
     *
     * @param profileId идентификатор профиля
     */
    @Override
    public void deleteTemplate(UUID profileId) {
        if (profileId == null) {
            throw new IllegalArgumentException("ProfileId не может быть null");
        }
        String objectName = buildTemplateObjectName(profileId);
        deleteObject(bucketTemplates, objectName);
    }

    /**
     * Удаляет файл из MinIO по конкретному профилю.
     *
     * @param profileId идентификатор профиля
     * @param fileId    идентификатор файла
     */
    @Override
    public void deleteFile(UUID profileId, UUID fileId) {
        if (profileId == null || fileId == null) {
            throw new IllegalArgumentException("Параметры не могут быть null");
        }
        String objectName = buildFileObjectName(profileId, fileId);
        deleteObject(bucketFiles, objectName);
    }

    /**
     * Удаляет файлы из MinIO по конкретному профилю.
     *
     * @param profileId UUID профиля
     * @param fileIds   список UUID файлов
     */
    @Override
    public void deleteFiles(UUID profileId, List<UUID> fileIds) {
        if (profileId == null || fileIds == null || fileIds.isEmpty()) {
            throw new IllegalArgumentException("Параметры не могут быть null или пустыми");
        }
        for (UUID fileId : fileIds) {
            String objectName = buildFileObjectName(profileId, fileId);
            deleteObject(bucketFiles, objectName);
        }
    }

    /**
     * Общая реализация удаления объекта из MinIO.
     *
     * @param bucket     название бакета
     * @param objectName имя удаляемого объекта
     */
    private void deleteObject(String bucket, String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
            log.info("Объект успешно удален: bucket={}, objectName={}", bucket, objectName);
        } catch (Exception e) {
            log.error("Ошибка удаления объекта из MinIO: bucket={}, objectName={}", bucket, objectName, e);
            throw new RuntimeException("Ошибка удаления объекта из MinIO", e);
        }
    }

    /**
     * Формирует имя файла для аватара.
     *
     * @param profileId идентификатор профиля
     * @return имя файла для аватара
     */
    private String buildAvatarObjectName(UUID profileId) {
        return profileId + ".jpg";
    }

    /**
     * Формирует имя файла для шаблона.
     *
     * @param profileId идентификатор профиля
     * @return имя файла для шаблона
     */
    private String buildTemplateObjectName(UUID profileId) {
        return profileId + ".odt";
    }

    /**
     * Формирует имя файла для файла.
     *
     * @param profileId идентификатор профиля
     * @param fileId    идентификатор файла
     * @return имя файла для файла
     */
    private String buildFileObjectName(UUID profileId, UUID fileId) {
        return profileId + "_" + fileId + ".pdf";
    }


}
