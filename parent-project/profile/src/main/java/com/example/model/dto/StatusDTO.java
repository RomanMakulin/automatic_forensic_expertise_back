package com.example.model.dto;

import com.example.model.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
public class StatusDTO {

    private UUID id;

    private String name = "Создан";

    private String verificationResult;

    private String activityStatus;

}
