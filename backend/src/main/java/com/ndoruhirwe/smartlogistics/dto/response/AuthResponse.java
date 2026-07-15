package com.ndoruhirwe.smartlogistics.dto.response;

import java.util.List;

public record AuthResponse(
        String token,
        String tokenType,
        String email,
        List<String> authorities
) {
}
