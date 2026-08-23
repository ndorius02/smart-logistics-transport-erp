package com.ndoruhirwe.smartlogistics.service;

import com.ndoruhirwe.smartlogistics.dto.request.InventoryCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.InventoryMinimumStockUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.InventoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface InventoryService {

    InventoryResponse createInventory(InventoryCreateRequest request);

    Page<InventoryResponse> getAllInventory(Pageable pageable);

    InventoryResponse getInventoryById(UUID id);

    Page<InventoryResponse> getInventoryByWarehouse(UUID warehouseId, Pageable pageable);

    Page<InventoryResponse> getInventoryByProduct(UUID productId, Pageable pageable);

    InventoryResponse getInventoryByProductAndWarehouse(UUID productId, UUID warehouseId);

    Page<InventoryResponse> getLowStockInventory(Pageable pageable);

    InventoryResponse updateMinimumStockLevel(UUID id, InventoryMinimumStockUpdateRequest request);
}
