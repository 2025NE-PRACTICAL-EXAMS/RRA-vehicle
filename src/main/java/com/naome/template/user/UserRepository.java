package com.naome.template.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmailOrNationalIdOrPhoneNumber(String email, String nationalId, String phoneNumber);
    Optional<User> findByEmail(String email);
}
