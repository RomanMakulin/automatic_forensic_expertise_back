package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "status")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String name = "Создан";

    @Column(name = "verification_result")
    private String verificationResult = "Профиль на верификации";

    @Column(name = "activity_status")
    private String activityStatus = "Профиль ждет активации";

    private String description = "Профиль на проверке у админа";

}
