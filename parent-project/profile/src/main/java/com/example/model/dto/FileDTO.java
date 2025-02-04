package com.example.model.dto;

import com.example.model.Profile;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FileDTO {

    private Profile profile;

    private String description;

    private String type;

    private String path;

    private LocalDateTime uploadDate;

}
