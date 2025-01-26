package com.example.auth.service.impl;

import com.example.auth.model.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.AuthService;
import com.example.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Реализация сервиса для работы с пользователем.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    /**
     * Репозиторий для работы с пользователями.
     */
    private final UserRepository userRepository;

    /**
     * Сервис для работы с авторизацией.
     */
    private final AuthService authService;


    /**
     * Конструктор класса UserServiceImpl.
     *
     * @param userRepository репозиторий для работы с пользователями
     */
    public UserServiceImpl(UserRepository userRepository,
                           AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    /**
     * Возвращает всех пользователей.
     *
     * @return список пользователей
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return пользователь
     */
    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Возвращает пользователя по его идентификатору в Keycloak.
     *
     * @param keycloakId идентификатор пользователя в Keycloak
     * @return пользователь
     */
    @Override
    public User getUserByKeycloakId(String keycloakId) {
        return userRepository.findUserByKeycloakId(keycloakId).orElseThrow(() -> new RuntimeException("User not found"));
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

