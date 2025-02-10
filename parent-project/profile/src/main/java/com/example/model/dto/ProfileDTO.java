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

    private String id;

    private AppUserDTO appUser;

    private String phone;

    private LocationDTO locationDTO = new LocationDTO();

    private StatusDTO statusDTO  = new StatusDTO();

    private Set<FileDTO> files;

    private Set<DirectionDTO> directions;

}
