package com.ndoruhirwe.smartlogistics.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import com.ndoruhirwe.smartlogistics.entity.enums.ShipmentStatus;

public record ShipmentResponse(
        UUID id,

        String shipmentNumber,

        UUID customerId,
        String customerCode,
        String customerName,

        UUID originWarehouseId,
        String originWarehouseCode,
        String originWarehouseName,

        UUID destinationWarehouseId,
        String destinationWarehouseCode,
        String destinationWarehouseName,

        UUID transportId,
        String transportCode,

        LocalDateTime plannedPickupDate,
        LocalDateTime plannedDeliveryDate,

        ShipmentStatus status,

        String notes,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
