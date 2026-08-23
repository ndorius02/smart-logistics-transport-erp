CREATE TABLE product_categories
(
    id          UUID PRIMARY KEY,

    code        VARCHAR(50)  NOT NULL,
    name        VARCHAR(150) NOT NULL,
    description VARCHAR(500),

    active      BOOLEAN      NOT NULL DEFAULT TRUE,

    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP,

    CONSTRAINT uk_product_categories_code
        UNIQUE (code)
);

CREATE UNIQUE INDEX uk_product_categories_name_ci
    ON product_categories (LOWER(name));