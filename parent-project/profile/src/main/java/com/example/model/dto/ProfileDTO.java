package com.example.model.dto;

import com.example.model.*;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class ProfileDTO {

    private UUID id;

    private AppUser appUser;

    private String photo;

    private String phone;

    private Location location;

    private Status status;

    private Set<File> files;

    private Set<Direction> directions;

}
