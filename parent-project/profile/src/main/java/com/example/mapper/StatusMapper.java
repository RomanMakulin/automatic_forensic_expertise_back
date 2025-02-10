package com.example.mapper;

import com.example.model.Status;
import com.example.model.dto.StatusDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatusMapper {

    StatusDTO toDto(Status status);

    Status toEntity(StatusDTO status);

}
