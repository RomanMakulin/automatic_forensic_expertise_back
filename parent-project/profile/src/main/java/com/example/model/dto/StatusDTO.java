package com.example.model.dto;

import com.example.model.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StatusDTO {

    private String name = "Создан";

    @JsonProperty("verification_result")
    private String verificationResult;

    @JsonProperty("activity_status")
    private String activityStatus;

}
