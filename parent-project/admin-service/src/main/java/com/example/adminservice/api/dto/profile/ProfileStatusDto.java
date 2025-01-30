package com.example.adminservice.api.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfileStatusDto {

    private UUID id;

    @JsonProperty
    private String verificationResult;

    @JsonProperty
    private String activityStatus;
}
