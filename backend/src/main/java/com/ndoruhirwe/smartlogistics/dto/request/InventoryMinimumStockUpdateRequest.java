package com.ndoruhirwe.smartlogistics.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record InventoryMinimumStockUpdateRequest(
        @NotNull(message = "Minimum stock level is required")
        @PositiveOrZero(message = "Minimum stock level must be zero or greater")
        BigDecimal minimumStockLevel
) {
}
