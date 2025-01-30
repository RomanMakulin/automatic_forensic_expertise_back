package com.example.auth.repository;

import com.example.auth.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс репозитория для работы с токенами сброса пароля.
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    /**
     * Поиск токена по его значению.
     *
     * @param token значение токена
     * @return токен, если он существует, иначе Optional.empty()
     */
    Optional<PasswordResetToken> findByToken(String token);

    /**
     * Удаление токена по его значению.
     *
     * @param token значение токена
     */
    void deleteByToken(String token);
}
