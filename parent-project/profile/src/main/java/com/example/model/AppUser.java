package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "full_name")
    private String fullName = "fullName";

    private String email = "email" + String.valueOf(new Random().nextInt(10000));

    @Column(name = "registration_date")
    private LocalDateTime registrationDate = LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role = new Role();

    @Column(name = "keycloak_id")
    private String keycloakId = "keycloakId";

}
