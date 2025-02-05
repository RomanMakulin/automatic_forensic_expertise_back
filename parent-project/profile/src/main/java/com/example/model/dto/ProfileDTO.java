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

    private String phone;

    private LocationDTO locationDTO;

    private StatusDTO statusDTO;

    private Set<File> files;

    private Set<Direction> directions;

}
