package com.ndoruhirwe.smartlogistics.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductCategoryCreateRequest(
        @NotBlank(message = "Category code is required")
        @Size(max = 50, message = "Category code must not exceed 50 characters")
        String code,

        @NotBlank(message = "Category name is required")
        @Size(max = 150, message = "Category name must not exceed 150 characters")
        String name,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description

) {
}
