package com.naome.template.owner.mappers;

import com.naome.template.owner.Owner;
import com.naome.template.owner.dto.RegisterOwnerRequestDTO;
import com.naome.template.owner.dto.OwnerResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface OwnerMapper {

    @Mapping(target = "fullNames", source = "fullNames")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    Owner toEntity(RegisterOwnerRequestDTO request);
    OwnerResponseDTO toResponse(Owner owner);
    void updateOwnerFromDto(RegisterOwnerRequestDTO dto, @MappingTarget Owner owner);

}
