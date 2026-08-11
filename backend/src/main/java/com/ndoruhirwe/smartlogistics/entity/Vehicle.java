package com.ndoruhirwe.smartlogistics.entity;

import com.ndoruhirwe.smartlogistics.entity.enums.VehicleStatus;
import com.ndoruhirwe.smartlogistics.entity.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "vehicle",
        uniqueConstraints = {@UniqueConstraint(
                name = "uk_vehicle_registration_number", columnNames = "registration_number"
        )}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Vehicle extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "registration_number", nullable = false, length = 50)
    private String registrationNumber;

    @Column(nullable = false, length = 100)
    private String brand;

    @Column(nullable = false, length = 100)
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, length = 50)
    private VehicleType vehicleType;

    @Column(name = "load_capacity", nullable = false)
    private Integer loadCapacity;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "operational_status", nullable = false, length = 50)
    private VehicleStatus operationalStatus = VehicleStatus.AVAILABLE;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}
