package com.example.model.dto;

import com.example.model.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AppUserDTO {

    private UUID id;

    private String fullName;

    private String email;

    private LocalDateTime registrationDate;

    private RoleDTO role;


}
