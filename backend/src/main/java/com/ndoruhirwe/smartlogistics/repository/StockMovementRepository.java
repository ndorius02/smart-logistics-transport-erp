package com.ndoruhirwe.smartlogistics.repository;

import com.ndoruhirwe.smartlogistics.entity.StockMovement;
import com.ndoruhirwe.smartlogistics.entity.enums.StockMovementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {

    Optional<StockMovement> findByReferenceIgnoreCase(String reference);

    @EntityGraph(attributePaths = {"product", "warehouse"})
    Optional<StockMovement> findById(UUID id);

    @EntityGraph(attributePaths = {"product", "warehouse"})
    Page<StockMovement> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"product", "warehouse"})
    Page<StockMovement> findByProductId(UUID productId, Pageable pageable);

    @EntityGraph(attributePaths = {"product", "warehouse"})
    Page<StockMovement> findByWarehouseId(UUID warehouseId, Pageable pageable);

    @EntityGraph(attributePaths = {"product", "warehouse"})
    Page<StockMovement> findByProductIdAndWarehouseId(UUID productId, UUID warehouseId, Pageable pageable);

    @EntityGraph(attributePaths = {"product", "warehouse"})
    Page<StockMovement> findByMovementType(StockMovementType movementType, Pageable pageable);

}
