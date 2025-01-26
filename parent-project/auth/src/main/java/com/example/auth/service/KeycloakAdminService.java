package com.example.auth.service;

import com.example.auth.model.dto.RegistrationRequest;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

/**
 * Интерфейс для работы с админкой Keycloak.
 */
public interface KeycloakAdminService {

    /**
     * Создает пользователя Keycloak.
     *
     * @param request данные пользователя для регистрации (DTO)
     * @return представление пользователя
     */
    void createKeycloakUser(RegistrationRequest request);

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param keycloakUserId идентификатор пользователя
     */
    void deleteUserById(String keycloakUserId);

    /**
     * Удаляет пользователя по его email.
     *
     * @param email email пользователя
     */
    void deleteUserByEmail(String email);

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param keycloakUserId идентификатор пользователя
     * @return представление пользователя
     */
    UserRepresentation getUserById(String keycloakUserId);

    /**
     * Возвращает список пользователей, соответствующих поисковому запросу.
     *
     * @param searchString поисковый запрос
     * @return список представлений пользователей
     */
    List<UserRepresentation> findUsers(String searchString);

    /**
     * Возвращает пользователя keycloak по его email.
     *
     * @param email email пользователя
     * @return представление пользователя
     */
    UserRepresentation getUserByEmail(String email);

    /**
     * Обновляет информацию о пользователе.
     *
     * @param user представление пользователя
     */
    void updateUser(UserRepresentation user);

    /**
     * Сбрасывает пароль пользователя.
     *
     * @param keycloakUserId идентификатор пользователя
     * @param newPassword    новый пароль
     * @param temporary      флаг, указывающий, является ли новый пароль временным
     */
    void resetPassword(String keycloakUserId, String newPassword, boolean temporary);

    /**
     * Назначает роль пользователя в рамках реалма.
     *
     * @param keycloakUserId идентификатор пользователя
     * @param roleName       имя роли
     */
    void assignRealmRole(String keycloakUserId, String roleName);

    /**
     * Удаляет роль пользователя в рамках реалма.
     *
     * @param keycloakUserId идентификатор пользователя
     * @param roleName       имя роли
     */
    void removeRealmRole(String keycloakUserId, String roleName);

    /**
     * Возвращает список ролей пользователя в рамках реалма.
     *
     * @param keycloakUserId идентификатор пользователя
     * @return список представлений ролей
     */
    List<RoleRepresentation> getUserRealmRoles(String keycloakUserId);
}
