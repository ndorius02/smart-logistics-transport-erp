package com.ndoruhirwe.smartlogistics.service;

import com.ndoruhirwe.smartlogistics.dto.request.StockMovementCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.StockMovementResponse;
import com.ndoruhirwe.smartlogistics.entity.enums.StockMovementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StockMovementService {

    StockMovementResponse createStockMovement(StockMovementCreateRequest request);

    Page<StockMovementResponse> getAllStockMovements(Pageable pageable);

    StockMovementResponse getStockMovementById(UUID id);

    Page<StockMovementResponse> getStockMovementsByProduct(UUID productId, Pageable pageable);

    Page<StockMovementResponse> getStockMovementsByWarehouse(UUID warehouseId, Pageable pageable);

    Page<StockMovementResponse> getStockMovementsByProductAndWarehouse(UUID productId, UUID warehouseId, Pageable pageable);

    Page<StockMovementResponse> getStockMovementsByType(StockMovementType movementType, Pageable pageable);
}
