package com.example.auth.service.impl;

import com.example.auth.model.Role;
import com.example.auth.model.User;
import com.example.auth.model.dto.RegistrationRequest;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.RegistrationService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для регистрации пользователей.
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private static final Logger log = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    @Value("${keycloak.realm}")
    String realmName;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Keycloak keycloak;

    /**
     * Конструктор класса RegistrationServiceImpl.
     *
     * @param userRepository   репозиторий для работы с пользователями
     * @param roleRepository   репозиторий для работы с ролями
     * @param keycloak         клиент Keycloak
     */
    public RegistrationServiceImpl(UserRepository userRepository, RoleRepository roleRepository, Keycloak keycloak) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.keycloak = keycloak;
    }

    /**
     * Создание пользователя в Keycloak.
     *
     * @param request запрос на регистрацию (DTO)
     * @return созданный пользователь в Keycloak (UserRepresentation)
     */
    private UserRepresentation createKeycloakUser(RegistrationRequest request) {
        // Создаём пользователя в Keycloak
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(request.getEmail());  // так как вход стоит по email
        userRepresentation.setEmail(request.getEmail());
        userRepresentation.setFirstName(request.getFirstName());
        userRepresentation.setLastName(request.getLastName());
        userRepresentation.setEnabled(true);

        // Установим пароль
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setTemporary(false);
        cred.setValue(request.getPassword());

        userRepresentation.setCredentials(List.of(cred));

        return userRepresentation;
    }

    /**
     * Вызываем Admin API для создания пользователя в Keycloak (отправляем запрос).
     *
     * @param userRepresentation данные о пользователе в Keycloak
     * @return интерфейс управления пользователем в Keycloak
     */
    private UsersResource checkResponseRegistration(UserRepresentation userRepresentation) {
        UsersResource usersResource = keycloak.realm(realmName).users();
        Response response = usersResource.create(userRepresentation);

        if (response.getStatus() != 201) {
            // Ошибка: Keycloak не создал пользователя
            response.close(); // закрываем Response
            throw new RuntimeException("Keycloak user creation failed. Status: " + response.getStatus());
        }
        response.close();
        return usersResource;
    }

    /**
     * Получаем ID пользователя в Keycloak по email.
     *
     * @param usersResource интерфейс управления пользователем в Keycloak
     * @param email         email пользователя
     * @return ID пользователя в Keycloak
     */
    private String getKeycloakUserId(UsersResource usersResource, String email) {
        List<UserRepresentation> found = usersResource.search(email);
        if (found.isEmpty()) {
            throw new RuntimeException("Keycloak user not found. Email: " + email);
        }
        return found.getFirst().getId();
    }

    /**
     * Регистрация пользователя в системе (синхронизация таблицы пользователей Keycloak + базовая таблица user в БД).
     *
     * @param request данные о пользователе
     */
    @Transactional
    @Override
    public void register(RegistrationRequest request) {

        // Создаём объект пользователя для Keycloak
        UserRepresentation userRepresentation = createKeycloakUser(request);

        // Вызываем Admin API для создания пользователя
        UsersResource usersResource = checkResponseRegistration(userRepresentation);

        // 2. Нужно узнать ID созданного пользователя (чтобы связать с локальной БД).
        String keycloakUserId = getKeycloakUserId(usersResource, request.getEmail());

        // 3. Сохраняем локально в нашей таблице user
        User user = createUserMainTable(request, keycloakUserId);
        log.info("User successfully created: {}", user);
    }

    /**
     * Создание пользователя в нашей базе данных user (синхронизация с таблицей пользователей Keycloak).
     *
     * @param request        данные о пользователе
     * @param keycloakUserId ID уже созданного пользователя в Keycloak
     */
    private User createUserMainTable(RegistrationRequest request, String keycloakUserId) {
        User localUser = new User();

        Role role = roleRepository.findByName("Expert")
                .orElseThrow(() -> new RuntimeException("Role not found")); // потом убрать

        localUser.setFullName(request.getFirstName() + " " + request.getLastName());
        localUser.setEmail(request.getEmail());
        localUser.setRegistrationDate(LocalDateTime.now());
        localUser.setRole(role);
        localUser.setKeycloakId(keycloakUserId);

        return userRepository.save(localUser);
    }

}

