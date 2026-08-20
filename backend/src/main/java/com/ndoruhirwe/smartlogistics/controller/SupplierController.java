package com.ndoruhirwe.smartlogistics.controller;

import com.ndoruhirwe.smartlogistics.dto.request.SupplierCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.SupplierUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.SupplierResponse;
import com.ndoruhirwe.smartlogistics.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupplierResponse createSupplier(@Valid @RequestBody SupplierCreateRequest request) {
        return supplierService.createSupplier(request);
    }

    @GetMapping
    public Page<SupplierResponse> getAllSuppliers(Pageable pageable) {
        return supplierService.getAllSuppliers(pageable);
    }

    @GetMapping("/search/company-name")
    public Page<SupplierResponse> searchByCompanyName(@RequestParam String companyName,
                                                      Pageable pageable) {
        return supplierService.searchSuppliersByCompanyName(companyName, pageable);
    }

    @GetMapping("/search/code")
    public Page<SupplierResponse> searchByCode(@RequestParam String code, Pageable pageable) {
        return supplierService.searchSuppliersByCode(code, pageable);
    }

    @GetMapping("/{id}")
    public SupplierResponse getSupplierById(@PathVariable UUID id) {
        return supplierService.getSupplierById(id);
    }

    @PutMapping("/{id}")
    public SupplierResponse updateSupplier(@PathVariable UUID id,
            @Valid @RequestBody SupplierUpdateRequest request) {
        return supplierService.updateSupplier(id, request);
    }

    @PatchMapping("/{id}/activate")
    public SupplierResponse activateSupplier(@PathVariable UUID id) {
        return supplierService.activateSupplier(id);
    }

    @PatchMapping("/{id}/deactivate")
    public SupplierResponse deactivateSupplier(@PathVariable UUID id) {
        return supplierService.deactivateSupplier(id);
    }
}
