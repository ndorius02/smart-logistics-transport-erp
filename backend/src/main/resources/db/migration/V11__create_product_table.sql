CREATE TABLE products
(
    id UUID NOT NULL,

    sku VARCHAR(50) NOT NULL,

    name VARCHAR(150) NOT NULL,

    description VARCHAR(500),

    category_id UUID NOT NULL,

    unit_of_measure VARCHAR(30) NOT NULL,

    weight NUMERIC(10, 3),

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    CONSTRAINT pk_products
        PRIMARY KEY (id),

    CONSTRAINT uk_products_sku
        UNIQUE (sku),

    CONSTRAINT fk_products_category
        FOREIGN KEY (category_id)
            REFERENCES product_categories (id),

    CONSTRAINT chk_products_weight_positive
        CHECK (
            weight IS NULL
                OR weight > 0
            )
);

CREATE INDEX idx_products_name
    ON products (name);

CREATE INDEX idx_products_category_id
    ON products (category_id);

CREATE INDEX idx_products_active
    ON products (active);

CREATE INDEX idx_products_unit_of_measure
    ON products (unit_of_measure);