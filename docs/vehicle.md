# Vehicle Module
## Purpose
The Vehicle module manages the fleet of vehicles used by the Smart Logistics platform.

Vehicles are used in transport operations and may later be assigned to drivers, shipments, and transport missions.

The module must also keep track of whether a vehicle is active and whether it is operationally available.
---
## Entity Overview
A vehicle contains the following information:
- ID
- Registration number
- Brand
- Model
- Vehicle type
- Load capacity
- Operational status
- Active status
- Creation date
- Last update date
---
## Business Rules
### BR-VEH-001 - Vehicle Identity
Each vehicle must have a unique identifier.
The following information is required:
- registration number
- brand
- model
- vehicle type
- load capacity
Example:
```text
Registration number: 1-ABC-123
Brand: Volvo
Model: FH16
Type: TRUCK
Load capacity: 24000
```
---
### BR-VEH-002 - Unique Registration Number
A vehicle registration number must be unique.
Two vehicles cannot have the same registration number.
Example:
```text
1-ABC-123
```
must identify only one vehicle in the system.
Registration numbers should be normalized before they are stored or compared.
---
### BR-VEH-003 - Positive Load Capacity
The load capacity of a vehicle must be greater than zero.
Valid:
```text
24000
3500
10000
```
Invalid:
```text
0
-500
```
The backend must reject invalid capacities.
---
### BR-VEH-004 - Vehicle Type
Each vehicle must have a supported vehicle type.
Initial supported values may include:
```text
TRUCK
VAN
SEMI_TRAILER
REFRIGERATED_TRUCK
```
Vehicle types should be represented using a controlled value such as a Java enum.
Additional vehicle types may be introduced later.
---
### BR-VEH-005 - Active Status
A newly created vehicle is active by default.
```text
active = true
```
An inactive vehicle remains stored in the database for historical and auditing purposes.
---
### BR-VEH-006 - Vehicle Activation and Deactivation
Vehicles should not normally be physically deleted.
Instead, the system changes their active status.
Deactivate:
```text
active = false
```
Activate:
```text
active = true
```
Planned endpoints:

```http
PATCH /api/vehicles/{id}/activate
PATCH /api/vehicles/{id}/deactivate
```
This approach preserves references from historical transport operations.
---
### BR-VEH-007 - Operational Status
The active status and operational status represent different concepts.
An active vehicle exists in the fleet and may potentially be used.
Its operational status indicates its current availability.
Initial operational statuses:
```text
AVAILABLE
ASSIGNED
MAINTENANCE
OUT_OF_SERVICE
```
#### AVAILABLE
The vehicle is active and available for a new transport assignment.
#### ASSIGNED
The vehicle is currently assigned to a transport operation.
#### MAINTENANCE
The vehicle is temporarily unavailable because it is undergoing maintenance.
#### OUT_OF_SERVICE
The vehicle cannot currently be used for transport operations.
A vehicle that is not `AVAILABLE` must not be assigned to a new transport operation.
This rule will become especially important when the Transport module is implemented.
---
### BR-VEH-008 - Vehicle Update
The following information may be updated:
- registration number
- brand
- model
- vehicle type
- load capacity
- operational status
The vehicle ID must never be modified.
Activation and deactivation should use their dedicated operations instead of a normal update request.
---
### BR-VEH-009 - Authorization
The first version of the RBAC rules is:

| Operation | ADMIN | MANAGER | TRANSPORT_COORDINATOR |
|-----------|-------|---------|-----------------------|
| View vehicles | Yes | Yes | Yes |
| Create vehicle | Yes | No | Yes |
| Update vehicle | Yes | No | Yes |
| Activate vehicle | Yes | No | Yes |
| Deactivate vehicle | Yes | No | Yes |
These permissions may evolve when additional business requirements are introduced.
---
### BR-VEH-010 - Audit Information
Each vehicle stores:
- createdAt
- updatedAt
These fields are managed automatically by the backend through the shared `Auditable` superclass.
`createdAt` is populated when the vehicle is created.
`updatedAt` is refreshed whenever the vehicle is modified.
---
## Planned API
### Create vehicle
```http
POST /api/vehicles
```
Creates a new vehicle.
---
### Get all vehicles
```http
GET /api/vehicles
```
Returns vehicles using pagination.
Example:
```http
GET /api/vehicles?page=0&size=20&sort=registrationNumber,asc
```
---
### Get vehicle by ID
```http
GET /api/vehicles/{id}
```
Returns one vehicle.
---
### Search vehicles
A search endpoint may be provided, for example:
```http
GET /api/vehicles/search?registrationNumber=ABC
```
or later:

```http
GET /api/vehicles/search?brand=Volvo
```
Search results should support pagination.
---
### Update vehicle
```http
PUT /api/vehicles/{id}
```
Updates the editable information of an existing vehicle.
---
### Activate vehicle
```http
PATCH /api/vehicles/{id}/activate
```
Activates an inactive vehicle.
---
### Deactivate vehicle
```http
PATCH /api/vehicles/{id}/deactivate
```
Deactivates a vehicle without physically deleting it.
---
## Validation
Input validation should be applied before data reaches the persistence layer.
Examples:
```text
registrationNumber → required
brand              → required
model              → required
vehicleType        → required
loadCapacity       → greater than zero
```
Database constraints should provide an additional level of protection.
For example:
```text
registration_number → UNIQUE
load_capacity       → CHECK > 0
```
---
## Error Handling
The module should use the project's centralized exception handling.
Planned error messages can be added to:
```text
ErrorMessages
```
For example:
```java
VEHICLE_NOT_FOUND =
        "Vehicle not found";
DUPLICATE_VEHICLE_REGISTRATION =
        "A vehicle with this registration number already exists";
```
Typical errors include:
```text
404 - Vehicle not found
409 - Registration number already exists
400 - Invalid input data
403 - User does not have permission
```
---
## Persistence

Vehicle data will be stored in the:
```text
vehicle
```
table.

The schema will be managed using Flyway.
The planned migration is:

```text
V3__create_vehicle_table.sql
```
Hibernate remains configured with:

```text
ddl-auto: validate
```
Therefore, Flyway is responsible for schema changes while Hibernate validates the entity mapping.
---
## Future Relationships
The Vehicle module is expected to interact with:
- Driver
- Transport
- Shipment
- Maintenance
- Warehouse
Possible future relationships include:
```text
Vehicle → Transport
Vehicle → Driver
Vehicle → MaintenanceRecord
```
These relationships are deliberately not implemented in the first version to keep the module loosely coupled.
---
## Design Notes
The Vehicle module follows the same architecture used by the existing User, Role, and Warehouse modules:
```text
Controller
↓
Service
    ↓
Repository
    ↓
Database
```
DTOs are used to isolate the REST API from JPA entities.
MapStruct is used for object mapping.
Pagination is used for collection endpoints.
Authorization rules are centralized in `AuthorizationRules`.
Audit information is inherited from `Auditable`.
Business error messages are centralized in `ErrorMessages`.