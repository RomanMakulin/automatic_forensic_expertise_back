package com.example.auth.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity()
@Table(name="appuser")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="full_name")
    private String fullName;

    private String email;

    @Column(name="registration_date")
    private LocalDateTime registrationDate;

    @OneToOne(cascade=CascadeType.ALL)
    private Role role;

    private String keycloakId;

    public User(UUID id, String fullName, String email, LocalDateTime registrationDate, Role role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.registrationDate = registrationDate;
        this.role = role;
    }

    public User() {
    }

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
