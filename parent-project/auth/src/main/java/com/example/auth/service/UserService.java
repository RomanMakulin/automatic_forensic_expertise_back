package com.example.auth.service;

/**
 * Интерфейс для работы с пользователем.
 */
public interface UserService {

    /**
     * Изменяет электронную почту пользователя.
     *
     * @param email новая электронная почта пользователя
     */
    void changeEmail(String email);

    /**
     * Изменяет пароль пользователя.
     *
     * @param password новый пароль пользователя
     */
    void changePassword(String password);

    /**
     * Изменяет имя пользователя.
     *
     * @param name новое имя пользователя
     */
    void changeName(String name);

}

