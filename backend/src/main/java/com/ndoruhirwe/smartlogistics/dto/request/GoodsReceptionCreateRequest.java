package com.ndoruhirwe.smartlogistics.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record GoodsReceptionCreateRequest(

        @NotBlank(message = "Reception reference is required")
        @Size(max = 50, message = "Reception reference must not exceed 50 characters")
        String reference,

        @NotNull(message = "Purchase order item is required")
        UUID purchaseOrderItemId,

        @NotNull(message = "Received quantity is required")
        @DecimalMin(
                value = "0.001",
                message = "Received quantity must be greater than zero"
        )
        BigDecimal quantity,

        @Size(max = 500, message = "Notes must not exceed 500 characters")
        String notes
) {
}
