package com.naome.template.vehicle;

import com.naome.template.commons.exceptions.BadRequestException;
import com.naome.template.commons.exceptions.ResourceNotFoundException;
import com.naome.template.history.HistoryRepository;
import com.naome.template.history.VehicleOwnershipHistory;
import com.naome.template.owner.Owner;
import com.naome.template.owner.OwnerRepository;
import com.naome.template.platenumbers.PlateNumber;
import com.naome.template.platenumbers.PlateRepository;
import com.naome.template.platenumbers.PlateService;
import com.naome.template.platenumbers.PlateStatus;
import com.naome.template.vehicle.dto.RegisterVehicleRequestDTO;
import com.naome.template.vehicle.dto.VehicleResponseDTO;
import com.naome.template.vehicle.dto.VehicleTransferRequest;
import com.naome.template.vehicle.mappers.VehicleMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final OwnerRepository ownerRepository;
    private final VehicleMapper vehicleMapper;
    private final PlateRepository plateRepository;
    private final PlateService plateService;
    private final HistoryRepository historyRepository;

    public VehicleResponseDTO registerVehicle(RegisterVehicleRequestDTO request) {
        var owner = ownerRepository.findById(request.currentOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        var plate = plateRepository.findById(request.plateNumberId())
                .orElseThrow(() -> new ResourceNotFoundException("Plate number not found"));

        Vehicle vehicle = vehicleMapper.toEntity(request);
        vehicle.setCurrentOwner(owner);
        vehicle.setPlateNumber(plate);

        vehicleRepository.save(vehicle);

        // Update plate number assignment after vehicle is saved
        plateService.assignPlateToVehicle(plate.getId(), vehicle);

        log.info("Vehicle registered: Vehicle ID = {}, Owner ID = {}, Plate = {}",
                vehicle.getId(), owner.getId(), plate.getPlateNumber());

        return vehicleMapper.toResponse(vehicle);
    }

    public VehicleResponseDTO getVehicleById(UUID id) {
        var vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        log.info("Fetched vehicle by ID: {}", id);
        return vehicleMapper.toResponse(vehicle);
    }

    public List<VehicleResponseDTO> getAllVehicles() {
        log.info("Fetching all vehicles");
        return vehicleRepository.findAll().stream()
                .map(vehicleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void transferVehicle(VehicleTransferRequest request) {
        Vehicle vehicle = vehicleRepository.findById(request.vehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        Owner currentOwner = vehicle.getCurrentOwner();
        if (currentOwner == null) {
            throw new BadRequestException("Vehicle does not have a current owner");
        }

        Owner newOwner = ownerRepository.findById(request.newOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("New owner not found"));

        // 🚫 Prevent transfer to the same owner
        if (currentOwner.getId().equals(newOwner.getId())) {
            throw new BadRequestException("Cannot transfer vehicle to the same owner");
        }

        // ✅ Get the currently assigned plate number from the vehicle itself
        PlateNumber currentPlate = vehicle.getPlateNumber();
        if (currentPlate == null) {
            throw new ResourceNotFoundException("Vehicle does not have a plate number assigned");
        }

        // 1. Release current plate
        currentPlate.setStatus(PlateStatus.AVAILABLE);
        currentPlate.setVehicle(null);
        currentPlate.setOwner(null);
        plateRepository.save(currentPlate);

        // 2. Find an available plate
        PlateNumber newPlate = plateRepository.findFirstByStatus(PlateStatus.AVAILABLE)
                .orElseThrow(() -> new ResourceNotFoundException("No available plate numbers in system"));

        // 3. Assign new plate
        newPlate.setStatus(PlateStatus.IN_USE);
        newPlate.setVehicle(vehicle);
        newPlate.setOwner(newOwner);
        plateRepository.save(newPlate);

        // 4. Save ownership history
        VehicleOwnershipHistory history = new VehicleOwnershipHistory();
        history.setVehicle(vehicle);
        history.setFromOwner(currentOwner);
        history.setToOwner(newOwner);
        history.setPurchasePrice(request.purchasePrice());
        history.setTransferDate(new Date());
        history.setOldPlateNumber(currentPlate.getPlateNumber());
        history.setNewPlateNumber(newPlate.getPlateNumber());
        historyRepository.save(history);

        // 5. Update vehicle
        vehicle.setCurrentOwner(newOwner);
        vehicle.setPlateNumber(newPlate);
        vehicleRepository.save(vehicle);

        log.info("Vehicle transferred: Vehicle ID = {}, From Owner ID = {}, To Owner ID = {}, " +
                        "Old Plate = {}, New Plate = {}, Price = {}",
                vehicle.getId(),
                history.getFromOwner().getId(),
                history.getToOwner().getId(),
                history.getOldPlateNumber(),
                history.getNewPlateNumber(),
                request.purchasePrice());
    }

    public List<Vehicle> searchByNationalId(String nationalId) {
        log.info("Searching for vehicles by national ID: {}", nationalId);
        return vehicleRepository.findByCurrentOwnerNationalId(nationalId);
    }
}
