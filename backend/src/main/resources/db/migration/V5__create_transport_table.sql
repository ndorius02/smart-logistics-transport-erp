CREATE TABLE transport (
                           id UUID PRIMARY KEY,

                           code VARCHAR(50) NOT NULL,

                           origin_warehouse_id UUID NOT NULL,
                           destination_warehouse_id UUID NOT NULL,

                           vehicle_id UUID NOT NULL,
                           driver_id UUID NOT NULL,

                           planned_departure_at TIMESTAMP NOT NULL,
                           planned_arrival_at TIMESTAMP NOT NULL,

                           actual_departure_at TIMESTAMP,
                           actual_arrival_at TIMESTAMP,

                           status VARCHAR(50) NOT NULL DEFAULT 'PLANNED',

                           created_at TIMESTAMP NOT NULL,
                           updated_at TIMESTAMP,

                           CONSTRAINT uk_transport_code
                               UNIQUE (code),

                           CONSTRAINT fk_transport_origin_warehouse
                               FOREIGN KEY (origin_warehouse_id)
                                   REFERENCES warehouse(id),

                           CONSTRAINT fk_transport_destination_warehouse
                               FOREIGN KEY (destination_warehouse_id)
                                   REFERENCES warehouse(id),

                           CONSTRAINT fk_transport_vehicle
                               FOREIGN KEY (vehicle_id)
                                   REFERENCES vehicle(id),

                           CONSTRAINT fk_transport_driver
                               FOREIGN KEY (driver_id)
                                   REFERENCES driver(id),

                           CONSTRAINT chk_transport_different_warehouses
                               CHECK (
                                   origin_warehouse_id <> destination_warehouse_id
                                   ),

                           CONSTRAINT chk_transport_planned_dates
                               CHECK (
                                   planned_departure_at < planned_arrival_at
                                   ),

                           CONSTRAINT chk_transport_actual_dates
                               CHECK (
                                   actual_departure_at IS NULL
                                       OR actual_arrival_at IS NULL
                                       OR actual_departure_at <= actual_arrival_at
                                   ),

                           CONSTRAINT chk_transport_status
                               CHECK (
                                   status IN (
                                              'PLANNED',
                                              'IN_PROGRESS',
                                              'COMPLETED',
                                              'CANCELLED'
                                       )
                                   )
);