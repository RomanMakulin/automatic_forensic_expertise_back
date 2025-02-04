package com.example.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileFullDTO {

    private UUID id;
    private String photo;
    private String phone;

    private UUID userId;
    private String fullName;
    private String email;
    private LocalDateTime registrationDate;
    private UUID roleId;
    private String roleName;

    private UUID locationId;
    private String country;
    private String region;
    private String city;
    private String address;

    private UUID statusId;
    private String statusName;
    private String verificationResult;
    private String activityStatus;

    private List<FileDTO> files;
    private Set<DirectionDTO> directions;

}
