package com.naome.template.vehicle.mappers;

import com.naome.template.vehicle.Vehicle;
import com.naome.template.vehicle.dto.RegisterVehicleRequestDTO;
import com.naome.template.vehicle.dto.VehicleResponseDTO;
import com.naome.template.platenumbers.PlateNumber;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface VehicleMapper {

    @Mapping(source = "currentOwner.id", target = "currentOwnerId")
    @Mapping(source = "plateNumber.id", target = "plateNumber")
    VehicleResponseDTO toResponse(Vehicle vehicle);

    @Mapping(target = "currentOwner", ignore = true)
    @Mapping(target = "plateNumber", ignore = true)
    Vehicle toEntity(RegisterVehicleRequestDTO dto);

}
