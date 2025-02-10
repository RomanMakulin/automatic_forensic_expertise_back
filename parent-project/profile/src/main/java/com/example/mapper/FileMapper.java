package com.example.mapper;

import com.example.model.File;
import com.example.model.dto.FileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMapper {

    FileDTO toDto(File file);

    @Mapping(target = "profile", ignore = true) // Игнорируем поле profile, так как оно не в DTO
    File toEntity(FileDTO dto);

}
