package com.ndoruhirwe.smartlogistics.service;

import com.ndoruhirwe.smartlogistics.dto.request.WarehouseCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.WarehouseUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.WarehouseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;
public interface WarehouseService {
    WarehouseResponse createWarehouse(WarehouseCreateRequest request);

    Page<WarehouseResponse> getAllWarehouses(Pageable pageable);

    WarehouseResponse getWarehouseById(UUID id);

    WarehouseResponse updateWarehouse(UUID id, WarehouseUpdateRequest request);
    WarehouseResponse deactivateWarehouse(UUID id);
    Page<WarehouseResponse> searchWarehousesByName(String name, Pageable pageable);
    WarehouseResponse activateWarehouse(UUID id);
}
