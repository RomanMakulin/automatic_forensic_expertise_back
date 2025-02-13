package com.example.model;

import com.example.model.dto.DirectionDTO;
import com.example.model.dto.FileDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * DTO с фронта для отклонения верификации профиля с неподходящими данными
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfileCancel {

    /**
     * id профиля
     */
    @JsonProperty("profile_id")
    @NotNull(message = "profileId is required")
    private UUID profileId;

    /**
     * Направления работы. Приходит с фронта (если хотя бы один элемент есть - его нужно удалить из БД)
     * Содержит ID
     */
    @NotNull(message = "directions is required")
    private List<String> directions;

    /**
     * Файлы. Приходит с фронта (если хотя бы один элемент есть - его нужно удалить из БД)
     * Содержит ID
     */
    @NotNull(message = "files is required")
    private List<String> files;

}
