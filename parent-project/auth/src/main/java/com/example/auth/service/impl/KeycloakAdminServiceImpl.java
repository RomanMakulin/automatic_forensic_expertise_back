package com.example.auth.service.impl;

import com.example.auth.model.dto.RegistrationRequest;
import com.example.auth.service.KeycloakAdminService;
import com.example.auth.util.KeycloakConsts;
import exceptions.CannotFindKeycloakUserException;
import exceptions.UserAlreadyExistsException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с админкой Keycloak.
 */
@Service
public class KeycloakAdminServiceImpl implements KeycloakAdminService {

    private static final Logger log = LoggerFactory.getLogger(KeycloakAdminServiceImpl.class);
    private final Keycloak keycloak;
    private final KeycloakConsts keycloakConsts;

    public KeycloakAdminServiceImpl(Keycloak keycloak, KeycloakConsts keycloakConsts) {
        this.keycloak = keycloak;
        this.keycloakConsts = keycloakConsts;
    }

    /**
     * Создаёт пользователя Keycloak.
     *
     * @param request данные пользователя для регистрации (DTO)
     * @return представление пользователя
     */
    @Transactional
    @Override
    public void createKeycloakUser(RegistrationRequest request) {
        // Создаём объект пользователя для Keycloak
        UserRepresentation userRepresentation = fillKeycloakNewUser(request);

        UsersResource usersResource = keycloak.realm(keycloakConsts.getRealm()).users();
        Response response = usersResource.create(userRepresentation);

        int status = response.getStatus();
        response.close();

        if (status == 201) {
            log.info("User successfully created in Keycloak table: {}", userRepresentation);
        } else if (status == 409) {
            log.error("User already exists in Keycloak table: {}", userRepresentation);
            throw new UserAlreadyExistsException("Такой пользователь уже существует (409).");
        } else {
            throw new RuntimeException("Keycloak user creation failed. Status: " + status);
        }
    }

    /**
     * Создание пользователя в Keycloak.
     *
     * @param request запрос на регистрацию (DTO)
     * @return созданный пользователь в Keycloak (UserRepresentation)
     */
    private UserRepresentation fillKeycloakNewUser(RegistrationRequest request) {
        // Создаём пользователя в Keycloak
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(request.getEmail());  // так как вход стоит по email
        userRepresentation.setEmail(request.getEmail());
        userRepresentation.setFirstName(request.getFirstName());
        userRepresentation.setLastName(request.getLastName());
        // сюда же можно добавить роль
        userRepresentation.setEnabled(true);

        // Установим пароль
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setTemporary(false);
        cred.setValue(request.getPassword());

        userRepresentation.setCredentials(List.of(cred));

        return userRepresentation;
    }

    /**
     * Удаление пользователя из Keycloak
     *
     * @param keycloakUserId id пользователя
     */
    @Transactional
    @Override
    public void deleteUserById(String keycloakUserId) {
        try {
            keycloak.realm(keycloakConsts.getRealm())
                    .users()
                    .delete(keycloakUserId);
        } catch (Exception e) {
            log.error("Error deleting user (keycloak). Message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Удаление пользователя из Keycloak по соответствующему email
     *
     * @param email почта пользователя
     */
    @Transactional
    @Override
    public void deleteUserByEmail(String email) {
        UserRepresentation user = getUserByEmail(email);
        deleteUserById(user.getId());
    }

    /**
     * Получить пользователя по id юзера Keycloak
     *
     * @param keycloakUserId id пользователя
     * @return юзер Keycloak
     */
    @Override
    public UserRepresentation getUserById(String keycloakUserId) {
        try {
            return keycloak.realm(keycloakConsts.getRealm())
                    .users()
                    .get(keycloakUserId)
                    .toRepresentation();
        } catch (Exception e) {
            log.error("Error retrieving user (keycloak). Message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Поиск пользователей по подстроке (username, email и т.п.)
     * Например, поиск "test" вернёт всех пользователей, у которых в имени/юзернейме/почте есть "test".
     *
     * @param searchString строка поиска
     */
    @Override
    public List<UserRepresentation> findUsers(String searchString) {
        return keycloak.realm(keycloakConsts.getRealm())
                .users()
                .search(searchString);
    }

    @Override
    public UserRepresentation getUserByEmail(String email) {

        // Общий список совпадений
        List<UserRepresentation> usersList = keycloak.realm(keycloakConsts.getRealm())
                .users()
                .search(email);

        // пользователя с таким email нет
        if (usersList.isEmpty()) {
            throw new CannotFindKeycloakUserException("No keycloak user found with email: " + email);
        }

        // Находим точное совпадение
        Optional<UserRepresentation> exactMatch = usersList.stream()
                .filter(u -> email.equalsIgnoreCase(u.getEmail()))
                .findFirst();

        if (exactMatch.isPresent()) {
            return exactMatch.get();
        } else {
            throw new CannotFindKeycloakUserException("No keycloak user found with email: " + email);
        }
    }

    /**
     * Обновить данные пользователя (firstName, lastName, email, enabled...)
     * Параметр user - это готовый UserRepresentation, у которого установлен ID (user.getId()) и новые поля.
     *
     * @param user данные юзера
     */
    @Transactional
    @Override
    public void updateUser(UserRepresentation user) {
        try {
            keycloak.realm(keycloakConsts.getRealm())
                    .users()
                    .get(user.getId())
                    .update(user);
        } catch (Exception e) {
            log.error("Error updating user (keycloak). Message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Сбросить пароль пользователя (установить новый).
     * temporary = true => при следующем входе пользователь должен сменить пароль.
     *
     * @param keycloakUserId id пользователя
     * @param newPassword    новый пароль
     * @param temporary      должен ли пользователь сменить пароль при слеожном входе
     */
    @Transactional
    @Override
    public void resetPassword(String keycloakUserId, String newPassword, boolean temporary) {
        try {
            CredentialRepresentation cred = new CredentialRepresentation();
            cred.setType(CredentialRepresentation.PASSWORD);
            cred.setValue(newPassword);
            cred.setTemporary(temporary);

            keycloak.realm(keycloakConsts.getRealm())
                    .users()
                    .get(keycloakUserId)
                    .resetPassword(cred);
        } catch (Exception e) {
            log.error("Error updating password (keycloak). Message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Пример назначения realm-роли пользователю (по имени роли).
     * В данном случае ищем роль на уровне Realm, а не Client.
     *
     * @param keycloakUserId id пользователя
     * @param roleName       название роли
     */
    @Transactional
    @Override
    public void assignRealmRole(String keycloakUserId, String roleName) {
        try {
            RoleRepresentation role = keycloak.realm(keycloakConsts.getRealm())
                    .roles()
                    .get(roleName)
                    .toRepresentation();

            keycloak.realm(keycloakConsts.getRealm())
                    .users()
                    .get(keycloakUserId)
                    .roles()
                    .realmLevel()
                    .add(Collections.singletonList(role));
        } catch (Exception e) {
            log.error("Error assigning role (keycloak). Message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Удалить (отобрать) realm-роль у пользователя.
     *
     * @param keycloakUserId id пользователя
     * @param roleName       название роли
     */
    @Transactional
    @Override
    public void removeRealmRole(String keycloakUserId, String roleName) {
        try {
            RoleRepresentation role = keycloak.realm(keycloakConsts.getRealm())
                    .roles()
                    .get(roleName)
                    .toRepresentation();

            keycloak.realm(keycloakConsts.getRealm())
                    .users()
                    .get(keycloakUserId)
                    .roles()
                    .realmLevel()
                    .remove(Collections.singletonList(role));
        } catch (Exception e) {
            log.error("Error removing role (keycloak). Message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Получить все realm-роли, которые назначены пользователю.
     *
     * @param keycloakUserId id пользователя
     */
    @Override
    public List<RoleRepresentation> getUserRealmRoles(String keycloakUserId) {
        return keycloak.realm(keycloakConsts.getRealm())
                .users()
                .get(keycloakUserId)
                .roles()
                .realmLevel()
                .listAll();
    }

}
