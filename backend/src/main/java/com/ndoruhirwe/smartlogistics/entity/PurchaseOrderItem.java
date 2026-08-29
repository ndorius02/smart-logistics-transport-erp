package com.ndoruhirwe.smartlogistics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "purchase_order_items",
        uniqueConstraints = {@UniqueConstraint(name = "uk_purchase_order_items_product",
                        columnNames = {"purchase_order_id", "product_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PurchaseOrderItem extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_order_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_purchase_order_items_purchase_order")
    )
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_purchase_order_items_product")
    )
    private Product product;

    @Column(name = "ordered_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal orderedQuantity;

    @Builder.Default
    @Column(name = "received_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal receivedQuantity = BigDecimal.ZERO;

    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;
}
