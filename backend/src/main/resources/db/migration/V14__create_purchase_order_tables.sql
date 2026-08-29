CREATE TABLE purchase_orders
(
    id UUID PRIMARY KEY,

    order_number VARCHAR(50) NOT NULL,

    supplier_id UUID NOT NULL,
    warehouse_id UUID NOT NULL,

    order_date DATE NOT NULL,

    expected_delivery_date DATE,

    status VARCHAR(30) NOT NULL,

    notes VARCHAR(500),

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,

    CONSTRAINT uk_purchase_orders_order_number
        UNIQUE (order_number),

    CONSTRAINT fk_purchase_orders_supplier
        FOREIGN KEY (supplier_id)
            REFERENCES suppliers (id),

    CONSTRAINT fk_purchase_orders_warehouse
        FOREIGN KEY (warehouse_id)
            REFERENCES warehouse (id)
);


CREATE TABLE purchase_order_items
(
    id UUID PRIMARY KEY,

    purchase_order_id UUID NOT NULL,

    product_id UUID NOT NULL,

    ordered_quantity NUMERIC(15,3) NOT NULL,

    received_quantity NUMERIC(15,3) NOT NULL DEFAULT 0,

    unit_price NUMERIC(15,2) NOT NULL,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,

    CONSTRAINT fk_purchase_order_items_purchase_order
        FOREIGN KEY (purchase_order_id)
            REFERENCES purchase_orders (id),

    CONSTRAINT fk_purchase_order_items_product
        FOREIGN KEY (product_id)
            REFERENCES products (id),

    CONSTRAINT uk_purchase_order_items_product
        UNIQUE (
                purchase_order_id,
                product_id
            ),

    CONSTRAINT chk_purchase_order_items_ordered_quantity
        CHECK (ordered_quantity > 0),

    CONSTRAINT chk_purchase_order_items_received_quantity
        CHECK (received_quantity >= 0),

    CONSTRAINT chk_purchase_order_items_received_not_greater_than_ordered
        CHECK (
            received_quantity
                <= ordered_quantity
            ),

    CONSTRAINT chk_purchase_order_items_unit_price
        CHECK (unit_price >= 0)
);


CREATE INDEX idx_purchase_orders_supplier_id
    ON purchase_orders (supplier_id);

CREATE INDEX idx_purchase_orders_warehouse_id
    ON purchase_orders (warehouse_id);

CREATE INDEX idx_purchase_orders_status
    ON purchase_orders (status);

CREATE INDEX idx_purchase_order_items_purchase_order_id
    ON purchase_order_items (purchase_order_id);

CREATE INDEX idx_purchase_order_items_product_id
    ON purchase_order_items (product_id);