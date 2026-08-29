package com.ndoruhirwe.smartlogistics.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;


public record PurchaseOrderCreateRequest(

        @NotBlank(message = "Order number is required")
        @Size(max = 50, message = "Order number must not exceed 50 characters")
        String orderNumber,

        @NotNull(message = "Supplier is required")
        UUID supplierId,

        @NotNull(message = "Warehouse is required")
        UUID warehouseId,

        LocalDate expectedDeliveryDate,

        @Size(max = 500, message = "Notes must not exceed 500 characters")
        String notes
) {
}
