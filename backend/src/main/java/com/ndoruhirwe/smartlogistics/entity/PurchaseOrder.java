package com.ndoruhirwe.smartlogistics.entity;

import com.ndoruhirwe.smartlogistics.entity.enums.PurchaseOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "purchase_orders", uniqueConstraints = {@UniqueConstraint(
                        name = "uk_purchase_orders_order_number",
                        columnNames = "order_number")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrder extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_number", nullable = false, length = 50)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_purchase_orders_supplier")
    )
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_purchase_orders_warehouse")
    )
    private Warehouse warehouse;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PurchaseOrderStatus status;

    @Column(length = 500)
    private String notes;

    @Builder.Default
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderItem> items = new ArrayList<>();
}
