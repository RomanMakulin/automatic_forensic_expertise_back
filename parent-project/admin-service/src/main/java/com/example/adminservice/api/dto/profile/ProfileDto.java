package com.example.adminservice.api.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {

    private UUID id;

    private UserDto user;

    private String photo;

    private String phone;

    private LocationDto location;

    @JsonProperty
    private ProfileStatusDto profileStatus;

    @JsonProperty
    private LocalDateTime lastLogin;

}
