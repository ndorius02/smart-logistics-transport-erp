package com.ndoruhirwe.smartlogistics.dto.response;

import com.ndoruhirwe.smartlogistics.entity.enums.StockMovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record StockMovementResponse(

        UUID id,

        String reference,

        UUID productId,
        String productSku,
        String productName,

        UUID warehouseId,
        String warehouseCode,
        String warehouseName,

        StockMovementType movementType,

        BigDecimal quantity,

        String reason,

        String notes,

        LocalDateTime movementDate,

        String createdBy,

        LocalDateTime createdAt
) {
}
