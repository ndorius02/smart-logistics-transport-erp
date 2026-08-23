package com.ndoruhirwe.smartlogistics.dto.request;

import com.ndoruhirwe.smartlogistics.entity.enums.StockMovementType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record StockMovementCreateRequest(
        @NotBlank(message = "Movement reference is required")
        @Size(max = 50, message = "Movement reference must not exceed 50 characters")
        String reference,

        @NotNull(message = "Product is required")
        UUID productId,

        @NotNull(message = "Warehouse is required")
        UUID warehouseId,

        @NotNull(message = "Movement type is required")
        StockMovementType movementType,

        @NotNull(message = "Movement quantity is required")
        @Positive(message = "Movement quantity must be greater than zero")
        BigDecimal quantity,

        @Size(max = 255, message = "Reason must not exceed 255 characters")
        String reason,

        @Size(max = 500, message = "Notes must not exceed 500 characters")
        String notes

) {
}
