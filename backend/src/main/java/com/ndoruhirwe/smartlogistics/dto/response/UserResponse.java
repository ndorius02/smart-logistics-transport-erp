package com.ndoruhirwe.smartlogistics.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        boolean active,
        UUID roleId,
        String roleName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
