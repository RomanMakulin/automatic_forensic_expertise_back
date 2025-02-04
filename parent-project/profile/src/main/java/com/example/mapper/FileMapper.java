package com.example.mapper;

import com.example.model.File;
import com.example.model.dto.FileDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileDTO toDto(File file);
    File toEntity(FileDTO dto);
}
