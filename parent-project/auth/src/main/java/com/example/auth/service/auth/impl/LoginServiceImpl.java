package com.example.auth.service.auth.impl;

import com.example.auth.api.dto.AuthUserDetailsResponse;
import com.example.auth.api.dto.LoginRequest;
import com.example.auth.api.dto.profile.ProfileDTO;
import com.example.auth.integrations.profile.ProfileIntegration;
import com.example.auth.model.User;
import com.example.auth.service.auth.LoginService;
import com.example.auth.service.user.UserService;
import com.example.auth.util.KeycloakConsts;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Сервис для аутентификации пользователей.
 */
@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);
    /**
     * Константы Keycloak.
     */
    private final KeycloakConsts keycloakConsts;

    private final UserService userService;

    private final ProfileIntegration profileIntegration;

    /**
     * Конструктор класса LoginServiceImpl.
     *
     * @param keycloakConsts константы Keycloak
     */
    public LoginServiceImpl(KeycloakConsts keycloakConsts,
                            UserService userService,
                            ProfileIntegration profileIntegration) {
        this.keycloakConsts = keycloakConsts;
        this.userService = userService;
        this.profileIntegration = profileIntegration;
    }

    /**
     * Аутентификация пользователя.
     *
     * @param request запрос на аутентификацию, содержащий информацию о пользователе
     * @return ответ сервера, содержащий результат аутентификации
     */
    @Override
    public AuthUserDetailsResponse login(LoginRequest request) {

        verifyEmail(request.getEmail()); // Проверка email пользователя на подтверждение

        Keycloak keycloakForLogin = KeycloakBuilder.builder()
                .serverUrl(keycloakConsts.getAuthServerUrlAdmin())
                .realm(keycloakConsts.getRealm())
                .clientId(keycloakConsts.getResource())
                .clientSecret(keycloakConsts.getSecret())
                .grantType(OAuth2Constants.PASSWORD)
                .username(request.getEmail())
                .password(request.getPassword())
                .build();

        AccessTokenResponse accessTokenResponse = keycloakForLogin.tokenManager().getAccessToken(); // Делаем запрос токена
        User user = userService.getUserByEmail(request.getEmail());

        ProfileDTO profileDTO = profileIntegration.getProfileRequest(user.getId(), accessTokenResponse.getToken());

        return new AuthUserDetailsResponse(user, accessTokenResponse, profileDTO);
    }

    /**
     * Проверка email пользователя на подтверждение.
     *
     * @param email email пользователя
     */
    private void verifyEmail(String email) {
        boolean isVerified = userService.getUserByEmail(email).isVerificationEmail();

        if (!isVerified) {
            throw new RuntimeException("Email is not verified");
        }
    }

}
