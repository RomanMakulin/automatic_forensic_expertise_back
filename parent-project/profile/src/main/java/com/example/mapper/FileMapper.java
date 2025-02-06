package com.example.mapper;

import com.example.model.File;
import com.example.model.dto.FileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMapper {

    @Mapping(source = "uploadDate", target = "cratedAt") // Соответствие полей
    FileDTO toDto(File file);

    @Mapping(source = "cratedAt", target = "uploadDate") // Обратное соответствие
    @Mapping(target = "profile", ignore = true) // Игнорируем поле profile, так как оно не в DTO
    File toEntity(FileDTO dto);

}
