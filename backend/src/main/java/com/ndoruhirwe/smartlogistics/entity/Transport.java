package com.ndoruhirwe.smartlogistics.entity;

import com.ndoruhirwe.smartlogistics.entity.enums.TransportStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transport",
        uniqueConstraints = {@UniqueConstraint(
                name = "uk_transport_code", columnNames = "code")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Transport extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "origin_warehouse_id", nullable = false)
    private Warehouse originWarehouse;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destination_warehouse_id", nullable = false
    )
    private Warehouse destinationWarehouse;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(name = "planned_departure_at", nullable = false)
    private LocalDateTime plannedDepartureAt;

    @Column(name = "planned_arrival_at", nullable = false)
    private LocalDateTime plannedArrivalAt;

    @Column(name = "actual_departure_at")
    private LocalDateTime actualDepartureAt;

    @Column(name = "actual_arrival_at")
    private LocalDateTime actualArrivalAt;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TransportStatus status = TransportStatus.PLANNED;
}
