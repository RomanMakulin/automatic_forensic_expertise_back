package com.example.adminservice.api.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * DTO: информация о файле эксперта
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FilesDto {

    /**
     * ID файла
     */
    private String id;

    /**
     * Имя файла
     */
    private String name;

    /**
     * Тип файла
     */
    private String type;

    /**
     * Путь к файлу
     */
    private String path;

    /**
     * Дата создания файла
     */
    @JsonProperty
    private String createdAt;

}
