package com.naome.template.history;

import com.naome.template.commons.validation.ValidPlateNumber;
import com.naome.template.history.dto.OwnershipHistoryDTO;
import com.naome.template.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class VehicleHistoryController {
    private final HistoryRepository historyRepository;
    private final VehicleRepository vehicleRepository;

    @GetMapping("/{vehicleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<OwnershipHistoryDTO>> getVehicleHistory(
            @PathVariable UUID vehicleId) {
        return ResponseEntity.ok(
                historyRepository.findByVehicleId(vehicleId).stream()
                        .map(OwnershipHistoryDTO::fromEntity)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<OwnershipHistoryDTO>> searchHistory(
            @ValidPlateNumber @RequestParam(required = false) String oldPlateNumber,
            @ValidPlateNumber @RequestParam(required = false) String newPlateNumber,
            @RequestParam(required = false) UUID vehicleId
    ) {
        if (oldPlateNumber == null && newPlateNumber == null && vehicleId == null) {
            return ResponseEntity.badRequest().build(); // Or include a message
        }

        return ResponseEntity.ok(
                historyRepository.findByOldPlateNumberOrNewPlateNumberOrVehicleId(
                                oldPlateNumber, newPlateNumber, vehicleId
                        ).stream()
                        .map(OwnershipHistoryDTO::fromEntity)
                        .collect(Collectors.toList())
        );
    }
}
