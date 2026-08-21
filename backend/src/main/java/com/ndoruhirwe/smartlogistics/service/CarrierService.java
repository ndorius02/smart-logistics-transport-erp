package com.ndoruhirwe.smartlogistics.service;

import com.ndoruhirwe.smartlogistics.dto.request.CarrierCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.CarrierUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.CarrierResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CarrierService {
    CarrierResponse createCarrier(CarrierCreateRequest request);

    Page<CarrierResponse> getAllCarriers(Pageable pageable);

    CarrierResponse getCarrierById(UUID id);

    CarrierResponse updateCarrier(UUID id, CarrierUpdateRequest request);

    CarrierResponse activateCarrier(UUID id);

    CarrierResponse deactivateCarrier(UUID id);

    Page<CarrierResponse> searchCarriersByCompanyName(String companyName, Pageable pageable);

    Page<CarrierResponse> searchCarriersByCode(String code, Pageable pageable);

    Page<CarrierResponse> searchCarriersByLicenseNumber(String licenseNumber, Pageable pageable);
}
