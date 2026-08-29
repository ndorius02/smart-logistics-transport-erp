package com.ndoruhirwe.smartlogistics.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record PurchaseOrderUpdateRequest(

        @NotNull(message = "Supplier is required")
        UUID supplierId,

        @NotNull(message = "Warehouse is required")
        UUID warehouseId,

        LocalDate expectedDeliveryDate,

        @Size(max = 500, message = "Notes must not exceed 500 characters")
        String notes
) {
}
