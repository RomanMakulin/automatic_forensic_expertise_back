package com.example.auth.service.user;

import com.example.auth.model.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.auth.AuthService;
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
     * Конструктор класса UserServiceImpl.
     *
     * @param userRepository репозиторий для работы с пользователями
     */
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
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

    /**
     * Получает список не прошедших проверку пользователей.
     */
    @Override
    public List<User> getNotVerifiedUsers() {
        return userRepository.findUnverifiedUsers();
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     */
    @Override
    public void deleteUserById(UUID id) {
        userRepository.deleteById(id);
    }

    /**
     * Удаляет пользователя по его email.
     *
     * @param email email пользователя
     */
    @Override
    public void deleteUserByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    /**
     * Удаляет пользователя.
     *
     * @param user пользователь
     */
    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}

