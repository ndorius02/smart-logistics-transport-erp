package com.ndoruhirwe.smartlogistics.dto.response;

import com.ndoruhirwe.smartlogistics.entity.enums.PurchaseOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PurchaseOrderResponse(

        UUID id,

        String orderNumber,

        UUID supplierId,

        String supplierCode,

        String supplierName,

        UUID warehouseId,

        String warehouseCode,

        String warehouseName,

        LocalDate orderDate,

        LocalDate expectedDeliveryDate,

        PurchaseOrderStatus status,

        String notes,

        List<PurchaseOrderItemResponse> items,

        BigDecimal totalAmount,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
