CREATE TABLE driver (
                        id UUID PRIMARY KEY,

                        first_name VARCHAR(100) NOT NULL,
                        last_name VARCHAR(100) NOT NULL,

                        license_number VARCHAR(50) NOT NULL,

                        phone_number VARCHAR(30) NOT NULL,

                        status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',

                        active BOOLEAN NOT NULL DEFAULT TRUE,

                        created_at TIMESTAMP NOT NULL,
                        updated_at TIMESTAMP,

                        CONSTRAINT uk_driver_license_number
                            UNIQUE (license_number),

                        CONSTRAINT chk_driver_status
                            CHECK (
                                status IN (
                                           'AVAILABLE',
                                           'ASSIGNED',
                                           'ON_LEAVE',
                                           'SUSPENDED'
                                    )
                                )
);