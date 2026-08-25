package com.ndoruhirwe.smartlogistics.entity;

import com.ndoruhirwe.smartlogistics.entity.enums.StockMovementType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stock_movements",
        uniqueConstraints = {@UniqueConstraint(name = "uk_stock_movements_reference",
                        columnNames = "reference")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class StockMovement extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String reference;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_stock_movements_product")
    )
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_stock_movements_warehouse"))
    private Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 30)
    private StockMovementType movementType;

    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal quantity;

    @Column(length = 255)
    private String reason;

    @Column(length = 500)
    private String notes;

    @Column(name = "movement_date", nullable = false)
    private LocalDateTime movementDate;

    @Column(name = "created_by", nullable = false, length = 150)
    private String createdBy;
}
