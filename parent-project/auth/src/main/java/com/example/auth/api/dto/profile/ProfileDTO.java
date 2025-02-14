package com.example.auth.api.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileDTO {

    private String id;

    private String phone;

    private String photo;

    private String passport;

    private String diplom;

    private LocationDTO locationDTO = new LocationDTO();

    private StatusDTO statusDTO  = new StatusDTO();

    private Set<FileDTO> files;

    private Set<DirectionDTO> directions;

}
