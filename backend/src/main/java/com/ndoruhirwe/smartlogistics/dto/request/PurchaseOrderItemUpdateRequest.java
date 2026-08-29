package com.ndoruhirwe.smartlogistics.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
public record PurchaseOrderItemUpdateRequest(

        @NotNull(message = "Ordered quantity is required")
        @DecimalMin(value = "0.001",
                message = "Ordered quantity must be greater than zero")
        BigDecimal orderedQuantity,

        @NotNull(message = "Unit price is required")
        @DecimalMin(value = "0.00", message = "Unit price must be zero or greater")
        BigDecimal unitPrice

) {
}
