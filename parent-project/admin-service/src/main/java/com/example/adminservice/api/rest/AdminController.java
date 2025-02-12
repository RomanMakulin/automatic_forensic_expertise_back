package com.example.adminservice.api.rest;

import com.example.adminservice.api.dto.profileCancel.ProfileCancel;
import com.example.adminservice.exceptions.ProfileServiceException;
import com.example.adminservice.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления профайлами
 */
@RestController
@RequestMapping("/api/administration")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Возвращает список всех профилей
     *
     * @return список профилей
     */
    @GetMapping("/get-all-profiles")
    public ResponseEntity<?> getAllProfiles() {
        try {
            return ResponseEntity.ok(adminService.getAllProfiles());
        } catch (ProfileServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while getting unverified profiles: " + e.getMessage());
        }
    }

    /**
     * Возвращает список всех профилей, которые не прошли проверку администратором
     *
     * @return список профилей
     */
    @GetMapping("/get-unverified-profiles")
    public ResponseEntity<?> getUnverifiedProfiles() {
        try {
            return ResponseEntity.ok(adminService.getUnverifiedProfiles());
        } catch (ProfileServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while getting unverified profiles: " + e.getMessage());
        }
    }

    /**
     * Подтверждает профиль пользователя
     *
     * @param profileId идентификатор профиля
     */
    @GetMapping("/validate-profile/{profileId}")
    public ResponseEntity<?> validateProfile(@PathVariable("profileId") String profileId) {
        try {
            adminService.verifyProfile(profileId);
            return ResponseEntity.ok().build();
        } catch (ProfileServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Validate failed: " + e.getMessage());
        }
    }

    /**
     * Отменяет подтверждение профиля пользователя
     *
     * @param profileCancel dto с неподходящими данными
     */
    @PostMapping("/cancel-validation")
    public ResponseEntity<?> cancelValidationProfile(@Valid @RequestBody ProfileCancel profileCancel) {
        try {
            adminService.cancelValidationProfile(profileCancel);
            return ResponseEntity.ok().build();
        } catch (ProfileServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Cancel validation failed: " + e.getMessage());
        }
    }

}
