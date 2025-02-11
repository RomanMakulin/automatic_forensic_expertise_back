package com.example.mapper;

import com.example.model.File;
import com.example.model.dto.FileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileMapper {

    @Mapping(source = "uploadDate", target = "createdAt") // Соответствие полей
    FileDTO toDto(File file);

    @Mapping(source = "createdAt", target = "uploadDate") // Обратное соответствие
    @Mapping(target = "profile", ignore = true) // Игнорируем поле profile, так как оно не в DTO
    File toEntity(FileDTO dto);

    @Mapping(source = "uploadDate", target = "createdAt")
    List<FileDTO> toDto(List<File> file);

    @Mapping(source = "createdAt", target = "uploadDate")
    @Mapping(target = "profile", ignore = true) // Игнорируем поле profile, так как оно не в DTO
    List<File> toEntity(List<FileDTO> dto);

}
