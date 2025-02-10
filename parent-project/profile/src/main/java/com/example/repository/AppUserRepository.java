package com.example.repository;

import com.example.model.AppUser;
import com.example.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> getAppUsersByEmail(String email);

    /**
     * Найти всех пользователей с ролью "Admin"
     *
     * @return список пользователей - администраторов
     */
    @Query("SELECT u FROM AppUser u WHERE u.role.name = 'Admin'")
    List<AppUser> findAdmins();

}
