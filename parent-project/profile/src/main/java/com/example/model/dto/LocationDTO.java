package com.example.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LocationDTO {

    @NotBlank(message = "Страна не может быть пустой")
    private String country;

    @NotBlank(message = "Регион не может быть пустой")
    private String region;

    @NotBlank(message = "Город не может быть пустой")
    private String city;

    @NotBlank(message = "адрес не может быть пустой")
    private String address;

}
