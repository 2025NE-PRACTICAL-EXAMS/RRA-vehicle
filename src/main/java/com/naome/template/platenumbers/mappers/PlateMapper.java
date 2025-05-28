package com.naome.template.platenumbers.mappers;

import com.naome.template.platenumbers.PlateNumber;
import com.naome.template.platenumbers.dto.PlateResponseDTO;
import com.naome.template.platenumbers.dto.RegisterPlateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PlateMapper {
    @Mapping(source = "issuedDate", target = "issuedDate", dateFormat = "yyyy-MM-dd")
    PlateResponseDTO toResponse(PlateNumber plate);

    @Mapping(target = "issuedDate", source = "issueDate")
    PlateNumber toEntity(RegisterPlateRequest request);
    void updateEntityFromDto(RegisterPlateRequest request, @MappingTarget PlateNumber plate);

}