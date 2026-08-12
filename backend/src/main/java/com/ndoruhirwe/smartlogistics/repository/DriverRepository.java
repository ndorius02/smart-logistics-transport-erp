package com.ndoruhirwe.smartlogistics.repository;

import com.ndoruhirwe.smartlogistics.entity.Driver;
import com.ndoruhirwe.smartlogistics.entity.enums.DriverStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DriverRepository extends JpaRepository<Driver, UUID> {
    Optional<Driver> findByLicenseNumberIgnoreCase(String licenseNumber);

    boolean existsByLicenseNumberIgnoreCase(String licenseNumber);

    Page<Driver> findByLicenseNumberContainingIgnoreCase(String licenseNumber, Pageable pageable);

    Page<Driver> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);

    Page<Driver> findByStatus(DriverStatus status, Pageable pageable);

    Page<Driver> findByActive(boolean active, Pageable pageable);
}
