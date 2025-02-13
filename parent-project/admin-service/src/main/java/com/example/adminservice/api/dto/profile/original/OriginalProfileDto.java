package com.example.adminservice.api.dto.profile.original;

import com.example.adminservice.api.dto.profile.DirectionDto;
import com.example.adminservice.api.dto.profile.FilesDto;
import com.example.adminservice.api.dto.profile.LocationDto;
import lombok.*;

import java.util.Set;
import java.util.UUID;

/**
 * DTO модели Profile
 * Принимаемый объект с мордуля "profile"
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OriginalProfileDto {

    /**
     * Идентификатор
     */
    private UUID id;

    /**
     * Пользователь
     */
    private OriginalUserDto appUser;

    /**
     * Телефон
     */
    private String phone;

    /**
     * Местоположение
     */
    private LocationDto location;

    /**
     * Статус
     */
    private OriginalStatusDto status;

    /**
     * Список файлов
     */
    private Set<OriginalFilesDto> files;

    /**
     * Список направлений
     */
    private Set<DirectionDto> directions;
}
