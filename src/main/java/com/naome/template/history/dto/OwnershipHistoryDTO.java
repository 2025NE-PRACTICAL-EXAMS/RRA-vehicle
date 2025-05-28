package com.naome.template.history.dto;

import com.naome.template.history.VehicleOwnershipHistory;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record OwnershipHistoryDTO(
        UUID historyId,
        String vehicleChassisNumber,
        String fromOwnerName,
        String fromOwnerNationalId,
        String toOwnerName,
        String toOwnerNationalId,
        String oldPlateNumber,
        String newPlateNumber,
        Date transferDate,
        Double purchasePrice
) {
    public static OwnershipHistoryDTO fromEntity(VehicleOwnershipHistory history) {
        return new OwnershipHistoryDTO(
                history.getId(),
                history.getVehicle().getChassisNumber(),
                history.getFromOwner().getFullNames(),
                history.getFromOwner().getNationalId(),
                history.getToOwner().getFullNames(),
                history.getToOwner().getNationalId(),
                history.getOldPlateNumber(),
                history.getNewPlateNumber(),
                history.getTransferDate(),
                history.getPurchasePrice()
        );
    }
}