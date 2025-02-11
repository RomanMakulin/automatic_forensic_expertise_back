package com.example.adminservice.service;

import com.example.adminservice.api.dto.profile.ProfileDto;
import com.example.adminservice.api.dto.profileCancel.ProfileCancel;
import com.example.adminservice.config.AppConfig;
import com.example.adminservice.exceptions.ProfileServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Реализация сервиса управления профилями админкой
 */
@Service
public class ProfileManageServiceImpl implements ProfileManageService {

    private static final Logger log = LoggerFactory.getLogger(ProfileManageServiceImpl.class);

    private final RestTemplate restTemplate;

    /**
     * Пути API
     */
    private final AppConfig appConfig;

    public ProfileManageServiceImpl(RestTemplate restTemplate, AppConfig appConfig) {
        this.restTemplate = restTemplate;
        this.appConfig = appConfig;
    }

    /**
     * Создание HTTP-заголовков с Bearer-токеном для авторизации
     *
     * @return HttpHeaders с заголовком Authorization
     */
    private HttpHeaders createAuthHeaders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalStateException("Невозможно получить JWT из SecurityContext");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt.getTokenValue());
        return headers;
    }


    /**
     * Отправка запроса на получение всех профилей
     *
     * @return - список профилей
     */
    @Override
    public List<ProfileDto> getAllProfiles() {
        return getProfilesProcess("get-all-profiles");
    }

    /**
     * Отправка запроса на получение непроверенных профилей
     *
     * @return - список профилей
     */
    @Override
    public List<ProfileDto> getNotVerifiedProfiles() {
        return getProfilesProcess("get-unverified-profiles");
    }

    /**
     * Общая реализация отправки запроса на получение профилей
     *
     * @param passKey - ключ урла для запроса из проперти
     * @return - список профилей
     */
    private List<ProfileDto> getProfilesProcess(String passKey) {
        String pathApi = appConfig.getPaths().getProfile().get(passKey);
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<ProfileDto>> response = restTemplate.exchange(
                    pathApi,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<ProfileDto>>() {
                    }
            );
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("Ошибка получения профилей. Статус: {}. Тело ответа: {}",
                    ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
            throw new ProfileServiceException("Ошибка получения профилей: " + ex.getResponseBodyAsString());
        } catch (RestClientException ex) {
            log.error("Ошибка вызова сервиса для получения профилей", ex);
            throw new ProfileServiceException("Ошибка получения профилей: " + ex.getMessage());
        }

    }

    /**
     * Отправка запроса на верификацию профиля
     *
     * @param profileId - ID профиля
     */
    @Override
    public void verifyProfile(String profileId) {
        String pathApi = appConfig.getPaths().getProfile().get("verify-profile");
        String urlWithId = String.format("%s/%s", pathApi, profileId);
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(urlWithId, HttpMethod.GET, requestEntity, Void.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("Ошибка верификации профиля с id {}. Статус: {}. Тело ответа: {}",
                    profileId, ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
            throw new ProfileServiceException("Ошибка верификации профиля: " + ex.getResponseBodyAsString(), ex);
        } catch (RestClientException ex) {
            log.error("Ошибка вызова сервиса для верификации профиля с id {}", profileId, ex);
            throw new ProfileServiceException("Ошибка верификации профиля: " + ex.getMessage(), ex);
        }
    }

    /**
     * Отправка запроса на отмену верификации профиля
     *
     * @param profileDto - некорректные данные профиля
     */
    @Override
    public void cancelProfile(ProfileCancel profileDto) {
        String pathApi = appConfig.getPaths().getProfile().get("cancel-validation");
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<ProfileCancel> requestEntity = new HttpEntity<>(profileDto, headers);

        try {
            restTemplate.exchange(pathApi, HttpMethod.POST, requestEntity, Void.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("Ошибка обращения к сервису профилей. Статус: {}. Тело ответа: {}", ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
            throw new ProfileServiceException("Ошибка обращения в сервис профилей: " + ex.getResponseBodyAsString());
        } catch (RestClientException ex) {
            log.error("Ошибка вызова сервиса профилей", ex);
            throw new ProfileServiceException("Ошибка обращения в сервис профилей: " + ex.getMessage());
        }
    }


}
