package com.naome.template.owner;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OwnerRepository extends JpaRepository<Owner, UUID> {
    boolean existsByNationalIdOrPhoneNumber(String nationalId, String phoneNumber);
    List<Owner> findByNationalId(String nationalId);
    // Find owners by Phone Number
    List<Owner> findByPhoneNumber(String phoneNumber);
}
