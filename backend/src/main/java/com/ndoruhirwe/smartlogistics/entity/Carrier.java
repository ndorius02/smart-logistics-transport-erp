package com.ndoruhirwe.smartlogistics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "carriers", uniqueConstraints = {
                @UniqueConstraint(name = "uk_carriers_code", columnNames = "code"),
                @UniqueConstraint(name = "uk_carriers_vat_number",
                        columnNames = "vat_number"),
                @UniqueConstraint(name = "uk_carriers_license_number",
                        columnNames = "license_number")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carrier extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    @Column(name = "contact_name", length = 150)
    private String contactName;

    @Column(length = 150)
    private String email;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(name = "vat_number", length = 50)
    private String vatNumber;

    @Column(name = "license_number", nullable = false, length = 100)
    private String licenseNumber;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}
