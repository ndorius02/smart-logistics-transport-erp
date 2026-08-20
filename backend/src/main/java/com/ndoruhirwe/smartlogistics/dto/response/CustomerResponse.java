package com.ndoruhirwe.smartlogistics.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerResponse(
        UUID id,

        String code,

        String companyName,

        String contactName,

        String email,

        String phoneNumber,

        String address,

        String city,

        String postalCode,

        String country,

        String vatNumber,

        boolean active,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
