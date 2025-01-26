package com.example.auth.service.impl;

import com.example.auth.model.Role;
import com.example.auth.model.User;
import com.example.auth.model.dto.RegistrationRequest;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.KeycloakAdminService;
import com.example.auth.service.RegistrationService;
import jakarta.persistence.EntityManager;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final KeycloakAdminService keycloakAdminService;
    private final EntityManager entityManager;

    public RegistrationServiceImpl(UserRepository userRepository,
                                   RoleRepository roleRepository,
                                   KeycloakAdminService keycloakAdminService,
                                   EntityManager entityManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.keycloakAdminService = keycloakAdminService;
        this.entityManager = entityManager;
    }

    /**
     * Регистрация пользователя в системе (синхронизация таблицы пользователей Keycloak + базовая таблица user в БД).
     *
     * @param request данные о пользователе
     */
    @Transactional
    @Override
    public void register(RegistrationRequest request) {

        // Создаем User в Keycloak БД
        keycloakAdminService.createKeycloakUser(request);

        // Нужно узнать ID созданного пользователя (чтобы связать с локальной БД).
        String keycloakUserId = keycloakAdminService.getUserByEmail(request.getEmail()).getId();

        // Сохраняем локально в нашей таблице user
        createUserMainTable(request, keycloakUserId);
    }

    /**
     * Создание пользователя в нашей базе данных user (синхронизация с таблицей пользователей Keycloak).
     *
     * @param request        данные о пользователе
     * @param keycloakUserId ID уже созданного пользователя в Keycloak
     */
    private void createUserMainTable(RegistrationRequest request, String keycloakUserId) {
        try {
            User localUser = new User();

            Role role = roleRepository.findByName("Expert")
                    .orElseThrow(() -> new RuntimeException("Role not found")); // потом убрать

            localUser.setFullName(request.getFirstName() + " " + request.getLastName());
            localUser.setEmail(request.getEmail());
            localUser.setRegistrationDate(LocalDateTime.now());
            localUser.setRole(role);
            localUser.setKeycloakId(keycloakUserId);

            userRepository.save(localUser);
            log.info("User successfully created: {}", localUser);
            entityManager.flush(); // Принудительная фиксация изменений в БД
        } catch (Exception e) {
            // При возникновении ошибки удаляем пользователя из Keycloak
            keycloakAdminService.deleteUserByEmail(request.getEmail());
            throw new RuntimeException("User not created in local table", e);
        }
    }

}

