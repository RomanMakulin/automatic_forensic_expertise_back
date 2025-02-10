package com.example.auth.service.auth;

import com.example.auth.config.ApiPathsConfig;
import com.example.auth.api.dto.MailRequest;
import com.example.auth.api.dto.ResetPassword;
import com.example.auth.integrations.keycloak.KeycloakAdminService;
import com.example.auth.integrations.mail.MailService;
import org.springframework.stereotype.Service;

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

    /**
     * Сервис для работы с админкой Keycloak
     */
    private final KeycloakAdminService keycloakAdminService;

    /**
     * Конфигурация путей API
     */
    private final ApiPathsConfig apiPathsConfig;


    public PasswordServiceImpl(MailService mailService,
                               PasswordTokenService passwordTokenService,
                               KeycloakAdminService keycloakAdminService,
                               ApiPathsConfig apiPathsConfig) {
        this.mailService = mailService;
        this.passwordTokenService = passwordTokenService;
        this.keycloakAdminService = keycloakAdminService;
        this.apiPathsConfig = apiPathsConfig;
    }

    /**
     * Запрос на сброс пароля по электронной почте.
     *
     * @param email электронная почта
     */
    @Override
    public void resetPasswordRequest(String email) {

        String token = passwordTokenService.createPasswordResetToken(email);

        String apiPath = apiPathsConfig.getFrontend().get("recovery-request");

        // Формируем ссылку для восстановления пароля
        String resetLink = apiPath + "?token=" + token;

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
