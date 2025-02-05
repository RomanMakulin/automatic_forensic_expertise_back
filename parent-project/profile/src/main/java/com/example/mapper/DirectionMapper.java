package com.example.mapper;

import com.example.model.Direction;
import com.example.model.dto.DirectionDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface DirectionMapper {

    DirectionDTO toDTO(Direction direction);

//    @BeanMapping(ignoreByDefault = true)
    Direction toEntity(DirectionDTO directionDTO);

    Set<DirectionDTO> toDTO(Set<Direction> directions);

    Set<Direction> toEntity(Set<DirectionDTO> directionDTOs);

}
