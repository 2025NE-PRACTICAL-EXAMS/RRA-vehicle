package com.naome.template.platenumbers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlateRepository extends JpaRepository<PlateNumber, UUID> {
    List<PlateNumber> findByOwnerId(UUID ownerId);

    Optional<PlateNumber> findByPlateNumber(String plateNumber);

    Optional<PlateNumber> findFirstByOwnerIdAndStatus(UUID ownerId, PlateStatus status);

    Optional<PlateNumber> findByVehicleId(UUID vehicleId);

    Optional<PlateNumber> findFirstByStatus(PlateStatus plateStatus);
}