package com.example.adminservice.api.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

/**
 * DTO: информация статуса профиля эксперта
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfileStatusDto {

    /**
     * ID статуса
     */
    private UUID id;

    /**
     * Результат проверки профиля администратором
     */
    @JsonProperty
    private String verificationResult;

    /**
     * Статус активности эксперта
     */
    @JsonProperty
    private String activityStatus;
}
