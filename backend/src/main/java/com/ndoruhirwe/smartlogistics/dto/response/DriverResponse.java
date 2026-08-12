package com.ndoruhirwe.smartlogistics.dto.response;

import com.ndoruhirwe.smartlogistics.entity.enums.DriverStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DriverResponse(
        UUID id,

        String firstName,

        String lastName,

        String licenseNumber,

        String phoneNumber,

        DriverStatus status,

        boolean active,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
