package com.example.service;

import com.example.integration.mail.MailIntegration;
import com.example.integration.mail.dto.MailRequest;
import com.example.model.AppUser;
import com.example.model.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для отправки писем
 */
@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    private final AppUserService appUserService;

    private final MailIntegration mailIntegration;

    public MailService(AppUserService appUserService,
                       MailIntegration mailIntegration) {
        this.appUserService = appUserService;
        this.mailIntegration = mailIntegration;
    }

    /**
     * Отправить рассылку администраторам о необходимости верифицировать профиль
     *
     * @param profileSaved новый профиль сохраненный в БД
     */
    public void sendMailToAdmins(Profile profileSaved) {
        try {
            List<AppUser> appUsers = appUserService.getAdmins();

            if (!appUsers.isEmpty()) {
                appUsers.forEach(appUser -> {
                    try {
                        MailRequest mailRequest = new MailRequest(
                                appUser.getEmail(),
                                "Необходимо верифицировать пользователя",
                                "Необходимо верифицировать пользователя: " + profileSaved.getAppUser().getEmail());

                        mailIntegration.sendMail(mailRequest);
                        log.info("Отправлено письмо администратору: {}", appUser.getEmail());
                    } catch (Exception e) {
                        log.error("Ошибка отправки письма админу {}: {}", appUser.getEmail(), e.getMessage(), e);
                    }
                });
            } else {
                log.error("Ошибка рассылки: нет администраторов в системе");
            }
        } catch (Exception e) {
            log.error("Ошибка при отправке писем администраторам: {}", e.getMessage(), e);
        }
    }

}
