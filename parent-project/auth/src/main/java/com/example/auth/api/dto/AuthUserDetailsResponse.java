package com.example.auth.api.dto;

import com.example.auth.api.dto.profile.ProfileDTO;
import com.example.auth.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.keycloak.representations.AccessTokenResponse;

/**
 * DTO для получения информации о пользователе при успешной авторизации
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserDetailsResponse {

    /**
     * Информация о пользователе
     */
    private User user;

    /**
     * Токен пользователя
     */
    @JsonProperty("access_token")
    private AccessTokenResponse accessToken;

    private ProfileDTO profile;

}
