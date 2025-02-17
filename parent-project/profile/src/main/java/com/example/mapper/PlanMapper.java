package com.example.mapper;

import com.example.model.Plan;
import com.example.model.dto.PlanDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlanMapper {

    Plan toEntity(PlanDTO planDTO);

    PlanDTO toDTO(Plan plan);

}
