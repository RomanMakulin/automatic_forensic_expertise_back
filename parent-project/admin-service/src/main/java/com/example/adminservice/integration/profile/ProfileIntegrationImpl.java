package com.example.adminservice.integration.profile;

import com.example.adminservice.api.dto.profile.original.OriginalProfileDto;
import com.example.adminservice.api.dto.profileCancel.ProfileCancel;
import com.example.adminservice.config.AppConfig;
import com.example.adminservice.exceptions.ProfileServiceException;
import com.example.adminservice.integration.IntegrationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Реализация сервиса для взаимодействия с модулем профилей
 */
@Service
public class ProfileIntegrationImpl implements ProfileIntegration {

    private final IntegrationHelper integrationHelper;
    private final AppConfig appConfig;

    public ProfileIntegrationImpl(IntegrationHelper integrationHelper,
                                  AppConfig appConfig) {
        this.integrationHelper = integrationHelper;
        this.appConfig = appConfig;
    }

    /**
     * Отправка запроса на получение всех профилей
     *
     * @return - список профилей
     */
    @Override
    public List<OriginalProfileDto> requestForAllProfiles() {
        return getProfilesProcess("get-all-profiles");
    }

    /**
     * Отправка запроса на получение непроверенных профилей
     *
     * @return - список профилей
     */
    @Override
    public List<OriginalProfileDto> requestForUnverifiedProfiles() {
        return getProfilesProcess("get-unverified-profiles");
    }

    /**
     * Общая реализация отправки запроса на получение профилей
     *
     * @param passKey - ключ урла для запроса из проперти
     * @return - список профилей
     */
    private List<OriginalProfileDto> getProfilesProcess(String passKey) {
        String pathApi = appConfig.getPaths().getProfile().get(passKey);
        return integrationHelper.simpleGetRequest(
                pathApi,
                new ParameterizedTypeReference<List<OriginalProfileDto>>() {
                }
        );
    }

    /**
     * Отправка запроса на верификацию профиля
     *
     * @param profileId - ID профиля
     */
    @Override
    public void requestForVerifyProfile(String profileId) {
        String pathApi = appConfig.getPaths().getProfile().get("verify-profile");
        String urlWithId = String.format("%s/%s", pathApi, profileId);

        integrationHelper.simpleGetRequest(urlWithId, Void.class);
    }

    /**
     * Отправка запроса на отмену верификации профиля
     *
     * @param profileDto - некорректные данные профиля
     */
    @Override
    public void requestForCancelVerifyProfile(ProfileCancel profileDto) {
        String pathApi = appConfig.getPaths().getProfile().get("cancel-validation");

        integrationHelper.simplePostRequest(pathApi, profileDto, Void.class);
    }

}
