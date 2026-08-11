package com.ndoruhirwe.smartlogistics.controller;

import com.ndoruhirwe.smartlogistics.dto.request.VehicleCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.VehicleUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.VehicleResponse;
import com.ndoruhirwe.smartlogistics.entity.enums.VehicleStatus;
import com.ndoruhirwe.smartlogistics.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(
            VehicleService vehicleService
    ) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse createVehicle(
            @Valid @RequestBody VehicleCreateRequest request
    ) {
        return vehicleService.createVehicle(request);
    }

    @GetMapping
    public Page<VehicleResponse> getAllVehicles(
            Pageable pageable
    ) {
        return vehicleService.getAllVehicles(pageable);
    }

    @GetMapping("/search/registration")
    public Page<VehicleResponse> searchByRegistrationNumber(
            @RequestParam String registrationNumber, Pageable pageable) {
        return vehicleService.searchVehiclesByRegistrationNumber(registrationNumber, pageable);
    }

    @GetMapping("/search/brand")
    public Page<VehicleResponse> searchByBrand(@RequestParam String brand, Pageable pageable) {
        return vehicleService.searchVehiclesByBrand(brand, pageable);
    }

    @GetMapping("/status/{status}")
    public Page<VehicleResponse> getVehiclesByStatus(
            @PathVariable VehicleStatus status, Pageable pageable) {
        return vehicleService.getVehiclesByStatus(status, pageable);
    }

    @GetMapping("/{id}")
    public VehicleResponse getVehicleById(@PathVariable UUID id) {
        return vehicleService.getVehicleById(id);
    }

    @PutMapping("/{id}")
    public VehicleResponse updateVehicle(@PathVariable UUID id,
            @Valid @RequestBody VehicleUpdateRequest request) {
        return vehicleService.updateVehicle(id, request);
    }

    @PatchMapping("/{id}/activate")
    public VehicleResponse activateVehicle(@PathVariable UUID id) {
        return vehicleService.activateVehicle(id);
    }

    @PatchMapping("/{id}/deactivate")
    public VehicleResponse deactivateVehicle(@PathVariable UUID id) {
        return vehicleService.deactivateVehicle(id);
    }

}
