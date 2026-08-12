# Transport Module
## 1. Purpose
The Transport module manages transport operations in the Smart Logistics platform.

A transport represents an operational movement between two warehouses and links:
- one origin warehouse;
- one destination warehouse;
- one vehicle;
- one driver.
The module manages the complete lifecycle of a transport, from planning to completion or cancellation.

The Transport module depends on the following existing modules:
- Warehouse;
- Vehicle;
- Driver.
---

## 2. Entity Overview
A transport contains the following information:
- ID;
- transport code;
- origin warehouse;
- destination warehouse;
- vehicle;
- driver;
- planned departure date/time;
- planned arrival date/time;
- actual departure date/time;
- actual arrival date/time;
- transport status;
- creation date;
- last update date.
Example transport code:
```text
TR-2026-0001
```
---

# 3. Business Rules

## BR-TR-001 - Transport Identity
Each transport must have a unique technical identifier.
The identifier is represented by a UUID.
Example:
```text
550e8400-e29b-41d4-a716-446655440000
```
Each transport must also have a unique business code.
Example:
```text
TR-2026-0001
```
The technical UUID is used internally by the application.
The transport code is the business identifier used by users and operational processes.
---

## BR-TR-002 - Unique Transport Code
The transport code must be unique.
Two transports cannot have the same code.
Before validation and persistence, the backend should normalize the transport code using:
```text
trim + uppercase
```
Example:
```text
 tr-2026-0001
```
becomes:
```text
TR-2026-0001
```
The uniqueness rule must be protected at two levels:
1. service layer validation;
2. database UNIQUE constraint.
---
## BR-TR-003 - Origin Warehouse Required
Every transport must have an origin warehouse.
The origin warehouse must exist in the database.
If the warehouse does not exist, the transport creation or update must be rejected.
---
## BR-TR-004 - Destination Warehouse Required
Every transport must have a destination warehouse.
The destination warehouse must exist in the database.
If the warehouse does not exist, the transport creation or update must be rejected.
---
## BR-TR-005 - Different Origin and Destination
The origin warehouse and destination warehouse must be different.
The following configuration is invalid:
```text
originWarehouseId == destinationWarehouseId
```
Example:
```text
Brussels Warehouse
        ↓
Brussels Warehouse
```
This transport must be rejected.
A valid transport looks like:
```text
Brussels Warehouse
        ↓
Antwerp Warehouse
```
---
## BR-TR-006 - Active Warehouses
Both the origin warehouse and destination warehouse must be active when a transport is created or modified.
Required:

```text
originWarehouse.active = true
destinationWarehouse.active = true
```
An inactive warehouse cannot participate in a new transport operation.
---
## BR-TR-007 - Vehicle Required
Every transport must have a vehicle.
The selected vehicle must exist in the database.
If the vehicle does not exist, the transport operation must be rejected.
---
## BR-TR-008 - Vehicle Eligibility
A vehicle selected for a transport must be active.
Required:
```text
vehicle.active = true
```
The vehicle must also be operationally available:
```text
vehicle.operationalStatus = AVAILABLE
```
Therefore, a vehicle with one of the following statuses cannot start a new transport:
```text
ASSIGNED
MAINTENANCE
OUT_OF_SERVICE
```
The exact available statuses depend on the `VehicleStatus` enum defined by the Vehicle module.
---
## BR-TR-009 - Driver Required
Every transport must have a driver.
The selected driver must exist in the database.
If the driver does not exist, the transport operation must be rejected.
---
## BR-TR-010 - Driver Eligibility
A driver selected for a transport must be active.
Required:
```text
driver.active = true
```
The driver must also be operationally available:
```text
driver.status = AVAILABLE
```
Therefore, drivers with the following statuses cannot start a new transport:
```text
ASSIGNED
ON_LEAVE
SUSPENDED
```
---
## BR-TR-011 - Planned Departure
Every transport must have a planned departure date/time.
Example:
```text
2026-08-15T08:00:00
```
The planned departure represents when the transport is expected to begin.
---
## BR-TR-012 - Planned Arrival
Every transport must have a planned arrival date/time.
Example:
```text
2026-08-15T14:00:00
```
The planned arrival represents when the transport is expected to reach the destination warehouse.
---
## BR-TR-013 - Planned Date Consistency
The planned departure date/time must occur before the planned arrival date/time.
Required:
```text
plannedDepartureAt < plannedArrivalAt
```
Invalid example:

```text
plannedDepartureAt = 2026-08-15T14:00

plannedArrivalAt   = 2026-08-15T08:00
```
The backend must reject this transport.
---
# 4. Transport Status
The first version of the Transport module supports the following statuses:
```text
PLANNED
IN_PROGRESS
COMPLETED
CANCELLED
```
These values are represented by:
```java
public enum TransportStatus {
    PLANNED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
```
The database stores the enum values as strings.
---
## BR-TR-014 - Initial Transport Status
A newly created transport always starts with:
```text
status = PLANNED
```
The API client must not decide the initial status.
The backend is responsible for assigning:
```text
PLANNED
```
during transport creation.
---
# 5. Transport Lifecycle
The normal lifecycle is:
```text
                 start
PLANNED --------------------> IN_PROGRESS
   |                              |
   |                              |
   | cancel                       | complete
   ↓                              ↓
CANCELLED                     COMPLETED
```
The following transitions are allowed:
```text
PLANNED → IN_PROGRESS
PLANNED → CANCELLED
IN_PROGRESS → COMPLETED
```
Invalid transitions must be rejected.
Examples of invalid transitions:
```text
COMPLETED → IN_PROGRESS
COMPLETED → PLANNED
CANCELLED → IN_PROGRESS
CANCELLED → COMPLETED
IN_PROGRESS → PLANNED
```
---
# 6. Transport Creation
## BR-TR-015 - Creating a Transport
When a transport is created, the backend must:
1. normalize the transport code;
2. verify that the code is unique;
3. load the origin warehouse;
4. load the destination warehouse;
5. verify that origin and destination are different;
6. verify that both warehouses are active;
7. load the vehicle;
8. verify that the vehicle is active;
9. verify that the vehicle is available;
10. load the driver;
11. verify that the driver is active;
12. verify that the driver is available;
13. validate the planned dates;
14. create the transport;
15. assign `PLANNED` as the initial status;
16. persist the transport.
---
## BR-TR-016 - Resource Reservation at Creation
Creating a `PLANNED` transport does not immediately change:
```text
vehicle.operationalStatus
```
or:
```text
driver.status
```
Therefore, immediately after transport creation:
```text
Transport.status = PLANNED
Vehicle.operationalStatus = AVAILABLE
Driver.status = AVAILABLE
```
The actual operational assignment occurs when the transport starts.
This keeps the first version of the workflow simple.
---
# 7. Starting a Transport
The dedicated endpoint is:
```http
PATCH /api/transports/{id}/start
```
---
## BR-TR-017 - Start Allowed Only from PLANNED
Only a transport with:
```text
status = PLANNED
```
may be started.
The transition is:
```text
PLANNED → IN_PROGRESS
```
A transport already:
```text
IN_PROGRESS
COMPLETED
CANCELLED
```
cannot be started.
---
## BR-TR-018 - Revalidate Vehicle Before Start
Before starting the transport, the backend must verify again:
```text
vehicle.active = true
```
and:
```text
vehicle.operationalStatus = AVAILABLE
```
This validation is important because the state of the vehicle may have changed since the transport was planned.
---
## BR-TR-019 - Revalidate Driver Before Start
Before starting the transport, the backend must verify again:
```text
driver.active = true
```
and:
```text
driver.status = AVAILABLE
```
This validation is important because the state of the driver may have changed since the transport was planned.
---
## BR-TR-020 - Resource Assignment
When the transport successfully starts:
```text
Transport.status = IN_PROGRESS
```
The vehicle becomes:
```text
Vehicle.operationalStatus = ASSIGNED
```
The driver becomes:
```text
Driver.status = ASSIGNED
```
The backend also records:
```text
actualDepartureAt
```
---
# 8. Completing a Transport
The dedicated endpoint is:

```http
PATCH /api/transports/{id}/complete
```
---
## BR-TR-021 - Complete Only an Active Transport
Only a transport with:
```text
status = IN_PROGRESS
```
can be completed.
The valid transition is:
```text
IN_PROGRESS → COMPLETED
```
A transport with:
```text
PLANNED
COMPLETED
CANCELLED
```
cannot be completed.
---
## BR-TR-022 - Actual Arrival
When the transport is completed, the backend records:
```text
actualArrivalAt
```
The value represents the actual completion time of the transport.
---
## BR-TR-023 - Release Vehicle
When a transport is successfully completed:
```text
Vehicle.operationalStatus = AVAILABLE
```
The vehicle becomes available for another transport operation.
---
## BR-TR-024 - Release Driver
When a transport is successfully completed:
```text
Driver.status = AVAILABLE
```
The driver becomes available for another transport operation.
---
# 9. Cancelling a Transport
The dedicated endpoint is:
```http
PATCH /api/transports/{id}/cancel
```
---
## BR-TR-025 - Cancel Planned Transport
In the first version, only a transport with:
```text
status = PLANNED
```
can be cancelled.
The transition is:
```text
PLANNED → CANCELLED
```
---
## BR-TR-026 - Completed Transport Cannot Be Cancelled
A transport with:
```text
status = COMPLETED
```
cannot be cancelled.
Completed transports represent historical operations and must remain unchanged.
---
## BR-TR-027 - In-Progress Cancellation
The first version does not allow:
```text
IN_PROGRESS → CANCELLED
```
Handling operational incidents during an active transport may be introduced later with additional business rules or statuses.
---
# 10. Transport Update
The endpoint is:

```http
PUT /api/transports/{id}
```
---
## BR-TR-028 - Update Only Planned Transport
Only transports with:
```text
status = PLANNED
```
may be structurally modified.
The following information may be updated:
- transport code;
- origin warehouse;
- destination warehouse;
- vehicle;
- driver;
- planned departure date/time;
- planned arrival date/time.
---
## BR-TR-029 - Status Cannot Be Updated Directly
The normal update endpoint must not allow the client to directly modify:
```text
status
```
Status transitions must use dedicated endpoints:
```http
PATCH /api/transports/{id}/start
PATCH /api/transports/{id}/complete
PATCH /api/transports/{id}/cancel
```
This ensures that all business rules are executed.
---
## BR-TR-030 - Actual Dates Cannot Be Updated Directly
The client must not directly modify:
```text
actualDepartureAt
actualArrivalAt
```
These values are controlled by the backend.
`actualDepartureAt` is set when the transport starts.
`actualArrivalAt` is set when the transport completes.
---

# 11. No Physical Deletion
## BR-TR-031 - Preserve Transport History
Transport records must not normally be physically deleted.
Therefore, the API does not provide:
```http
DELETE /api/transports/{id}
```
A planned transport that will no longer take place must use:
```text
status = CANCELLED
```
This preserves historical and operational traceability.
---
# 12. Audit Information
`Transport` inherits from:
```java
Auditable
```
Therefore, it automatically contains:
```text
createdAt
updatedAt
```
The application manages these values through JPA lifecycle callbacks.
The client must not provide or modify these fields.
---

# 13. Relationships
The Transport entity references four operational resources:
```text
Transport
├── originWarehouse
│       └── Warehouse
├── destinationWarehouse
│       └── Warehouse
├── vehicle
│       └── Vehicle
└── driver
        └── Driver
```
The database therefore contains foreign keys for:

```text
origin_warehouse_id
destination_warehouse_id
vehicle_id
driver_id
```
---
# 14. Driver and Vehicle Relationship
Driver and Vehicle do not have a permanent direct relationship.
The relationship is established through Transport:
```text
Driver
   \
    \
     Transport
    /
   /
Vehicle
```
This allows the application to preserve the historical assignment of drivers and vehicles for every transport operation.
---
# 15. Authorization
The Transport module uses centralized authorization rules through:
```java
AuthorizationRules
```
The initial RBAC matrix is:

| Operation | ADMIN | MANAGER | TRANSPORT_COORDINATOR |
|-----------|:-----:|:-------:|:---------------------:|
| View transports | Yes | Yes | Yes |
| Search transports | Yes | Yes | Yes |
| Create transport | Yes | No | Yes |
| Update planned transport | Yes | No | Yes |
| Start transport | Yes | No | Yes |
| Complete transport | Yes | No | Yes |
| Cancel transport | Yes | No | Yes |

The `MANAGER` role has read-only access to Transport operations.

The `ADMIN` and `TRANSPORT_COORDINATOR` roles manage operational transport activities.
---