package com.example.auth.service.sheduler;

import com.example.auth.model.User;
import com.example.auth.integrations.keycloak.KeycloakAdminService;
import com.example.auth.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Служба для выполнения задач по расписанию.
 */
@Service
public class DailyTaskScheduler {

    private static final Logger log = LoggerFactory.getLogger(DailyTaskScheduler.class);

    /**
     * Сервис для работы с пользователями.
     */
    private final UserService userService;

    private final KeycloakAdminService keycloakAdminService;

    public DailyTaskScheduler(UserService userService,
                              KeycloakAdminService keycloakAdminService) {
        this.userService = userService;
        this.keycloakAdminService = keycloakAdminService;
    }

    /**
     * Запланированно, каждые 4 часа, выполняется задача для удаления неподтвержденных пользователей.
     */
    @Scheduled(cron = "0 0 0/4 * * ?") // Каждые 4 часа
    public void executeDailyTask() {
        // Получение всех неподтвержденных пользователей
        List<User> users = userService.getNotVerifiedUsers();

        if (users.isEmpty()) {
            log.debug("Нет неподтвержденных пользователей");
            return;
        }

        // Удаление пользователей из Keycloak и локальной базы данных
        users.forEach(user -> {
            try {
                keycloakAdminService.deleteUserByEmail(user.getEmail()); // Удаление из Keycloak
                userService.deleteUser(user); // Удаление из локальной базы
                log.debug("Пользователь удален: {}", user);
            } catch (Exception e) {
                log.error("Ошибка при удалении пользователя: {}", user, e);
            }
        });

        log.info("Удалено неподтвержденных пользователей: {}", users.size());
    }

}
