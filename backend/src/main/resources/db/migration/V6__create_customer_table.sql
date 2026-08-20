CREATE TABLE customers
(
    id UUID NOT NULL,

    code VARCHAR(50) NOT NULL,

    company_name VARCHAR(150) NOT NULL,

    contact_name VARCHAR(150),

    email VARCHAR(150),

    phone_number VARCHAR(30),

    address VARCHAR(255) NOT NULL,

    city VARCHAR(100) NOT NULL,

    postal_code VARCHAR(20),

    country VARCHAR(100) NOT NULL,

    vat_number VARCHAR(50),

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT pk_customers
        PRIMARY KEY (id),

    CONSTRAINT uk_customers_code
        UNIQUE (code),

    CONSTRAINT uk_customers_vat_number
        UNIQUE (vat_number)
);


-- =========================================
-- INDEXES
-- =========================================

CREATE INDEX idx_customers_company_name
    ON customers (company_name);

CREATE INDEX idx_customers_city
    ON customers (city);

CREATE INDEX idx_customers_active
    ON customers (active);