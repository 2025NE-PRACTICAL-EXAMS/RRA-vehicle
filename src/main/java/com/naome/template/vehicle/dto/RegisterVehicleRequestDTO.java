package com.naome.template.vehicle.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegisterVehicleRequestDTO(
        @NotBlank(message = "Chassis number is required")
        String chassisNumber,

        @NotBlank(message = "Manufacturer company is required")
        String manufacturerCompany,

        @Min(1900)
        @NotNull(message = "Manufacture year is required")
        Integer manufactureYear,

        @NotNull(message = "Price is required")
        Double price,

        @NotBlank(message = "Model name is required")
        String modelName,

        @NotNull(message = "Owner ID is required")
        UUID currentOwnerId,

        @NotNull(message = "Plate number ID is required")
        UUID plateNumberId
) {
}
