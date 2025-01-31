package com.example.adminservice.api.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO: Все данные профиля для фронта
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {

    /**
     * id профиля
     */
    private UUID id;

    /**
     * Данные пользователя (регистрационные)
     */
    private UserDto user;

    /**
     * Фото профиля (ссылка на файл)
     */
    private String photo;

    /**
     * Телефон профиля
     */
    private String phone;

    /**
     * Локация профиля
     */
    private LocationDto location;

    /**
     * Статус профиля
     */
    @JsonProperty
    private ProfileStatusDto profileStatus;

    /**
     * Направления работы профиля
     */
    private List<DirectionDto> directions;

    /**
     * Файлы профиля
     */
    private List<FilesDto> files;

    /**
     * Шаблоны профиля
     * (Пока заглушка)
     */
//    private List<TemplateDto> templates;

}
