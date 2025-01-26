package com.example.auth.service.impl;

import com.example.auth.model.dto.LoginRequest;
import com.example.auth.service.LoginService;
import com.example.auth.util.KeycloakConsts;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

/**
 * Сервис для аутентификации пользователей.
 */
@Service
public class LoginServiceImpl implements LoginService {

    /**
     * Константы Keycloak.
     */
    private final KeycloakConsts keycloakConsts;

    /**
     * Конструктор класса LoginServiceImpl.
     *
     * @param keycloakConsts константы Keycloak
     */
    public LoginServiceImpl(KeycloakConsts keycloakConsts) {
        this.keycloakConsts = keycloakConsts;
    }

    /**
     * Аутентификация пользователя.
     *
     * @param request запрос на аутентификацию, содержащий информацию о пользователе
     * @return ответ сервера, содержащий результат аутентификации
     */
    @Override
    public AccessTokenResponse login(LoginRequest request) {

        Keycloak keycloakForLogin = KeycloakBuilder.builder()
                .serverUrl(keycloakConsts.getAuthServerUrlAdmin())
                .realm(keycloakConsts.getRealm())
                .clientId(keycloakConsts.getResource())
                .clientSecret(keycloakConsts.getSecret())
                .grantType(OAuth2Constants.PASSWORD)
                .username(request.getEmail())
                .password(request.getPassword())
                .build();

        // Делаем запрос токена и возвращаем его
        return keycloakForLogin.tokenManager().getAccessToken();
    }

}
