package com.ndoruhirwe.smartlogistics.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record InventoryCreateRequest(

        @NotNull(message = "Product is required")
        UUID productId,

        @NotNull(message = "Warehouse is required")
        UUID warehouseId,

        @NotNull(message = "Minimum stock level is required")
        @PositiveOrZero(message = "Minimum stock level must be zero or greater")
        BigDecimal minimumStockLevel
) {
}
