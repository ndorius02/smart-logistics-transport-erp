CREATE TABLE inventory
(
    id UUID PRIMARY KEY,

    product_id UUID NOT NULL,
    warehouse_id UUID NOT NULL,

    quantity NUMERIC(15,3) NOT NULL DEFAULT 0,
    reserved_quantity NUMERIC(15,3) NOT NULL DEFAULT 0,
    minimum_stock_level NUMERIC(15,3) NOT NULL DEFAULT 0,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,

    CONSTRAINT fk_inventory_product
        FOREIGN KEY (product_id)
            REFERENCES products (id),

    CONSTRAINT fk_inventory_warehouse
        FOREIGN KEY (warehouse_id)
            REFERENCES warehouse (id),

    CONSTRAINT uk_inventory_product_warehouse
        UNIQUE (product_id, warehouse_id),

    CONSTRAINT chk_inventory_quantity
        CHECK (quantity >= 0),

    CONSTRAINT chk_inventory_reserved_quantity
        CHECK (reserved_quantity >= 0),

    CONSTRAINT chk_inventory_reserved_not_greater_than_quantity
        CHECK (reserved_quantity <= quantity),

    CONSTRAINT chk_inventory_minimum_stock_level
        CHECK (minimum_stock_level >= 0)
);