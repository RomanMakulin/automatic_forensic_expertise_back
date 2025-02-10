package com.example.service;

import com.example.model.AppUser;
import com.example.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public Optional<AppUser> getAppUserByEmail(String email) {
        return appUserRepository.getAppUsersByEmail(email);
    }

    public Optional<AppUser> getAppUserById(UUID id) {
        return appUserRepository.findById(id);
    }

    /**
     * Найти всех пользователей с ролью "Admin"
     *
     * @return список пользователей - администр
     */
    public List<AppUser> getAdmins() {
        return appUserRepository.findAdmins();
    }

}
