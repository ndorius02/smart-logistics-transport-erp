package com.ndoruhirwe.smartlogistics.dto.response;

import com.ndoruhirwe.smartlogistics.entity.enums.VehicleStatus;
import com.ndoruhirwe.smartlogistics.entity.enums.VehicleType;

import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleResponse(
        UUID id,

        String registrationNumber,

        String brand,

        String model,

        VehicleType vehicleType,

        Integer loadCapacity,

        VehicleStatus operationalStatus,

        boolean active,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
