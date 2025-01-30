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
public class UserDto {

    private UUID id;

    @JsonProperty
    private String fullName;

    private String email;

    @JsonProperty
    private LocalDateTime registrationDate;

    @JsonProperty
    private boolean verificationEmail;

    private RoleDto role;

    @JsonProperty
    private String keycloakId;

}

