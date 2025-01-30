package com.example.auth.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity()
@Table(name="app_user")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="full_name")
    private String fullName;

    private String email;

    @Column(name="registration_date")
    private LocalDateTime registrationDate;

    @Column(name="verification_email")
    private boolean verificationEmail;

    @OneToOne(cascade=CascadeType.ALL)
    private Role role;

    private String keycloakId;

}
