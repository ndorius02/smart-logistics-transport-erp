package com.ndoruhirwe.smartlogistics.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductCategoryResponse(
        UUID id,

        String code,

        String name,

        String description,

        boolean active,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
