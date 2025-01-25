package com.example.auth.controller;

import com.example.auth.model.Role;
import com.example.auth.model.User;
import com.example.auth.model.dto.LoginRequest;
import com.example.auth.model.dto.RegistrationRequest;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
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

    @GetMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {

        // 1. Создаём пользователя в Keycloak
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(request.getEmail());  // можно username = email
        userRepresentation.setEmail(request.getEmail());
        userRepresentation.setFirstName(request.getFirstName());
        userRepresentation.setLastName(request.getLastName());
        userRepresentation.setEnabled(true);

;
        // Установим пароль
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setTemporary(false);
        cred.setValue(request.getPassword());

        userRepresentation.setCredentials(List.of(cred));

        // Вызываем Admin API для создания пользователя
        UsersResource usersResource = keycloak.realm(realmName).users();
        Response response = usersResource.create(userRepresentation);

        if (response.getStatus() != 201) {
            // Ошибка: Keycloak не создал пользователя
            String error = "Не удалось создать пользователя в Keycloak. Статус: " + response.getStatus();
            response.close(); // закрываем Response

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        response.close();


        // 2. Нужно узнать ID созданного пользователя (чтобы связать с локальной БД).
        // Обычно ищем по username/email. Или Keycloak вернёт Location с /{uid}
        List<UserRepresentation> found = usersResource.search(request.getEmail());
        if (found.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Пользователь создан, но не найден при поиске");
        }

        String keycloakUserId = found.get(0).getId();

        // 3. Сохраняем локально в нашей таблице user
        User localUser = new User();

        Role role = roleRepository.findByName("Expert")
                .orElseThrow(() -> new RuntimeException("Не найден роль ROLE_USER")); // потом убрать


        localUser.setFullName(request.getFirstName() + " " + request.getLastName());
        localUser.setEmail(request.getEmail());
        localUser.setRegistrationDate(LocalDateTime.now());
        localUser.setRole(role);
        localUser.setKeycloakId(keycloakUserId);

        userRepository.save(localUser);

        return ResponseEntity.ok("Пользователь успешно зарегистрирован. KeycloakId = " + keycloakUserId);
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
