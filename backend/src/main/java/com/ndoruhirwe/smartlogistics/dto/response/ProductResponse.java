package com.ndoruhirwe.smartlogistics.dto.response;

import com.ndoruhirwe.smartlogistics.entity.enums.UnitOfMeasure;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(

        UUID id,

        String sku,

        String name,

        String description,

        UUID categoryId,

        String categoryCode,

        String categoryName,

        UnitOfMeasure unitOfMeasure,

        BigDecimal weight,

        boolean active,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
