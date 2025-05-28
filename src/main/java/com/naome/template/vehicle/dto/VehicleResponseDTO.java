package com.naome.template.vehicle.dto;

import java.util.UUID;

public record VehicleResponseDTO(
        UUID id,
        String chassisNumber,
        String manufacturerCompany,
        Integer manufactureYear,
        Double price,
        String modelName,
        UUID currentOwnerId,
        String plateNumber
) {
}
