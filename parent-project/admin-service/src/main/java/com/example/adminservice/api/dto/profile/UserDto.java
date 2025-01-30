package com.example.adminservice.api.dto.profile;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO: информация о пользователе (регистрационные данные) - эксперта
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    /**
     * Идентификатор пользователя
     */
    private UUID id;

    /**
     * Имя пользователя
     */
    @JsonProperty
    private String fullName;

    /**
     * Email пользователя
     */
    private String email;

    /**
     * Дата регистрации пользователя
     */
    @JsonProperty
    private LocalDateTime registrationDate;

    /**
     * Роль пользователя
     */
    private String role;

}

