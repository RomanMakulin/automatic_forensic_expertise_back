package com.example.adminservice.api.rest;

import com.example.adminservice.api.dto.profile.ProfileDto;
import com.example.adminservice.api.dto.profileCancel.ProfileCancel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления профайлами
 */
@RestController
@RequestMapping("/api/administration")
public class AdminController {

    /**
     * Возвращает список всех профилей
     *
     * @return список профилей
     */
    @GetMapping("/get-all-profiles")
    public ResponseEntity<List<ProfileDto>> getAllProfiles() {
        // TODO logic
    }

    /**
     * Возвращает список всех профилей, которые не прошли проверку администратором
     *
     * @return список профилей
     */
    @GetMapping("/get-unverified-profiles")
    public ResponseEntity<List<ProfileDto>> getUnverifiedProfiles() {
        // TODO logic
    }

    /**
     * Подтверждает профиль пользователя
     *
     * @param profileId идентификатор профиля
     */
    @GetMapping("/validate-profile/{profileId}")
    public ResponseEntity<Void> validateProfile(@PathVariable("profileId") String profileId) {
        // TODO logic
    }

    /**
     * Отменяет подтверждение профиля пользователя
     *
     * @param profileCancel dto с неподходящими данными
     */
    @PostMapping("/cancel-validation")
    public ResponseEntity<Void> cancelValidationProfile(@RequestBody ProfileCancel profileCancel) {
        // TODO logic
    }

}
