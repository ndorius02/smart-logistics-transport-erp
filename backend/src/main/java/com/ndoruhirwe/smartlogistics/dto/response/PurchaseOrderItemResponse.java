package com.ndoruhirwe.smartlogistics.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record PurchaseOrderItemResponse(

        UUID id,

        UUID productId,

        String productSku,

        String productName,

        BigDecimal orderedQuantity,

        BigDecimal receivedQuantity,

        BigDecimal remainingQuantity,

        BigDecimal unitPrice,

        BigDecimal lineTotal
) {
}
