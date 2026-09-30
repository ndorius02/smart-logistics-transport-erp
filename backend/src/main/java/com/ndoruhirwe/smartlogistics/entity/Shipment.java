package com.ndoruhirwe.smartlogistics.entity;

import com.ndoruhirwe.smartlogistics.entity.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shipment",
        uniqueConstraints = {@UniqueConstraint(
                        name = "uk_shipment_number", columnNames = "shipment_number")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Shipment extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "shipment_number", nullable = false, length = 50)
    private String shipmentNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "origin_warehouse_id", nullable = false)
    private Warehouse originWarehouse;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destination_warehouse_id", nullable = false)
    private Warehouse destinationWarehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_id")
    private Transport transport;

    @Column(name = "planned_pickup_date", nullable = false)
    private LocalDateTime plannedPickupDate;

    @Column(name = "planned_delivery_date", nullable = false)
    private LocalDateTime plannedDeliveryDate;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ShipmentStatus status = ShipmentStatus.DRAFT;

    @Column(length = 500)
    private String notes;
}
