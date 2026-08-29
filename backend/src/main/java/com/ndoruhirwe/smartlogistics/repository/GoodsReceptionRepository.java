package com.ndoruhirwe.smartlogistics.repository;

import com.ndoruhirwe.smartlogistics.entity.GoodsReception;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
public interface GoodsReceptionRepository extends JpaRepository<GoodsReception, UUID> {

    boolean existsByReferenceIgnoreCase(String reference);

    @EntityGraph(attributePaths = {
            "purchaseOrderItem",
            "purchaseOrderItem.product",
            "purchaseOrderItem.purchaseOrder",
            "purchaseOrderItem.purchaseOrder.warehouse"
    })
    Optional<GoodsReception> findById(UUID id);

    @EntityGraph(attributePaths = {
            "purchaseOrderItem",
            "purchaseOrderItem.product",
            "purchaseOrderItem.purchaseOrder",
            "purchaseOrderItem.purchaseOrder.warehouse"
    })
    Page<GoodsReception> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {
            "purchaseOrderItem",
            "purchaseOrderItem.product",
            "purchaseOrderItem.purchaseOrder",
            "purchaseOrderItem.purchaseOrder.warehouse"
    })
    Page<GoodsReception> findByPurchaseOrderItemPurchaseOrderId(
            UUID purchaseOrderId,
            Pageable pageable
    );
}
