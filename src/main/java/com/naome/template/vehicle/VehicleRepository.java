package com.naome.template.vehicle;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    Optional<Vehicle> findByChassisNumberOrPlateNumber_PlateNumber(String chassisNumber, String plateNumber);

    List<Vehicle> findByCurrentOwnerNationalId(String nationalId);
}
