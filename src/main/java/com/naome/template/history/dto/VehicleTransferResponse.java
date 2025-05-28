package com.naome.template.history.dto;

import com.naome.template.history.VehicleOwnershipHistory;

import java.util.Date;
import java.util.UUID;

public record VehicleTransferResponse(
        UUID id,
        UUID vehicleId,
        UUID fromOwnerId,
        UUID toOwnerId,
        String oldPlateNumber,
        String newPlateNumber,
        Date transferDate,
        Double purchasePrice
) {
    public static VehicleTransferResponse fromEntity(VehicleOwnershipHistory history) {
        return new VehicleTransferResponse(
                history.getId(),
                history.getVehicle().getId(),
                history.getFromOwner().getId(),
                history.getToOwner().getId(),
                history.getOldPlateNumber(),
                history.getNewPlateNumber(),
                history.getTransferDate(),
                history.getPurchasePrice()
        );
    }
}