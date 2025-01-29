package com.example.auth.service.impl;

import com.example.auth.model.User;
import com.example.auth.model.dto.MailRequest;
import com.example.auth.model.dto.ResetPassword;
import com.example.auth.service.*;
import com.example.auth.service.integrations.mail.MailService;
import com.example.auth.util.ApiPaths;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordServiceImpl implements PasswordService {

    /**
     * Сервис для отправки писем
     */
    private final MailService mailService;

    /**
     * Сервис для работы с токенами сброса пароля
     */
    private final PasswordTokenService passwordTokenService;

    private final KeycloakAdminService keycloakAdminService;


    public PasswordServiceImpl(MailService mailService,
                               PasswordTokenService passwordTokenService,
                               KeycloakAdminService keycloakAdminService) {
        this.mailService = mailService;
        this.passwordTokenService = passwordTokenService;
        this.keycloakAdminService = keycloakAdminService;
    }

    /**
     * Запрос на сброс пароля по электронной почте.
     *
     * @param email электронная почта
     */
    @Override
    public void resetPasswordRequest(String email) {

        String token = passwordTokenService.createPasswordResetToken(email);

        // Формируем ссылку для восстановления пароля
        String resetLink = ApiPaths.Frontend.RESET_REQUEST + "?token=" + token;

        // Создаём HTML-письмо с кликабельной ссылкой
        String message = "<p>Для восстановления пароля перейдите по ссылке: " +
                "<a href='" + resetLink + "'>Восстановить пароль</a></p>";

        MailRequest mailRequest = new MailRequest(email, "Reset password", message);

        mailService.publicSendMail(mailRequest);
    }

    /**
     * Сброс пароля.
     *
     * @param resetPassword  объект DTO с данными для сброса пароля
     */
    @Override
    public void resetPassword(ResetPassword resetPassword) {
        passwordTokenService.validateToken(resetPassword.getToken()); // проверяем токен

        String keycloakId = passwordTokenService.getUser(resetPassword.getToken()).getKeycloakId();
        keycloakAdminService.resetPassword(keycloakId, resetPassword.getPassword());
    }
}
