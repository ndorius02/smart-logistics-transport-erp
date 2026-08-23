package com.ndoruhirwe.smartlogistics.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record InventoryResponse(
        UUID id,

        UUID productId,
        String productSku,
        String productName,

        UUID warehouseId,
        String warehouseCode,
        String warehouseName,

        BigDecimal quantity,

        BigDecimal reservedQuantity,

        BigDecimal availableQuantity,

        BigDecimal minimumStockLevel,

        boolean lowStock,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
