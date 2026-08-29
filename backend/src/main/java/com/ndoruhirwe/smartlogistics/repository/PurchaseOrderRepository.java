package com.ndoruhirwe.smartlogistics.repository;

import com.ndoruhirwe.smartlogistics.entity.PurchaseOrder;
import com.ndoruhirwe.smartlogistics.entity.enums.PurchaseOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID>  {

    boolean existsByOrderNumberIgnoreCase(String orderNumber);

    @EntityGraph(attributePaths = {"supplier", "warehouse"})
    Optional<PurchaseOrder> findById(UUID id);

    @EntityGraph(attributePaths = {"supplier", "warehouse"})
    Page<PurchaseOrder> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"supplier", "warehouse"})
    Page<PurchaseOrder> findByStatus(PurchaseOrderStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"supplier", "warehouse"})
    Page<PurchaseOrder> findBySupplierId(UUID supplierId, Pageable pageable);

    @EntityGraph(attributePaths = {"supplier", "warehouse"})
    Page<PurchaseOrder> findByWarehouseId(UUID warehouseId, Pageable pageable);

    @EntityGraph(attributePaths = {"supplier", "warehouse"})
    Optional<PurchaseOrder> findByOrderNumberIgnoreCase(String orderNumber);
}
