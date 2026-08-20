CREATE TABLE suppliers
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

    updated_at TIMESTAMP,

    CONSTRAINT pk_suppliers
        PRIMARY KEY (id),

    CONSTRAINT uk_suppliers_code
        UNIQUE (code),

    CONSTRAINT uk_suppliers_vat_number
        UNIQUE (vat_number)
);

CREATE INDEX idx_suppliers_company_name
    ON suppliers (company_name);

CREATE INDEX idx_suppliers_city
    ON suppliers (city);

CREATE INDEX idx_suppliers_active
    ON suppliers (active);