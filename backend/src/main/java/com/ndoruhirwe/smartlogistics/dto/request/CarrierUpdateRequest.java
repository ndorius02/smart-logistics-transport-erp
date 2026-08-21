package com.ndoruhirwe.smartlogistics.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CarrierUpdateRequest(
        @NotBlank(message = "Carrier code is required")
        @Size(max = 50, message = "Carrier code must not exceed 50 characters")
        String code,

        @NotBlank(message = "Company name is required")
        @Size(max = 150, message = "Company name must not exceed 150 characters")
        String companyName,

        @Size(max = 150, message = "Contact name must not exceed 150 characters")
        String contactName,

        @Email(message = "Email must be valid")
        @Size(max = 150, message = "Email must not exceed 150 characters")
        String email,

        @Size(max = 30, message = "Phone number must not exceed 30 characters")
        String phoneNumber,

        @NotBlank(message = "Address is required")
        @Size(max = 255, message = "Address must not exceed 255 characters")
        String address,

        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must not exceed 100 characters")
        String city,

        @Size(max = 20, message = "Postal code must not exceed 20 characters")
        String postalCode,

        @NotBlank(message = "Country is required")
        @Size(max = 100, message = "Country must not exceed 100 characters")
        String country,

        @Size(max = 50, message = "VAT number must not exceed 50 characters")
        String vatNumber,

        @NotBlank(message = "Carrier license number is required")
        @Size(max = 100, message = "Carrier license number must not exceed 100 characters")
        String licenseNumber,

        @NotNull(message = "Active status is required")
        Boolean active
) {
}
