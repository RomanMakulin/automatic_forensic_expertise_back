package com.example.mapper;

import com.example.model.Direction;
import com.example.model.dto.DirectionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DirectionMapper {

    DirectionDTO toDTO(Direction direction);

    Direction toEntity(DirectionDTO directionDTO);

}
