package com.ndoruhirwe.smartlogistics.entity;

import com.ndoruhirwe.smartlogistics.entity.enums.CargoType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "cargo",
        uniqueConstraints = {@UniqueConstraint(name = "uk_cargo_number",
                        columnNames = "cargo_number")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cargo extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "cargo_number", nullable = false, length = 50)
    private String cargoNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    @Column(nullable = false, length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "cargo_type", nullable = false, length = 50)
    private CargoType cargoType;

    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal quantity;

    @Column(nullable = false, length = 50)
    private String unit;

    @Column(precision = 15, scale = 3)
    private BigDecimal weight;

    @Column(precision = 15, scale = 3)
    private BigDecimal volume;

    @Column(name = "special_instructions", length = 500)
    private String specialInstructions;

}
