package com.ndoruhirwe.smartlogistics.repository;

import com.ndoruhirwe.smartlogistics.entity.Transport;
import com.ndoruhirwe.smartlogistics.entity.enums.TransportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransportRepository extends JpaRepository<Transport, UUID> {

    Optional<Transport> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);
    @Override
    @EntityGraph(attributePaths = {
            "originWarehouse",
            "destinationWarehouse",
            "vehicle",
            "driver"
    })
    Optional<Transport> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {
            "originWarehouse",
            "destinationWarehouse",
            "vehicle",
            "driver"
    })
    Page<Transport> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {
            "originWarehouse",
            "destinationWarehouse",
            "vehicle",
            "driver"
    })
    Page<Transport> findByCodeContainingIgnoreCase(String code, Pageable pageable);

    @EntityGraph(attributePaths = {
            "originWarehouse",
            "destinationWarehouse",
            "vehicle",
            "driver"
    })
    Page<Transport> findByStatus(TransportStatus status, Pageable pageable);
}
