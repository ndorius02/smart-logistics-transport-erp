package com.ndoruhirwe.smartlogistics.dto.response;

import com.ndoruhirwe.smartlogistics.entity.enums.CargoType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CargoResponse(
        UUID id,

        String cargoNumber,

        UUID shipmentId,
        String shipmentNumber,

        String description,

        CargoType cargoType,

        BigDecimal quantity,
        String unit,

        BigDecimal weight,
        BigDecimal volume,

        String specialInstructions,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
