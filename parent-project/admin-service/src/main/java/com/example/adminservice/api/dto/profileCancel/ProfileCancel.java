package com.example.adminservice.api.dto.profileCancel;

import com.example.adminservice.api.dto.profile.DirectionDto;
import com.example.adminservice.api.dto.profile.FilesDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

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
    private String profileId;

    /**
     * Если приходит с фронта true - удаляем из профиля паспорт
     */
    private Boolean needPassportDelete;

    /**
     * Если приходит с фронта true - удаляем из профиля диплом
     */
    private Boolean needDiplomDelete;

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
