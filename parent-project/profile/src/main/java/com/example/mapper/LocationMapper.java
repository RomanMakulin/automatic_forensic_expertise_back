package com.example.mapper;

import com.example.model.Location;
import com.example.model.dto.LocationDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationDTO toDto(Location location);

    Location toEntity(LocationDTO locationDTO);

}
