package com.example.mapper;

import com.example.model.Direction;
import com.example.model.dto.DirectionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DirectionMapper {

    DirectionDTO toDTO(Direction direction);

    @Mapping(target = "id", ignore = true) // Игнорируем id при маппинге File -> FileDTO
    Direction toEntity(DirectionDTO directionDTO);

}
