package com.ndoruhirwe.smartlogistics.dto.request;

import com.ndoruhirwe.smartlogistics.entity.enums.VehicleStatus;
import com.ndoruhirwe.smartlogistics.entity.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record VehicleUpdateRequest(
        @NotBlank(message = "Registration number is required")
        @Size(max = 50, message = "Registration number must not exceed 50 characters")
        String registrationNumber,

        @NotBlank(message = "Brand is required")
        @Size(max = 100, message = "Brand must not exceed 100 characters")
        String brand,

        @NotBlank(message = "Model is required")
        @Size(max = 100, message = "Model must not exceed 100 characters")
        String model,

        @NotNull(message = "Vehicle type is required")
        VehicleType vehicleType,

        @NotNull(message = "Load capacity is required")
        @Positive(message = "Load capacity must be greater than zero")
        Integer loadCapacity,

        @NotNull(message = "Operational status is required")
        VehicleStatus operationalStatus
) {
}
