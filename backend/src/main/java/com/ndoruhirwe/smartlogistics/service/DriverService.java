package com.ndoruhirwe.smartlogistics.service;

import com.ndoruhirwe.smartlogistics.dto.request.DriverCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.DriverUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.DriverResponse;
import com.ndoruhirwe.smartlogistics.entity.enums.DriverStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DriverService {

    DriverResponse createDriver(DriverCreateRequest request);

    Page<DriverResponse> getAllDrivers(Pageable pageable);

    DriverResponse getDriverById(UUID id);

    DriverResponse updateDriver(UUID id, DriverUpdateRequest request);

    DriverResponse activateDriver(UUID id);

    DriverResponse deactivateDriver(UUID id);

    Page<DriverResponse> searchDriversByLicenseNumber(String licenseNumber, Pageable pageable);

    Page<DriverResponse> searchDriversByLastName(String lastName, Pageable pageable);

    Page<DriverResponse> getDriversByStatus(DriverStatus status, Pageable pageable);
}
