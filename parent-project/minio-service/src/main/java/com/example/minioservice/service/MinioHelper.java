package com.example.minioservice.service;

import io.minio.*;
import io.minio.messages.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс MinioHelper предоставляет методы для работы с MinIO.
 */
@Component
public class MinioHelper {

    private static final Logger log = LoggerFactory.getLogger(MinioHelper.class);
    private final MinioClient minioClient;

    /**
     * Конструктор класса MinioHelper.
     *
     * @param minioClient клиент MinIO
     */
    public MinioHelper(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * Загружает файл в MinIO.
     *
     * @param bucket     имя бакета
     * @param file       файл для загрузки
     * @param objectName имя объекта в MinIO
     */
    public void upload(String bucket, MultipartFile file, String objectName) {
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            log.info("Файл загружен: bucket={}, objectName={}", bucket, objectName);
        } catch (Exception e) {
            log.error("Ошибка загрузки в MinIO: bucket={}, objectName={}", bucket, objectName, e);
            throw new RuntimeException("Ошибка загрузки файла в MinIO", e);
        }
    }

    /**
     * Получает файл из MinIO.
     *
     * @param bucket     имя бакета
     * @param objectName имя объекта в MinIO
     * @return поток данных файла
     */
    public InputStream getObject(String bucket, String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error("Ошибка получения файла из MinIO: bucket={}, objectName={}", bucket, objectName, e);
            throw new RuntimeException("Ошибка получения файла из MinIO", e);
        }
    }

    /**
     * Удаляет файл из MinIO.
     *
     * @param bucket     имя бакета
     * @param objectName имя объекта в MinIO
     */
    public void delete(String bucket, String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
            log.info("Файл удален: bucket={}, objectName={}", bucket, objectName);
        } catch (Exception e) {
            log.error("Ошибка удаления файла из MinIO: bucket={}, objectName={}", bucket, objectName, e);
            throw new RuntimeException("Ошибка удаления файла из MinIO", e);
        }
    }

    /**
     * Получает список файлов из MinIO.
     * (Не получает сами файлы, лишь сведения о них)
     *
     * @param bucket имя бакета
     * @param prefix префикс для фильтрации файлов
     * @return список имен файлов
     */
    public List<String> listObjects(String bucket, String prefix) {
        List<String> objectNames = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucket)
                            .prefix(prefix)
                            .build()
            );
            for (Result<Item> result : results) {
                objectNames.add(result.get().objectName());
            }
        } catch (Exception e) {
            log.error("Ошибка получения списка файлов из MinIO: bucket={}", bucket, e);
            throw new RuntimeException("Ошибка получения списка файлов из MinIO", e);
        }
        return objectNames;
    }
}
