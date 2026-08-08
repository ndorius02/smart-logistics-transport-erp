CREATE TABLE warehouse (
                           id UUID PRIMARY KEY,

                           code VARCHAR(50) NOT NULL UNIQUE,

                           name VARCHAR(150) NOT NULL,

                           address VARCHAR(255) NOT NULL,

                           city VARCHAR(100) NOT NULL,

                           country VARCHAR(100) NOT NULL,

                           capacity INTEGER NOT NULL,

                           active BOOLEAN NOT NULL DEFAULT TRUE,

                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                           updated_at TIMESTAMP,

                           CONSTRAINT chk_warehouse_capacity_positive
                               CHECK (capacity > 0)
);