package com.example.adminservice.api.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "идентификатор не может быть пустым")
    private UUID id;

    /**
     * Данные пользователя (регистрационные)
     */
    @NotNull(message = "пользователь не может быть пустым")
    private UserDto user;

    /**
     * Фото профиля (ссылка на файл)
     */
    @NotNull(message = "фото не может быть пустым")
    private String photo;

    /**
     * Паспорт профиля (ссылка на файл)
     */
    @NotNull(message = "паспорт не может быть пустым")
    private String passport;

    /**
     * Диплом профиля (ссылка на файл)
     */
    @NotNull(message = "диплом не может быть пустым")
    private String diplom;

    /**
     * Телефон профиля
     */
    @NotNull(message = "номер телефона не может быть пустым")
    private String phone;

    /**
     * Локация профиля
     */
    @NotNull(message = "локация не может быть пустой")
    private LocationDto location;

    /**
     * Статус профиля
     */
    @JsonProperty("profile_status")
    @NotNull(message = "статус пользователя не может быть пустым")
    private ProfileStatusDto profileStatus;

    /**
     * Направления работы профиля
     */
    @NotEmpty(message = "направления работы не может быть пустым")
    private List<DirectionDto> directions;

    /**
     * Файлы профиля
     */
    @NotEmpty(message = "файлы не может быть пустым")
    private List<FilesDto> files;

}
