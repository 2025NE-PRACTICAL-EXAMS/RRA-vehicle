package com.naome.template.platenumbers;

import com.naome.template.commons.exceptions.ResourceNotFoundException;
import com.naome.template.platenumbers.dto.RegisterPlateRequest;
import com.naome.template.platenumbers.dto.PlateResponseDTO;
import com.naome.template.platenumbers.mappers.PlateMapper;
import com.naome.template.vehicle.Vehicle;
import com.naome.template.vehicle.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlateService {

    private final PlateRepository plateRepository;
    private final PlateMapper plateMapper;
    private final VehicleRepository vehicleRepository;

    public PlateResponseDTO registerPlate(RegisterPlateRequest request) {
        PlateNumber plate = plateMapper.toEntity(request);
        plate.setStatus(PlateStatus.AVAILABLE);
        plate.setOwner(null);
        plate.setVehicle(null);

        plateRepository.save(plate);
        return plateMapper.toResponse(plate);
    }

    public List<PlateResponseDTO> getAllPlates() {
        return plateRepository.findAll()
                .stream()
                .map(plateMapper::toResponse)
                .collect(Collectors.toList());
    }

    public PlateResponseDTO getPlateById(UUID id) {
        PlateNumber plate = plateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plate not found"));
        return plateMapper.toResponse(plate);
    }

    @Transactional
    public void assignPlateToVehicle(UUID plateId, Vehicle vehicle) {
        PlateNumber plate = plateRepository.findById(plateId)
                .orElseThrow(() -> new ResourceNotFoundException("Plate not found"));

        plate.setStatus(PlateStatus.IN_USE);
        plate.setVehicle(vehicle);
        plate.setOwner(vehicle.getCurrentOwner());

        plateRepository.save(plate);
    }

    public String deletePlateById(UUID plateId) {
        PlateNumber plate = plateRepository.findById(plateId)
                .orElseThrow(() -> new ResourceNotFoundException("Plate not found"));

        if (plate.getVehicle() != null) {
            throw new IllegalStateException("Cannot delete plate that is assigned to a vehicle.");
        }

        plateRepository.delete(plate);

        return plate.getPlateNumber() + "Plate has been deleted";
    }


    public String deleteAllPlatesWithNoVehicles() {
        List<PlateNumber> plates = plateRepository.findAll();

        List<PlateNumber> unassignedPlates = plates.stream()
                .filter(plate -> plate.getVehicle() == null)
                .toList();

        if (unassignedPlates.isEmpty()) {
            return "No unassigned plates to delete.";
        }

        plateRepository.deleteAll(unassignedPlates);
        return unassignedPlates.size() + " unassigned plates deleted successfully.";
    }


    @Transactional
    public PlateResponseDTO updatePlateNumber(UUID plateId, RegisterPlateRequest request) {
        boolean exists = plateRepository.existsById(plateId);
        if (!exists) {
            throw new ResourceNotFoundException("Plate not found");
        }

        PlateNumber plate = plateRepository.findById(plateId).get(); // safe to use get() here
        plateMapper.updateEntityFromDto(request, plate);

        PlateNumber updatedPlate = plateRepository.save(plate);
        return plateMapper.toResponse(updatedPlate);
    }




}
