package com.example.mapper;

import com.example.model.Profile;
import com.example.model.dto.ProfileDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDTO toDto(Profile profile);

    Profile toEntity(ProfileDTO profileDTO);

    List<ProfileDTO> toDto(List<Profile> profiles);

    List<Profile> toEntity(List<ProfileDTO> profileDTOs);

}
