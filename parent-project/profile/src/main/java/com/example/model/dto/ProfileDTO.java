package com.example.model.dto;

import com.example.model.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileDTO {

    private AppUser appUser;

    private String phone;

    private LocationDTO locationDTO;

    private StatusDTO statusDTO;

    private Set<File> files;

    private Set<Direction> directions;

}
