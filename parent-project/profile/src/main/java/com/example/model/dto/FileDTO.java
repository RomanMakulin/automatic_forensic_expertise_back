package com.example.model.dto;

import com.example.model.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileDTO {

    /**
     * Идентификатор файла
     */
    private UUID id;

    /**
     * Имя файла
     */
    private String path;

    /**
     * Дата создания файла
     */
    private LocalDateTime cratedAt;

}
