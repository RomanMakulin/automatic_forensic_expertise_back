package com.example.adminservice.api.dto.profile.original;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO: информация о файле эксперта
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OriginalFilesDto {

    /**
     * ID файла
     */
    private String id;

    /**
     * Путь к файлу
     */
    @NotNull(message = "Путь к файлу не может быть пустым")
    @JsonProperty("path")
    private String downloadUrl;

    /**
     * Дата создания файла
     */
    @NotNull(message = "Дата создания файла не может быть пустой")
    @JsonProperty("uploadDate")
    private String createdAt;

}
