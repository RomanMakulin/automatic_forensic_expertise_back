package com.example.auth.service.impl;

import com.example.auth.repository.UserRepository;
import com.example.auth.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса для работы с пользователем.
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * Репозиторий для работы с пользователями.
     */
    private final UserRepository userRepository;

    /**
     * Конструктор класса UserServiceImpl.
     *
     * @param userRepository репозиторий для работы с пользователями
     */
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Изменяет электронную почту пользователя.
     *
     * @param email новая электронная почта пользователя
     */
    @Override
    public void changeEmail(String email) {

    }

    /**
     * Изменяет пароль пользователя.
     *
     * @param password новый пароль пользователя
     */
    @Override
    public void changePassword(String password) {

    }

    /**
     * Изменяет имя пользователя.
     *
     * @param name новое имя пользователя
     */
    @Override
    public void changeName(String name) {

    }
}

