package com.ndoruhirwe.smartlogistics.controller;


import com.ndoruhirwe.smartlogistics.dto.request.InventoryCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.InventoryMinimumStockUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.InventoryResponse;
import com.ndoruhirwe.smartlogistics.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")

public class InventoryController {


    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse createInventory(
            @Valid @RequestBody InventoryCreateRequest request) {
        return inventoryService.createInventory(request);
    }

    @GetMapping
    public Page<InventoryResponse> getAllInventory(Pageable pageable) {
        return inventoryService.getAllInventory(pageable);
    }

    @GetMapping("/low-stock")
    public Page<InventoryResponse> getLowStockInventory(Pageable pageable) {
        return inventoryService.getLowStockInventory(pageable);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public Page<InventoryResponse> getInventoryByWarehouse(
            @PathVariable UUID warehouseId, Pageable pageable) {
        return inventoryService.getInventoryByWarehouse(warehouseId, pageable);
    }

    @GetMapping("/product/{productId}")
    public Page<InventoryResponse> getInventoryByProduct(@PathVariable UUID productId,
            Pageable pageable) {
        return inventoryService.getInventoryByProduct(productId, pageable);
    }

    @GetMapping("/product/{productId}/warehouse/{warehouseId}")
    public InventoryResponse getInventoryByProductAndWarehouse(@PathVariable UUID productId,
            @PathVariable UUID warehouseId) {
        return inventoryService.getInventoryByProductAndWarehouse(productId, warehouseId);
    }

    @GetMapping("/{id}")
    public InventoryResponse getInventoryById(@PathVariable UUID id) {
        return inventoryService.getInventoryById(id);
    }

    @PatchMapping("/{id}/minimum-stock-level")
    public InventoryResponse updateMinimumStockLevel(@PathVariable UUID id,
            @Valid @RequestBody InventoryMinimumStockUpdateRequest request) {
        return inventoryService.updateMinimumStockLevel(id, request);
    }

}
