package com.naome.template.owner;

import com.naome.template.commons.exceptions.BadRequestException;
import com.naome.template.owner.dto.OwnerResponseDTO;
import com.naome.template.owner.dto.PlateNumberResponseDTO;
import com.naome.template.owner.dto.RegisterOwnerRequestDTO;
import com.naome.template.owner.mappers.OwnerMapper;
import com.naome.template.vehicle.Vehicle;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    public OwnerResponseDTO createOwner(RegisterOwnerRequestDTO ownerRequest) {
        log.info("Creating new owner with National ID: {} and Phone: {}", ownerRequest.nationalId(), ownerRequest.phoneNumber());

        if (ownerRepository.existsByNationalIdOrPhoneNumber(ownerRequest.nationalId(), ownerRequest.phoneNumber())) {
            log.warn("Owner already exists with National ID: {} or Phone: {}", ownerRequest.nationalId(), ownerRequest.phoneNumber());
            throw new BadRequestException("Owner with this National ID or Phone Number already exists");
        }

        Owner newOwner = ownerMapper.toEntity(ownerRequest);
        ownerRepository.save(newOwner);
        log.info("Owner created with ID: {}", newOwner.getId());

        return ownerMapper.toResponse(newOwner);
    }

    public List<OwnerResponseDTO> searchOwners(String nationalId, String phoneNumber) {
        log.info("Searching owners by National ID: {} or Phone: {}", nationalId, phoneNumber);
        List<Owner> owners;

        if (nationalId != null) {
            owners = ownerRepository.findByNationalId(nationalId);
        } else if (phoneNumber != null) {
            owners = ownerRepository.findByPhoneNumber(phoneNumber);
        } else {
            owners = ownerRepository.findAll();
        }

        log.info("Found {} owners", owners.size());
        return owners.stream()
                .map(owner -> new OwnerResponseDTO(
                        owner.getId(),
                        owner.getFullNames(),
                        owner.getNationalId(),
                        owner.getPhoneNumber(),
                        owner.getAddress()))
                .collect(Collectors.toList());
    }

    public List<PlateNumberResponseDTO> getPlateNumbersByOwner(UUID ownerId) {
        log.info("Fetching plate numbers for owner ID: {}", ownerId);

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> {
                    log.warn("Owner not found with ID: {}", ownerId);
                    return new BadRequestException("Owner not found");
                });

        List<Vehicle> plateNumbers = owner.getVehicles();
        log.info("Owner has {} vehicles", plateNumbers.size());

        return plateNumbers.stream()
                .map(vehicle -> new PlateNumberResponseDTO(vehicle.getPlateNumber().getPlateNumber()))
                .collect(Collectors.toList());
    }

    public List<OwnerResponseDTO> getAllOwners() {
        log.info("Fetching all owners");
        List<Owner> owners = ownerRepository.findAll();
        log.info("Total owners found: {}", owners.size());

        return owners.stream()
                .map(owner -> new OwnerResponseDTO(
                        owner.getId(),
                        owner.getFullNames(),
                        owner.getNationalId(),
                        owner.getPhoneNumber(),
                        owner.getAddress()))
                .collect(Collectors.toList());
    }
}