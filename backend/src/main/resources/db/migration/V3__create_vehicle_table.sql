CREATE TABLE vehicle (
                         id UUID PRIMARY KEY,

                         registration_number VARCHAR(50) NOT NULL,
                         brand VARCHAR(100) NOT NULL,
                         model VARCHAR(100) NOT NULL,
                         vehicle_type VARCHAR(50) NOT NULL,

                         load_capacity INTEGER NOT NULL,

                         operational_status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',

                         active BOOLEAN NOT NULL DEFAULT TRUE,

                         created_at TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP,

                         CONSTRAINT uk_vehicle_registration_number
                             UNIQUE (registration_number),

                         CONSTRAINT chk_vehicle_load_capacity
                             CHECK (load_capacity > 0),

                         CONSTRAINT chk_vehicle_operational_status
                             CHECK (
                                 operational_status IN (
                                                        'AVAILABLE',
                                                        'ASSIGNED',
                                                        'MAINTENANCE',
                                                        'OUT_OF_SERVICE'
                                     )
                                 ),

                         CONSTRAINT chk_vehicle_type
                             CHECK (
                                 vehicle_type IN (
                                                  'TRUCK',
                                                  'VAN',
                                                  'SEMI_TRAILER',
                                                  'REFRIGERATED_TRUCK'
                                     )
                                 )
);