package com.naome.template.vehicle;

import com.naome.template.history.HistoryRepository;
import com.naome.template.history.VehicleOwnershipHistory;
import com.naome.template.vehicle.dto.RegisterVehicleRequestDTO;
import com.naome.template.vehicle.dto.VehicleResponseDTO;
import com.naome.template.vehicle.dto.VehicleTransferRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    private final HistoryRepository historyRepository;

    //ROLE_ADMIN,
    // Register a new vehicle
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<VehicleResponseDTO> registerVehicle(
            @Valid @RequestBody RegisterVehicleRequestDTO requestDTO
    ) {
        VehicleResponseDTO responseDTO = vehicleService.registerVehicle(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // Get a vehicle by ID
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> getVehicleById(@PathVariable UUID id) {
        VehicleResponseDTO vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }

    // Get all vehicles
    @GetMapping
    public ResponseEntity<List<VehicleResponseDTO>> getAllVehicles() {
        List<VehicleResponseDTO> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferVehicle(@RequestBody @Valid VehicleTransferRequest request) {
        vehicleService.transferVehicle(request);
        return ResponseEntity.ok("Vehicle transferred successfully");
    }

}
