CREATE TABLE goods_receptions
(
    id UUID PRIMARY KEY,

    reference VARCHAR(50) NOT NULL,

    purchase_order_item_id UUID NOT NULL,

    quantity NUMERIC(15,3) NOT NULL,

    notes VARCHAR(500),

    reception_date TIMESTAMP NOT NULL,

    created_by VARCHAR(150) NOT NULL,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,

    CONSTRAINT uk_goods_receptions_reference
        UNIQUE (reference),

    CONSTRAINT fk_goods_receptions_purchase_order_item
        FOREIGN KEY (purchase_order_item_id)
            REFERENCES purchase_order_items (id),

    CONSTRAINT chk_goods_receptions_quantity
        CHECK (quantity > 0)
);


CREATE INDEX idx_goods_receptions_purchase_order_item_id
    ON goods_receptions (purchase_order_item_id);

CREATE INDEX idx_goods_receptions_reception_date
    ON goods_receptions (reception_date);