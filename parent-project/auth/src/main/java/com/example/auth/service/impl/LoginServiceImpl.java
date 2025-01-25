package com.example.auth.service.impl;

import com.example.auth.model.dto.LoginRequest;
import com.example.auth.service.LoginService;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public ResponseEntity<?> login(LoginRequest request) {
        try {
            Keycloak keycloakForLogin = KeycloakBuilder.builder()
                    .serverUrl("http://localhost:8081")
                    .realm("demo")
                    .clientId("spring-app")
                    .clientSecret("Iz7VgXRx9udwlbpurBD4R3Wf3YMToNsQ")
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .build();

            // 2) Делаем запрос токена
            AccessTokenResponse tokenResponse = keycloakForLogin.tokenManager().getAccessToken();

            // Возвращаем токен
            return ResponseEntity.ok(tokenResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

}
