package com.example.adminservice.api.dto.profile;

import lombok.*;

/**
 * DTO: информация о направлении работы эксперта
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DirectionDto {

    /**
     * id направления работы
     */
    private String id;

    /**
     * Название направления работы
     */
    private String name;

}
