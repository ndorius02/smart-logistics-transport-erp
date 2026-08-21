CREATE TABLE carriers
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

    license_number VARCHAR(100) NOT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP,

    CONSTRAINT pk_carriers
        PRIMARY KEY (id),

    CONSTRAINT uk_carriers_code
        UNIQUE (code),

    CONSTRAINT uk_carriers_vat_number
        UNIQUE (vat_number),

    CONSTRAINT uk_carriers_license_number
        UNIQUE (license_number)
);

CREATE INDEX idx_carriers_company_name
    ON carriers (company_name);

CREATE INDEX idx_carriers_city
    ON carriers (city);

CREATE INDEX idx_carriers_active
    ON carriers (active);

CREATE INDEX idx_carriers_license_number
    ON carriers (license_number);