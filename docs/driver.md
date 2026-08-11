# Driver Module

## Purpose

The Driver module manages drivers used by the Smart Logistics platform.
Drivers can later be assigned to transport operations together with vehicles.

The module keeps track of:
- driver identity
- driving license
- operational availability
- active/inactive status
- audit information
---
## Entity Overview
A driver contains:
- ID
- First name
- Last name
- License number
- Phone number
- Operational status
- Active status
- Creation date
- Last update date
---
## Business Rules
### BR-DRV-001 - Driver Identity
Each driver must have a unique identifier.
Required information:
- first name
- last name
- license number
- phone number
---
### BR-DRV-002 - Unique Driving License Number
A driving license number must be unique.
Two drivers cannot have the same license number.
Example:
```text
BE-DRV-2026-001
```
The license number should be normalized before persistence or comparison.
---
### BR-DRV-003 - Driving License Required
A driver cannot be created without a license number.
The application must validate this field before persistence.
Database constraints must also prevent null values.
---
### BR-DRV-004 - Active Status
A newly created driver is active by default.
```text
active = true
```
Inactive drivers remain stored in the database.
Drivers are not physically deleted in normal business operations.
---
### BR-DRV-005 - Driver Activation and Deactivation
Drivers can be activated or deactivated explicitly.
Planned endpoints:
```http
PATCH /api/drivers/{id}/activate
PATCH /api/drivers/{id}/deactivate
```
Deactivation sets:
```text
active = false
```
Activation sets:
```text
active = true
```
---
### BR-DRV-006 - Operational Status
The active status and operational status represent different business concepts.
Initial statuses:
```text
AVAILABLE
ASSIGNED
ON_LEAVE
SUSPENDED
```
#### AVAILABLE
The driver is active and available for transport assignment.
#### ASSIGNED
The driver is currently assigned to a transport operation.
#### ON_LEAVE
The driver temporarily belongs to the organization but is not available for assignment.
#### SUSPENDED
The driver cannot currently perform transport operations.
---
### BR-DRV-007 - Transport Eligibility
A driver may be assigned to a new transport operation only when:
```text
active = true
AND
status = AVAILABLE
```
This rule will be enforced mainly in the Transport module.
---
### BR-DRV-008 - Driver Update
The following fields may be updated:
- first name
- last name
- license number
- phone number
- operational status
The driver ID must never be changed.
The active status should not be modified through the normal update endpoint.
Activation and deactivation use dedicated operations.
---
### BR-DRV-009 - Authorization
Initial RBAC rules:
| Operation | ADMIN | MANAGER | TRANSPORT_COORDINATOR |
|-----------|-------|---------|-----------------------|
| View drivers | Yes | Yes | Yes |
| Create driver | Yes | No | Yes |
| Update driver | Yes | No | Yes |
| Activate driver | Yes | No | Yes |
| Deactivate driver | Yes | No | Yes |
These permissions may evolve with future business requirements.
---
### BR-DRV-010 - Audit Information
Each driver stores:
- createdAt
- updatedAt
These fields are inherited from the shared `Auditable` superclass.
---
## Planned API
### Create driver
```http
POST /api/drivers
```
---
### Get all drivers
```http
GET /api/drivers
```
Supports pagination:
```http
GET /api/drivers?page=0&size=10&sort=lastName,asc
```
---
### Get driver by ID
```http
GET /api/drivers/{id}
```
---
### Search by license number
```http
GET /api/drivers/search/license?licenseNumber=DRV
```
Supports pagination.
---
### Search by last name
```http
GET /api/drivers/search/last-name?lastName=Martin
```
Supports pagination.
---
### Filter by status
```http
GET /api/drivers/status/AVAILABLE
```
Supports pagination.
---
### Update driver
```http
PUT /api/drivers/{id}
```
---
### Activate driver
```http
PATCH /api/drivers/{id}/activate
```
---
### Deactivate driver
```http
PATCH /api/drivers/{id}/deactivate
```
---
## Validation
Examples of validation rules:
```text
firstName      → required
lastName       → required
licenseNumber  → required
phoneNumber    → required
status         → controlled enum value
```
The database should provide additional protection.
---
## Error Handling
Driver errors should use the centralized `ErrorMessages` class.
Planned messages:
```java
DRIVER_NOT_FOUND =
        "Driver not found";
DUPLICATE_DRIVER_LICENSE =
        "A driver with this license number already exists";
```
Typical HTTP errors:

```text
400 - Invalid input data
403 - Access denied
404 - Driver not found
409 - License number already exists
```
---
##Persistence
Driver data will be stored in:
```text
driver
```
The schema is managed by Flyway.
Planned migration:
```text
V4__create_driver_table.sql
```
Hibernate remains configured with:
```text
ddl-auto: validate
```
Therefore Flyway manages schema evolution and Hibernate validates entity mappings.
---
## Future Relationships
The Driver module is expected to interact with:
- Vehicle
- Transport
- Shipment
The first version does not directly couple Driver to Vehicle.
Driver and Vehicle assignments will be managed later through the Transport module.
---
## Design Notes
The Driver module follows the same architecture as Warehouse and Vehicle:
```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```
It also reuses:
- `Auditable`
- `ErrorMessages`
- MapStruct
- pagination
- centralized RBAC in `AuthorizationRules`