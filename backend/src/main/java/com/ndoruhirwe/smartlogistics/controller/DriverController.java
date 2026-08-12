package com.ndoruhirwe.smartlogistics.controller;

import com.ndoruhirwe.smartlogistics.dto.request.DriverCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.DriverUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.DriverResponse;
import com.ndoruhirwe.smartlogistics.entity.enums.DriverStatus;
import com.ndoruhirwe.smartlogistics.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {
    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverResponse createDriver(@Valid @RequestBody DriverCreateRequest request) {
        return driverService.createDriver(request);
    }

    @GetMapping
    public Page<DriverResponse> getAllDrivers(Pageable pageable) {
        return driverService.getAllDrivers(pageable);
    }

    @GetMapping("/search/license")
    public Page<DriverResponse> searchByLicenseNumber(@RequestParam String licenseNumber, Pageable pageable) {
        return driverService.searchDriversByLicenseNumber(licenseNumber, pageable);
    }

    @GetMapping("/search/last-name")
    public Page<DriverResponse> searchByLastName(@RequestParam String lastName, Pageable pageable) {
        return driverService.searchDriversByLastName(lastName, pageable);
    }

    @GetMapping("/status/{status}")
    public Page<DriverResponse> getDriversByStatus(@PathVariable DriverStatus status, Pageable pageable) {
        return driverService.getDriversByStatus(status, pageable);
    }

    @GetMapping("/{id}")
    public DriverResponse getDriverById(@PathVariable UUID id) {
        return driverService.getDriverById(id);
    }

    @PutMapping("/{id}")
    public DriverResponse updateDriver(@PathVariable UUID id,
                                       @Valid @RequestBody DriverUpdateRequest request) {
        return driverService.updateDriver(id, request);
    }

    @PatchMapping("/{id}/activate")
    public DriverResponse activateDriver(@PathVariable UUID id) {
        return driverService.activateDriver(id);
    }

    @PatchMapping("/{id}/deactivate")
    public DriverResponse deactivateDriver(@PathVariable UUID id) {
        return driverService.deactivateDriver(id);
    }
}
