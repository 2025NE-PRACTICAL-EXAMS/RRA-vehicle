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
}
 
