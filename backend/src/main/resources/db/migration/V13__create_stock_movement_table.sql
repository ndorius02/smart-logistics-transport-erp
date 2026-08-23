CREATE TABLE stock_movements
(
    id UUID PRIMARY KEY,

    reference VARCHAR(50) NOT NULL,

    product_id UUID NOT NULL,
    warehouse_id UUID NOT NULL,

    movement_type VARCHAR(30) NOT NULL,

    quantity NUMERIC(15,3) NOT NULL,

    reason VARCHAR(255),

    notes VARCHAR(500),

    movement_date TIMESTAMP NOT NULL,

    created_by VARCHAR(150) NOT NULL,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,

    CONSTRAINT uk_stock_movements_reference
        UNIQUE (reference),

    CONSTRAINT fk_stock_movements_product
        FOREIGN KEY (product_id)
            REFERENCES products (id),

    CONSTRAINT fk_stock_movements_warehouse
        FOREIGN KEY (warehouse_id)
            REFERENCES warehouse (id),

    CONSTRAINT chk_stock_movements_quantity_positive
        CHECK (quantity > 0)
);

CREATE INDEX idx_stock_movements_product_id
    ON stock_movements (product_id);

CREATE INDEX idx_stock_movements_warehouse_id
    ON stock_movements (warehouse_id);

CREATE INDEX idx_stock_movements_movement_type
    ON stock_movements (movement_type);

CREATE INDEX idx_stock_movements_movement_date
    ON stock_movements (movement_date);