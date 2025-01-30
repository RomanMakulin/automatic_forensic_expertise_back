package com.example.adminservice.api.dto.profileCancel;

import com.example.adminservice.api.dto.profile.DirectionDto;
import com.example.adminservice.api.dto.profile.FilesDto;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty
    private String profileId;

    /**
     * Направления работы. Приходит с фронта (если хотя бы один элемент есть - его нужно удалить из БД)
     */
    private List<DirectionDto> directions;

    /**
     * Файлы. Приходит с фронта (если хотя бы один элемент есть - его нужно удалить из БД)
     */
    private List<FilesDto> files;

}
