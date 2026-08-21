package com.ndoruhirwe.smartlogistics.repository;

import com.ndoruhirwe.smartlogistics.entity.Carrier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CarrierRepository  extends JpaRepository<Carrier, UUID> {
    Optional<Carrier> findByCodeIgnoreCase(String code);

    Optional<Carrier> findByVatNumberIgnoreCase(String vatNumber);

    Optional<Carrier> findByLicenseNumberIgnoreCase(String licenseNumber);

    Page<Carrier> findByCompanyNameContainingIgnoreCase(String companyName, Pageable pageable);

    Page<Carrier> findByCodeContainingIgnoreCase(String code, Pageable pageable);

    Page<Carrier> findByLicenseNumberContainingIgnoreCase(String licenseNumber, Pageable pageable);
}
