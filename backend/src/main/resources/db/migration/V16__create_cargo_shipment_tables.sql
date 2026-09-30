
CREATE TABLE shipment
(
    id                      UUID         NOT NULL,
    shipment_number         VARCHAR(50)  NOT NULL,
    customer_id             UUID         NOT NULL,
    origin_warehouse_id     UUID         NOT NULL,
    destination_warehouse_id UUID        NOT NULL,
    transport_id            UUID,
    planned_pickup_date     TIMESTAMP    NOT NULL,
    planned_delivery_date   TIMESTAMP    NOT NULL,
    status                  VARCHAR(50)  NOT NULL,
    notes                   VARCHAR(500),
    created_at              TIMESTAMP    NOT NULL,
    updated_at              TIMESTAMP,

    CONSTRAINT pk_shipment
        PRIMARY KEY (id),

    CONSTRAINT uk_shipment_number
        UNIQUE (shipment_number),

    CONSTRAINT fk_shipment_customer
        FOREIGN KEY (customer_id)
            REFERENCES customer (id),

    CONSTRAINT fk_shipment_origin_warehouse
        FOREIGN KEY (origin_warehouse_id)
            REFERENCES warehouse (id),

    CONSTRAINT fk_shipment_destination_warehouse
        FOREIGN KEY (destination_warehouse_id)
            REFERENCES warehouse (id),

    CONSTRAINT fk_shipment_transport
        FOREIGN KEY (transport_id)
            REFERENCES transport (id),

    CONSTRAINT chk_shipment_different_warehouses
        CHECK (origin_warehouse_id <> destination_warehouse_id),

    CONSTRAINT chk_shipment_dates
        CHECK (planned_delivery_date >= planned_pickup_date)
);


CREATE TABLE cargo
(
    id                      UUID          NOT NULL,
    cargo_number            VARCHAR(50)   NOT NULL,
    shipment_id             UUID          NOT NULL,
    description             VARCHAR(255)  NOT NULL,
    cargo_type              VARCHAR(50)   NOT NULL,
    quantity                DECIMAL(15,3) NOT NULL,
    unit                    VARCHAR(50)   NOT NULL,
    weight                  DECIMAL(15,3),
    volume                  DECIMAL(15,3),
    special_instructions    VARCHAR(500),
    created_at              TIMESTAMP     NOT NULL,
    updated_at              TIMESTAMP,

    CONSTRAINT pk_cargo
        PRIMARY KEY (id),

    CONSTRAINT uk_cargo_number
        UNIQUE (cargo_number),

    CONSTRAINT fk_cargo_shipment
        FOREIGN KEY (shipment_id)
            REFERENCES shipment (id),

    CONSTRAINT chk_cargo_quantity_positive
        CHECK (quantity > 0),

    CONSTRAINT chk_cargo_weight_non_negative
        CHECK (weight IS NULL OR weight >= 0),

    CONSTRAINT chk_cargo_volume_non_negative
        CHECK (volume IS NULL OR volume >= 0)
);

CREATE INDEX idx_shipment_customer
    ON shipment (customer_id);

CREATE INDEX idx_shipment_origin_warehouse
    ON shipment (origin_warehouse_id);

CREATE INDEX idx_shipment_destination_warehouse
    ON shipment (destination_warehouse_id);

CREATE INDEX idx_shipment_transport
    ON shipment (transport_id);

CREATE INDEX idx_shipment_status
    ON shipment (status);

CREATE INDEX idx_cargo_shipment
    ON cargo (shipment_id);

CREATE INDEX idx_cargo_type
    ON cargo (cargo_type);