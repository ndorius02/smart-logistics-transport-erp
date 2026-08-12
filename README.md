# Smart Logistics & Transport ERP

Smart Logistics & Transport ERP is a full-stack enterprise application inspired by real-world logistics, transport, warehouse and supply chain operations.

The project is designed as a professional portfolio project to demonstrate backend architecture, security, business-rule implementation, database design and full-stack development using Spring Boot, Angular and PostgreSQL.
---
## Business Domain
The application models business processes commonly found in:
- Logistics
- Transport
- Supply Chain
- Warehouse Management
- Cargo Operations
- Import & Export
---
## Current Backend Features
The current backend includes:
- JWT Authentication
- Role-Based Access Control (RBAC)
- User Management
- Role Management
- Warehouse Management
- Vehicle Management
- Driver Management
- Transport Management
- Pagination
- Search and filtering
- Business rule validation
- Centralized exception handling
- Database migrations with Flyway
- DTO mapping with MapStruct
- Audit fields with reusable JPA base entity
---
## Implemented Roles
Current roles include:
- ADMIN
- MANAGER
- WAREHOUSE_OFFICER
- TRANSPORT_COORDINATOR
Authorization rules are centralized and applied at API level.
Examples:
- ADMIN can manage users and roles.
- MANAGER has read access to operational resources.
- WAREHOUSE_OFFICER has warehouse-specific access.
- TRANSPORT_COORDINATOR manages vehicles, drivers and transports.
---
## Implemented Modules
### Administration
- User
- Role
- Authentication
- Authorization
### Warehouse Management
- Warehouse creation and update
- Pagination
- Search
- Activation / deactivation
### Fleet Management
- Vehicle creation and update
- Vehicle types
- Operational vehicle status
- Activation / deactivation
- Pagination and search
### Driver Management
- Driver creation and update
- Driver operational status
- License-number uniqueness
- Activation / deactivation
- Pagination and search
### Transport Management
Transport operations link:
- Origin warehouse
- Destination warehouse
- Vehicle
- Driver
Supported lifecycle:
```text
PLANNED
   |
   +----> CANCELLED
   |
   v
IN_PROGRESS
   |
   v
COMPLETED

### Business rules include:
- origin and destination must be different;
- warehouses must be active;
- vehicle must be active and available;
- driver must be active and available;
- planned departure must be before planned arrival;
- status transitions are controlled by dedicated operations.

## Planned Modules
### The following modules are planned for future iterations:
- Customer Management
- Supplier Management
- Carrier Management
- Product Management
- Inventory Management
- Purchase Orders
- Cargo Management
- Shipment Management
- Delivery Management
- Reporting
- KPI Dashboard
## Tech Stack
### Backend
- Java 21
- Spring Boot 3.5.16
- Spring Security (JWT)
- Spring Data JPA
- Hibernate
- PostgreSQL
- Jakarta Validation
- MapStruct
- Lombok
- Flyway
- Swagger/OpenAPI
- Maven
### Frontend
- Angular
- TypeScript
- Angular Material
- RxJS
- Reactive Forms
- Angular Router
### Testing
- JUnit 5
- Mockito
- Spring Boot Test
- Testcontainers (PostgreSQL integration testing)
### Unit tests are being added progressively for service-layer business logic.
Planned later:
- Integration testing
- PostgreSQL Testcontainers
- Security / RBAC integration tests
### DevOps & Tools
- Docker & Docker Compose
- Git & GitHub
- GitHub Actions(CI/CD ready)
- Postman  (API testing)
- Flyway
## Project Status
. ✅ Functional Analysis & Business Rules
. ✅ Authentication & JWT Security
. ✅ Role-Based Authorization
. ✅ User / Role Management
. ✅ Warehouse Backend
. ✅ Vehicle Backend
. ✅ Driver Backend
. ✅ Transport Backend
. 🟡 Unit Testing
. ⬜ Angular Frontend
. ⬜ UI Screenshots
. ⬜ Final Integration Testing
. ⬜ Dockerization
. ⬜ Deployment
## Screenshots
### Screenshots will be added during frontend development.
Planned screenshots:
. Login
. Dashboard
. Warehouse Management
. Vehicle Management
. Driver Management
. Transport Planning
. Transport Status Workflow
. Reports / KPI Dashboard
## Future Improvements
. Customer Management
. Supplier Management
. Inventory Tracking
. Purchase Orders
. Cargo & Shipment Management
. Delivery Management
. Barcode / QR Code support
. Email Notifications
. Google Maps integration
. Audit Logs
. Multi-language support
. Mobile-friendly interface
. Advanced Reporting
. CI/CD
. Docker Compose
. Cloud Deployment
## About the Author
Bachelor in Computer Science (Application Development) with a background in Transport & Logistics. Passionate about designing enterprise applications and applying software engineering best practices to logistics, transport and supply chain management.
