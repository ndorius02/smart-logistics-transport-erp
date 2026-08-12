package com.ndoruhirwe.smartlogistics.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;
public record TransportUpdateRequest(
        @NotBlank(message = "Transport code is required")
        @Size(max = 50, message = "Transport code must not exceed 50 characters")
        String code,

        @NotNull(message = "Origin warehouse is required")
        UUID originWarehouseId,

        @NotNull(message = "Destination warehouse is required")
        UUID destinationWarehouseId,

        @NotNull(message = "Vehicle is required")
        UUID vehicleId,

        @NotNull(message = "Driver is required")
        UUID driverId,

        @NotNull(message = "Planned departure date is required")
        LocalDateTime plannedDepartureAt,

        @NotNull(message = "Planned arrival date is required")
        LocalDateTime plannedArrivalAt
) {
}
