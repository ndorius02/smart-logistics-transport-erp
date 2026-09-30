package com.ndoruhirwe.smartlogistics.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;
public record ShipmentUpdateRequest(
        @NotNull
        UUID customerId,

        @NotNull
        UUID originWarehouseId,

        @NotNull
        UUID destinationWarehouseId,

        @NotNull
        @FutureOrPresent
        LocalDateTime plannedPickupDate,

        @NotNull
        @FutureOrPresent
        LocalDateTime plannedDeliveryDate,

        @Size(max = 500)
        String notes
) {
}
