package com.ndoruhirwe.smartlogistics.repository;

import com.ndoruhirwe.smartlogistics.entity.Vehicle;
import com.ndoruhirwe.smartlogistics.entity.enums.VehicleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    Optional<Vehicle> findByRegistrationNumberIgnoreCase(String registrationNumber);

    boolean existsByRegistrationNumberIgnoreCase(String registrationNumber);

    Page<Vehicle> findByRegistrationNumberContainingIgnoreCase(String registrationNumber, Pageable pageable);

    Page<Vehicle> findByBrandContainingIgnoreCase(String brand, Pageable pageable);

    Page<Vehicle> findByOperationalStatus(VehicleStatus operationalStatus, Pageable pageable);

    Page<Vehicle> findByActive(boolean active, Pageable pageable);
}
