package com.ndoruhirwe.smartlogistics.controller;


import com.ndoruhirwe.smartlogistics.dto.request.StockMovementCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.StockMovementResponse;
import com.ndoruhirwe.smartlogistics.entity.enums.StockMovementType;
import com.ndoruhirwe.smartlogistics.service.StockMovementService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/stock-movements")

public class StockMovementController {

    private final StockMovementService stockMovementService;

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StockMovementResponse createStockMovement(
            @Valid @RequestBody StockMovementCreateRequest request) {
        return stockMovementService.createStockMovement(request);
    }

    @GetMapping
    public Page<StockMovementResponse> getAllStockMovements(Pageable pageable) {
        return stockMovementService.getAllStockMovements(pageable);
    }

    @GetMapping("/product/{productId}")
    public Page<StockMovementResponse> getStockMovementsByProduct(
            @PathVariable UUID productId,
            Pageable pageable) {
        return stockMovementService.getStockMovementsByProduct(productId, pageable);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public Page<StockMovementResponse> getStockMovementsByWarehouse(
            @PathVariable UUID warehouseId,
            Pageable pageable) {
        return stockMovementService.getStockMovementsByWarehouse(warehouseId, pageable);
    }

    @GetMapping("/product/{productId}/warehouse/{warehouseId}")
    public Page<StockMovementResponse> getStockMovementsByProductAndWarehouse(
            @PathVariable UUID productId,
            @PathVariable UUID warehouseId, Pageable pageable) {
        return stockMovementService.getStockMovementsByProductAndWarehouse(productId,
                warehouseId,
                pageable);
    }

    @GetMapping("/type/{movementType}")
    public Page<StockMovementResponse> getStockMovementsByType(
            @PathVariable StockMovementType movementType,
            Pageable pageable) {
        return stockMovementService.getStockMovementsByType(movementType, pageable);
    }

    @GetMapping("/{id}")
    public StockMovementResponse getStockMovementById(@PathVariable UUID id) {
        return stockMovementService.getStockMovementById(id);
    }
}
