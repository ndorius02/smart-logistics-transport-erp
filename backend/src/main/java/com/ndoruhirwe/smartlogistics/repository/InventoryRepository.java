package com.ndoruhirwe.smartlogistics.repository;

import com.ndoruhirwe.smartlogistics.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    boolean existsByProductIdAndWarehouseId(UUID productId, UUID warehouseId);

    @EntityGraph(attributePaths = {"product", "warehouse"})
    Optional<Inventory> findById(UUID id);

    @EntityGraph(attributePaths = {"product", "warehouse"})
    Page<Inventory> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"product", "warehouse"})
    Page<Inventory> findByWarehouseId(UUID warehouseId, Pageable pageable);

    @EntityGraph(attributePaths = {"product", "warehouse"})
    Page<Inventory> findByProductId(UUID productId, Pageable pageable);

    @EntityGraph(attributePaths = {"product", "warehouse"})
    Optional<Inventory> findByProductIdAndWarehouseId(UUID productId, UUID warehouseId);

    @Query("""
        SELECT i
        FROM Inventory i
        WHERE (i.quantity - i.reservedQuantity) <= i.minimumStockLevel
        """)
    @EntityGraph(attributePaths = {"product", "warehouse"})
    Page<Inventory> findLowStock(Pageable pageable);

}
