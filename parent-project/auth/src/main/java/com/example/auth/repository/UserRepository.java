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

    Optional<User> findByEmail(String email);

}
