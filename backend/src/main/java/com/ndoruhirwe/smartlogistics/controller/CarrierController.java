package com.ndoruhirwe.smartlogistics.controller;

import com.ndoruhirwe.smartlogistics.dto.request.CarrierCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.CarrierUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.CarrierResponse;
import com.ndoruhirwe.smartlogistics.service.CarrierService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/carriers")
public class CarrierController {

    private final CarrierService carrierService;

    public CarrierController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarrierResponse createCarrier(
            @Valid @RequestBody CarrierCreateRequest request) {
        return carrierService.createCarrier(request);
    }

    @GetMapping
    public Page<CarrierResponse> getAllCarriers(Pageable pageable) {
        return carrierService.getAllCarriers(pageable);
    }

    @GetMapping("/search/company-name")
    public Page<CarrierResponse> searchByCompanyName(@RequestParam String companyName, Pageable pageable) {
        return carrierService.searchCarriersByCompanyName(companyName, pageable);
    }

    @GetMapping("/search/code")
    public Page<CarrierResponse> searchByCode(@RequestParam String code, Pageable pageable) {
        return carrierService.searchCarriersByCode(code, pageable);
    }

    @GetMapping("/search/license")
    public Page<CarrierResponse> searchByLicenseNumber(@RequestParam String licenseNumber, Pageable pageable) {
        return carrierService.searchCarriersByLicenseNumber(licenseNumber, pageable);
    }

    @GetMapping("/{id}")
    public CarrierResponse getCarrierById(@PathVariable UUID id) {
        return carrierService.getCarrierById(id);
    }

    @PutMapping("/{id}")
    public CarrierResponse updateCarrier(@PathVariable UUID id,
            @Valid @RequestBody CarrierUpdateRequest request) {
        return carrierService.updateCarrier(id, request);
    }

    @PatchMapping("/{id}/activate")
    public CarrierResponse activateCarrier(@PathVariable UUID id) {
        return carrierService.activateCarrier(id);
    }

    @PatchMapping("/{id}/deactivate")
    public CarrierResponse deactivateCarrier(@PathVariable UUID id) {
        return carrierService.deactivateCarrier(id);
    }
}
