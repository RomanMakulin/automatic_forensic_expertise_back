package com.example.model.newDto.profile;

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
public class FilesDto {

    /**
     * ID файла
     */
    private String id;

    /**
     * Путь к файлу
     */
    @NotNull(message = "Путь к файлу не может быть пустым")
    private String path;

    /**
     * Дата создания файла
     */
    @JsonProperty("created_at")
    @NotNull(message = "Дата создания файла не может быть пустой")
    private String createdAt;

}
