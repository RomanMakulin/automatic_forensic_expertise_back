package com.example.auth.service;

import com.example.auth.model.User;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс для работы с пользователем.
 */
public interface UserService {

    /**
     * Возвращает всех пользователей.
     *
     * @return список пользователей
     */
    List<User> getAllUsers();

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return пользователь
     */
    User getUserById(UUID id);

    /**
     * Возвращает пользователя по его идентификатору в Keycloak.
     *
     * @param keycloakId идентификатор пользователя в Keycloak
     * @return пользователь
     */
    User getUserByKeycloakId(String keycloakId);

    /**
     * Изменяет имя пользователя.
     *
     * @param name новое имя пользователя
     */
    void changeName(String name);

}

