package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "file")
public class File {

    @Id
    private UUID id = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    private String path;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

}
