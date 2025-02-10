package com.example.mapper;

import com.example.model.Profile;
import com.example.model.dto.ProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(source = "location", target = "locationDTO")
    @Mapping(source = "status", target = "statusDTO")
    ProfileDTO toDto(Profile profile);

    @Mapping(source = "locationDTO", target = "location")
    @Mapping(source = "statusDTO", target = "status")
    Profile toEntity(ProfileDTO profileDTO);

    List<ProfileDTO> toDto(List<Profile> profiles);

    List<Profile> toEntity(List<ProfileDTO> profileDTOs);

}
