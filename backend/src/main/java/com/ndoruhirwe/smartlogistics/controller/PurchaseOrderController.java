package com.ndoruhirwe.smartlogistics.controller;

import com.ndoruhirwe.smartlogistics.dto.request.PurchaseOrderCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.PurchaseOrderItemCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.PurchaseOrderItemUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.PurchaseOrderUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.PurchaseOrderResponse;
import com.ndoruhirwe.smartlogistics.entity.enums.PurchaseOrderStatus;
import com.ndoruhirwe.smartlogistics.service.PurchaseOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    @PostMapping
    public ResponseEntity<PurchaseOrderResponse> create(
            @Valid @RequestBody PurchaseOrderCreateRequest request) {
        PurchaseOrderResponse response = purchaseOrderService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(purchaseOrderService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<PurchaseOrderResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(purchaseOrderService.getAll(pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PurchaseOrderResponse>> getByStatus(
            @PathVariable PurchaseOrderStatus status, Pageable pageable) {

        return ResponseEntity.ok(purchaseOrderService.getByStatus(status, pageable)
        );
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<Page<PurchaseOrderResponse>> getBySupplier(@PathVariable UUID supplierId,
            Pageable pageable) {
        return ResponseEntity.ok(purchaseOrderService.getBySupplier(supplierId, pageable));
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<Page<PurchaseOrderResponse>> getByWarehouse(
            @PathVariable UUID warehouseId,
            Pageable pageable) {
        return ResponseEntity.ok(
                purchaseOrderService.getByWarehouse(warehouseId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderResponse> update(@PathVariable UUID id,
            @Valid @RequestBody PurchaseOrderUpdateRequest request) {
        return ResponseEntity.ok(purchaseOrderService.update(id, request)
        );
    }

    @PostMapping("/{purchaseOrderId}/items")
    public ResponseEntity<PurchaseOrderResponse> addItem(@PathVariable UUID purchaseOrderId,
            @Valid @RequestBody PurchaseOrderItemCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(purchaseOrderService.addItem(purchaseOrderId, request)
                );
    }

    @PutMapping("/{purchaseOrderId}/items/{itemId}")
    public ResponseEntity<PurchaseOrderResponse> updateItem(
            @PathVariable UUID purchaseOrderId,
            @PathVariable UUID itemId,
            @Valid @RequestBody PurchaseOrderItemUpdateRequest request) {
        return ResponseEntity.ok(purchaseOrderService.updateItem(purchaseOrderId, itemId, request));
    }

    @DeleteMapping("/{purchaseOrderId}/items/{itemId}")
    public ResponseEntity<PurchaseOrderResponse> removeItem(
            @PathVariable UUID purchaseOrderId,
            @PathVariable UUID itemId) {

        return ResponseEntity.ok(purchaseOrderService.removeItem(purchaseOrderId, itemId));
    }

    @PatchMapping("/{id}/submit")
    public ResponseEntity<PurchaseOrderResponse> submit(@PathVariable UUID id) {
        return ResponseEntity.ok(purchaseOrderService.submit(id));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<PurchaseOrderResponse> approve(@PathVariable UUID id) {
        return ResponseEntity.ok(purchaseOrderService.approve(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<PurchaseOrderResponse> cancel(@PathVariable UUID id) {
        return ResponseEntity.ok(purchaseOrderService.cancel(id));
    }
}
