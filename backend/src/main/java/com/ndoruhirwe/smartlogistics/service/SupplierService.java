package com.ndoruhirwe.smartlogistics.service;

import com.ndoruhirwe.smartlogistics.dto.request.SupplierCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.SupplierUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.SupplierResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SupplierService {

    SupplierResponse createSupplier(SupplierCreateRequest request);

    Page<SupplierResponse> getAllSuppliers(Pageable pageable);

    SupplierResponse getSupplierById(UUID id);

    SupplierResponse updateSupplier(UUID id, SupplierUpdateRequest request);

    SupplierResponse activateSupplier(UUID id);

    SupplierResponse deactivateSupplier(UUID id);

    Page<SupplierResponse> searchSuppliersByCompanyName(String companyName, Pageable pageable);

    Page<SupplierResponse> searchSuppliersByCode(String code, Pageable pageable);
}
