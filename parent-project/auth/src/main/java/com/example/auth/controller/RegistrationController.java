package com.example.auth.controller;

import com.example.auth.model.Role;
import com.example.auth.model.User;
import com.example.auth.model.dto.LoginRequest;
import com.example.auth.model.dto.RegistrationRequest;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.AuthService;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/public/auth")
public class RegistrationController {

    private final AuthService authService;

    public RegistrationController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegistrationRequest request) {
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // 1) Строим новый клиент через KeycloakBuilder
            //    но на этот раз указываем grant_type=password, clientId и secret (если клиент конфиденциальный).

            Keycloak keycloakForLogin = KeycloakBuilder.builder()
                    .serverUrl("http://localhost:8081")
                    .realm("demo")               // ваш realm
                    .clientId("spring-app")      // ваш clientId
                    // Если client "spring-app" является CONFIDENTIAL, надо указать clientSecret
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
            // Если Keycloak отвечает ошибкой (401 / invalid_grant), попадём сюда
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

}
