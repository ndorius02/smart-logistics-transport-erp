package com.ndoruhirwe.smartlogistics.repository;

import com.ndoruhirwe.smartlogistics.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
    Optional<Supplier> findByCodeIgnoreCase(String code);

    Optional<Supplier> findByVatNumberIgnoreCase(String vatNumber);

    Page<Supplier> findByCompanyNameContainingIgnoreCase(String companyName, Pageable pageable);

    Page<Supplier> findByCodeContainingIgnoreCase(String code, Pageable pageable);
}

