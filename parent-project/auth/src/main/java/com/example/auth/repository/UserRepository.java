package com.example.auth.repository;

import com.example.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс репозитория для работы с пользователями.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Проверяет, существует ли пользователь с указанным email.
     *
     * @param email email пользователя
     * @return true, если пользователь с указанным email существует, иначе false
     */
    boolean existsByEmail(String email);

    /**
     * Поиск пользователя по email
     *
     * @param email email пользователя
     * @return юзер, если он существует, иначе Optional.empty()
     */
    Optional<User> findByEmail(String email);

    /**
     * Поиск пользователя по keycloakId
     *
     * @param keycloakId id пользователя
     * @return юзер, если он существует, иначе Optional.empty()
     */
    Optional<User> findUserByKeycloakId(String keycloakId);

}
