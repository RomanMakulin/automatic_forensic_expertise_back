package com.example.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.Set;

@Data
public class ProfileCreateDTO {

    @NotBlank(message = "номер телефона не может быть пустым")
    private String phone;

    @NotNull
    private LocationDTO locationDTO;

    @NotNull
    private Set<DirectionDTO> directionDTOList;

}
