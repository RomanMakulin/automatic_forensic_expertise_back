package com.example.adminservice.api.dto.profile;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    private UUID id;

    private String country;

    private String region;

    private String city;

    private String address;

}
