package com.ndoruhirwe.smartlogistics.service;

import com.ndoruhirwe.smartlogistics.dto.request.VehicleCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.VehicleUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.VehicleResponse;
import com.ndoruhirwe.smartlogistics.entity.enums.VehicleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;
public interface VehicleService {
    VehicleResponse createVehicle(VehicleCreateRequest request);

    Page<VehicleResponse> getAllVehicles(Pageable pageable);

    VehicleResponse getVehicleById(UUID id);

    VehicleResponse updateVehicle(UUID id, VehicleUpdateRequest request);

    VehicleResponse activateVehicle(UUID id);

    VehicleResponse deactivateVehicle(UUID id);

    Page<VehicleResponse> searchVehiclesByRegistrationNumber(String registrationNumber, Pageable pageable);

    Page<VehicleResponse> searchVehiclesByBrand(String brand, Pageable pageable);

    Page<VehicleResponse> getVehiclesByStatus(VehicleStatus status, Pageable pageable);
}
