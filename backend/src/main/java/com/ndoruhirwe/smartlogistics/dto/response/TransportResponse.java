package com.ndoruhirwe.smartlogistics.dto.response;

import com.ndoruhirwe.smartlogistics.entity.enums.TransportStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransportResponse(
        UUID id,

        String code,

        UUID originWarehouseId,

        String originWarehouseCode,

        String originWarehouseName,

        UUID destinationWarehouseId,

        String destinationWarehouseCode,

        String destinationWarehouseName,

        UUID vehicleId,

        String vehicleRegistrationNumber,

        UUID driverId,

        String driverFirstName,

        String driverLastName,

        LocalDateTime plannedDepartureAt,

        LocalDateTime plannedArrivalAt,

        LocalDateTime actualDepartureAt,

        LocalDateTime actualArrivalAt,

        TransportStatus status,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
