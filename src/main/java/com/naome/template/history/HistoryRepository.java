package com.naome.template.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface HistoryRepository extends JpaRepository<VehicleOwnershipHistory, UUID> {

    List<VehicleOwnershipHistory> findByVehicleId(UUID vehicleId);

    // ✅ Custom query to search by plate number or chassis number

    List<VehicleOwnershipHistory> findByOldPlateNumberOrNewPlateNumberOrVehicleId(
            String oldPlateNumber, String newPlateNumber,UUID vehicleId);
}
