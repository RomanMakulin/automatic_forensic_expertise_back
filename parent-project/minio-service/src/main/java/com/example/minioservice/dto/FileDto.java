package com.example.minioservice.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO для объекта файл
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {

    /**
     * Идентификатор файла
     */
    private UUID id;

    /**
     * Имя файла
     */
    private String path;

    /**
     * Дата создания файла
     */
    private LocalDateTime cratedAt;

}
