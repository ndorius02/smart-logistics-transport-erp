package com.ndoruhirwe.smartlogistics.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record GoodsReceptionResponse(

        UUID id,

        String reference,

        UUID purchaseOrderId,

        String purchaseOrderNumber,

        UUID purchaseOrderItemId,

        UUID productId,

        String productSku,

        String productName,

        UUID warehouseId,

        String warehouseCode,

        String warehouseName,

        BigDecimal quantity,

        String notes,

        LocalDateTime receptionDate,

        String createdBy,

        LocalDateTime createdAt
) {
}
