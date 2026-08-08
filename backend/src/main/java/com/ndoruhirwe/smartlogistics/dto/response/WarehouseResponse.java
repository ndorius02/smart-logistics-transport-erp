package com.ndoruhirwe.smartlogistics.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record WarehouseResponse(
        UUID id,
        String code,
        String name,
        String address,
        String city,
        String country,
        Integer capacity,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
