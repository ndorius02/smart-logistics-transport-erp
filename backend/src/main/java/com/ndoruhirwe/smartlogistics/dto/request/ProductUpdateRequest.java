package com.ndoruhirwe.smartlogistics.dto.request;

import com.ndoruhirwe.smartlogistics.entity.enums.UnitOfMeasure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;
public record ProductUpdateRequest(

        @NotBlank(message = "SKU is required")
        @Size(max = 50, message = "SKU must not exceed 50 characters")
        String sku,

        @NotBlank(message = "Product name is required")
        @Size(max = 150, message = "Product name must not exceed 150 characters")
        String name,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        @NotNull(message = "Product category is required")
        UUID categoryId,

        @NotNull(message = "Unit of measure is required")
        UnitOfMeasure unitOfMeasure,

        @Positive(message = "Weight must be greater than zero")
        BigDecimal weight,

        @NotNull(message = "Active status is required")
        Boolean active
) {
}
