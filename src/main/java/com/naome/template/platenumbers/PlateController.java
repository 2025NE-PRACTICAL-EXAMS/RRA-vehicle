package com.naome.template.platenumbers;

import com.naome.template.platenumbers.dto.PlateResponseDTO;
import com.naome.template.platenumbers.dto.RegisterPlateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/plates")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class PlateController {

    private final PlateService plateNumberService;

    // Register a new plate number (with no owner)
    @PostMapping
    public ResponseEntity<PlateResponseDTO> registerPlate(@Valid @RequestBody RegisterPlateRequest request) {
        PlateResponseDTO response = plateNumberService.registerPlate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PlateResponseDTO>> getAllPlates() {
        List<PlateResponseDTO> response = plateNumberService.getAllPlates();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlateResponseDTO> getPlateById(@PathVariable UUID id) {
        PlateResponseDTO response = plateNumberService.getPlateById(id);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePlateById(@PathVariable UUID id) {
        String response = plateNumberService.deletePlateById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<String> deletePlateWithNoVehicles() {
        String response = plateNumberService.deleteAllPlatesWithNoVehicles();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlateResponseDTO> updatePlate(
            @PathVariable UUID id,
            @Valid @RequestBody RegisterPlateRequest request) {
        PlateResponseDTO response = plateNumberService.updatePlateNumber(id, request);
        return ResponseEntity.ok(response);
    }



}
