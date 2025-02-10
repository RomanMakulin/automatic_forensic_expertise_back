package com.example.model.dto;

import com.example.model.Status;
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

    private String verificationResult;

    private String activityStatus;

}
