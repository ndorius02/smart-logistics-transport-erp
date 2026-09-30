package com.ndoruhirwe.smartlogistics.dto.request;

import com.ndoruhirwe.smartlogistics.entity.enums.CargoType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CargoCreateRequest(
        @NotBlank
        @Size(max = 50)
        String cargoNumber,

        @NotBlank
        @Size(max = 255)
        String description,

        @NotNull
        CargoType cargoType,

        @NotNull
        @DecimalMin(value = "0.001")
        BigDecimal quantity,

        @NotBlank
        @Size(max = 50)
        String unit,

        @DecimalMin(value = "0.000")
        BigDecimal weight,

        @DecimalMin(value = "0.000")
        BigDecimal volume,

        @Size(max = 500)
        String specialInstructions
) {
}
