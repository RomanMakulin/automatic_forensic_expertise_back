package com.example.adminservice.api.dto.profile;

import lombok.*;

import java.util.UUID;

/**
 * DTO: информация о локации работы эксперта
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    /**
     * Страна
     */
    private String country;

    /**
     * Регион
     */
    private String region;

    /**
     * Город
     */
    private String city;

    /**
     * Адрес
     */
    private String address;

}
