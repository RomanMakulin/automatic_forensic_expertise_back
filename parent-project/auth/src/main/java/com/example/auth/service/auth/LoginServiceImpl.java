package com.example.auth.service.auth;

import com.example.auth.model.dto.LoginRequest;
import com.example.auth.service.user.UserService;
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

    private final UserService userService;

    /**
     * Конструктор класса LoginServiceImpl.
     *
     * @param keycloakConsts константы Keycloak
     */
    public LoginServiceImpl(KeycloakConsts keycloakConsts,
                            UserService userService) {
        this.keycloakConsts = keycloakConsts;
        this.userService = userService;
    }

    /**
     * Аутентификация пользователя.
     *
     * @param request запрос на аутентификацию, содержащий информацию о пользователе
     * @return ответ сервера, содержащий результат аутентификации
     */
    @Override
    public AccessTokenResponse login(LoginRequest request) {

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

        // Делаем запрос токена и возвращаем его
        return keycloakForLogin.tokenManager().getAccessToken();
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
