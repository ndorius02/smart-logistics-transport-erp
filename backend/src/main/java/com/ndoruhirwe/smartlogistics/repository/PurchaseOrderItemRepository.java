package com.ndoruhirwe.smartlogistics.repository;

import com.ndoruhirwe.smartlogistics.entity.PurchaseOrderItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, UUID> {

    boolean existsByPurchaseOrderIdAndProductId(UUID purchaseOrderId, UUID productId);

    @EntityGraph(attributePaths = {"product"})
    List<PurchaseOrderItem> findByPurchaseOrderId(UUID purchaseOrderId);

    @EntityGraph(attributePaths = {"product", "purchaseOrder"})
    Optional<PurchaseOrderItem> findByIdAndPurchaseOrderId(UUID id, UUID purchaseOrderId);

    long countByPurchaseOrderId(UUID purchaseOrderId);
}
