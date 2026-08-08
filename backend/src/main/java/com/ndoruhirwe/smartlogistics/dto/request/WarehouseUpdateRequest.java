package com.ndoruhirwe.smartlogistics.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record WarehouseUpdateRequest(
        @NotBlank(message = "Warehouse code is required")
        @Size(max = 50, message = "Warehouse code must not exceed 50 characters")
        String code,

        @NotBlank(message = "Warehouse name is required")
        @Size(max = 150, message = "Warehouse name must not exceed 150 characters")
        String name,

        @NotBlank(message = "Address is required")
        @Size(max = 255, message = "Address must not exceed 255 characters")
        String address,

        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must not exceed 100 characters")
        String city,

        @NotBlank(message = "Country is required")
        @Size(max = 100, message = "Country must not exceed 100 characters")
        String country,

        @NotNull(message = "Capacity is required")
        @Positive(message = "Capacity must be greater than zero")
        Integer capacity,

        @NotNull(message = "Active status is required")
        Boolean active
) {
}
