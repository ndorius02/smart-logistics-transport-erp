package com.ndoruhirwe.smartlogistics.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CarrierResponse(
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

        String licenseNumber,

        boolean active,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
