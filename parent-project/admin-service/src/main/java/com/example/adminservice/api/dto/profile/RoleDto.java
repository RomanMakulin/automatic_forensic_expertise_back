package com.example.adminservice.api.dto.profile;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    private UUID id;

    private String name;

}
