package com.ndoruhirwe.smartlogistics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "goods_receptions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_goods_receptions_reference",
                        columnNames = "reference")}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GoodsReception extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String reference;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_order_item_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_goods_receptions_purchase_order_item"))
    private PurchaseOrderItem purchaseOrderItem;

    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal quantity;

    @Column(length = 500)
    private String notes;

    @Column(name = "reception_date", nullable = false)
    private LocalDateTime receptionDate;

    @Column(name = "created_by", nullable = false, length = 150)
    private String createdBy;
}
