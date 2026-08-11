package com.ndoruhirwe.smartlogistics.controller;

import com.ndoruhirwe.smartlogistics.dto.request.WarehouseCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.WarehouseUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.WarehouseResponse;
import com.ndoruhirwe.smartlogistics.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/warehouses")

public class WarehouseController {
    private final WarehouseService warehouseService;

    public WarehouseController(
            WarehouseService warehouseService
    ) {
        this.warehouseService = warehouseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WarehouseResponse createWarehouse(
            @Valid @RequestBody WarehouseCreateRequest request
    ) {
        return warehouseService.createWarehouse(request);
    }

    @GetMapping
    public Page<WarehouseResponse> getAllWarehouses(
            Pageable pageable
    ) {
        return warehouseService.getAllWarehouses(pageable);
    }

    @GetMapping("/search")
    public Page<WarehouseResponse> searchWarehousesByName(
            @RequestParam String name,
            Pageable pageable
    ) {
        return warehouseService.searchWarehousesByName(
                name,
                pageable
        );
    }

    @GetMapping("/{id}")
    public WarehouseResponse getWarehouseById(
            @PathVariable UUID id
    ) {
        return warehouseService.getWarehouseById(id);
    }

    @PutMapping("/{id}")
    public WarehouseResponse updateWarehouse(
            @PathVariable UUID id,
            @Valid @RequestBody WarehouseUpdateRequest request
    ) {
        return warehouseService.updateWarehouse(id, request);
    }

    @PatchMapping("/{id}/activate")
    public WarehouseResponse activateWarehouse(
            @PathVariable UUID id
    ) {
        return warehouseService.activateWarehouse(id);
    }

    @PatchMapping("/{id}/deactivate")
    public WarehouseResponse deactivateWarehouse(
            @PathVariable UUID id
    ) {
        return warehouseService.deactivateWarehouse(id);
    }
}
