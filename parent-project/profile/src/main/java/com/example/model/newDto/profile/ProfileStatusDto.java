package com.example.model.newDto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
     * Результат проверки профиля администратором
     */
    @JsonProperty("verification_result")
    @NotNull(message = "результат проверки профиля администратором не может быть null")
    private String verificationResult;

    /**
     * Статус активности эксперта
     */
    @JsonProperty("activity_status")
    @NotNull(message = "статус активности эксперта не может быть null")
    private String activityStatus;
}
