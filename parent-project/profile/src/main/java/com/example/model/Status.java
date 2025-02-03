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

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_result")
    private VerificationResult verificationResult = VerificationResult.NEED_VERIFY;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_status")
    private ActivityStatus activityStatus = ActivityStatus.ZALYPA;


    public enum VerificationResult {

        NEED_VERIFY("Профиль на верификации"),
        NEED_REMAKE("Требуется доработка"),
        APPROVED("Профиль подтвержден");

        private final String description;

        VerificationResult(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum ActivityStatus {

        XYI, ZALYPA

    }

}
