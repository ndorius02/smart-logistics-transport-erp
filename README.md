
# Smart Logistics & Transport ERP

A full-stack enterprise logistics management platform combining modern software engineering with real-world transport, warehouse, inventory, procurement, and supply chain business processes.

**Smart Logistics & Transport ERP** is designed to manage and connect core logistics operations, including warehouse management, vehicles, drivers, transport planning, business partners, products, inventory, stock movements, purchase orders,  goods receptions, cargo & shipment, delivery and reporting & analytics.

The project is built as a professional portfolio project and reflects my dual background in **Computer Science / Application Development** and **Transport & Logistics**.

Rather than implementing isolated CRUD modules, the application models interconnected business workflows with authentication, role-based access control (RBAC), resource availability, operational statuses, inventory tracking, procurement workflows, and transport lifecycle rules.

For example, the procurement workflow connects **Suppliers → Purchase Orders → Goods Receptions → Stock Movements → Inventory**, ensuring that received goods automatically generate stock movements and update the corresponding warehouse inventory.

The application follows a layered full-stack architecture with a **Spring Boot REST API**, **Angular frontend**, **PostgreSQL database**, **JWT-based security**, and business rules designed around realistic logistics operations.

## 🎯 Project Goals

- Model realistic logistics, transport, warehouse, business partners, inventory, and procurement workflows,  cargo & shipment, delivery and reporting & analytics
- Demonstrate full-stack enterprise application development
- Implement business rules beyond basic CRUD operations
- Apply secure authentication and role-based authorization
- Maintain consistency between interconnected business modules
- Build a modular and maintainable architecture
- Demonstrate both **software engineering skills** and understanding of **logistics operations**

---

## 🚧 Project Status

**Smart Logistics & Transport ERP is actively under development.**

The core architecture and several business modules are already implemented, while additional logistics and supply chain capabilities are planned as the project evolves.

The objective is to progressively build a realistic end-to-end logistics ERP rather than implementing disconnected features.

---

## 🗺️ Planned Business Modules

The following modules are part of the planned development roadmap.

### 📦 Cargo & Shipment

- Cargo management
- Shipment management
- Cargo-to-transport assignment
- Shipment lifecycle and status tracking
- Integration with transport planning
- Origin and destination management

### 🚚 Delivery

- Delivery management
- Delivery status tracking
- Transport-to-delivery workflow
- Proof-of-delivery workflow
- Delivery completion and operational traceability

### 📊 Reporting & Analytics

- Operational reports
- Historical transport analysis
- Fleet utilization
- Warehouse activity
- Inventory and stock movement analysis
- Procurement activity
- Driver activity
- Advanced KPI dashboards


## Implemented Modules
### Administration & Security

The application uses JWT-based authentication and Spring Security.

### Roles

The application currently supports the following roles:

| Role | Main Responsibility |
|---|---|
| `ADMIN` | Full administrative and operational access |
| `MANAGER` | Read access to operational resources |
| `WAREHOUSE_OFFICER` | Warehouse-related operations |
| `TRANSPORT_COORDINATOR` | Vehicle, driver and transport operations |

Authorization is enforced at API level by Spring Security and reflected
in the Angular interface.
### Authentication

![Smart Logistics ERP - Login](frontend/docs/screenshots/login.png)

### Operational Dashboard

The dashboard provides a consolidated overview of logistics operations.
Current KPIs include:
- Total warehouses
- Available vehicles
- Available drivers
- Planned transports
- Transports in progress
- Completed transports

The dashboard also displays recent transport operations including their
origin, destination, assigned vehicle, driver, departure time and status.

![Smart Logistics ERP Dashboard](frontend/docs/screenshots/dashboard.png)

## 🚚 Transport & Resource Management

The transport domain connects **warehouses, vehicles, drivers, and transport operations** to model realistic logistics planning and resource allocation.

### 🏭 Warehouse Management

Warehouses represent the logistics locations used across transport, inventory, and procurement operations.

Key capabilities:

- Create and maintain warehouse locations
- Manage warehouse code, address, city, country, and capacity
- Activate or deactivate warehouses
- Use warehouses as transport origins and destinations
- Maintain Product × Warehouse inventory positions
- Receive purchased goods into the appropriate warehouse
- Integrate warehouses with Transport, Inventory, Stock Movement, and Procurement workflows

### 🚛 Vehicle Management

The vehicle module manages fleet resources used for transport operations.

Key capabilities:

- Register and maintain fleet vehicles
- Manage vehicle identification and operational information
- Track vehicle availability
- Activate or deactivate vehicles
- Assign available vehicles to transport operations
- Prevent conflicting resource allocation through business rules

### 👨‍✈️ Driver Management

The driver module manages drivers participating in transport operations.

Key capabilities:

- Create and maintain driver records
- Manage driver identification and operational information
- Track driver availability
- Activate or deactivate drivers
- Assign available drivers to transport operations
- Prevent conflicting driver allocation through business rules

### 🗺️ Transport Management

Transport operations coordinate warehouses, vehicles, and drivers through a controlled operational lifecycle.

A transport defines:

- Origin warehouse
- Destination warehouse
- Assigned vehicle
- Assigned driver
- Planned departure and arrival
- Operational status

Transport lifecycle:

```text
PLANNED
   │
   │ Start transport
   ▼
IN_PROGRESS
   │
   │ Complete transport
   ▼
COMPLETED

PLANNED ──────→ CANCELLED
```

The transport workflow enforces business rules related to **resource availability, assignment, operational status, and lifecycle transitions**.

### 🔗 Cross-Module Integration

```text
Origin Warehouse
       │
       ▼
   Transport
   ├── Vehicle
   ├── Driver
   └── Destination Warehouse
       │
       ▼
Transport Lifecycle
PLANNED → IN_PROGRESS → COMPLETED
```

This integration demonstrates how logistics resources are coordinated through business rules rather than managed as isolated CRUD entities.

![Transport Management](frontend/docs/screenshots/transport-list.png)

### Business Rules

Transport operations enforce rules including:
- Origin and destination warehouses must be different
- Warehouses must be active
- Vehicle must be active and available
- Driver must be active and available
- Planned departure must occur before planned arrival
- Transport status transitions are controlled
- Transport execution updates operational resource availability


## 🤝 Business Partners Management

The Business Partners module centralizes the external organizations involved in logistics and supply chain operations.

The application distinguishes between **Customers, Suppliers, and Carriers**, each with dedicated business information and lifecycle management.

### 👥 Customers

Customers represent companies receiving logistics and transport services.

Key capabilities:

- Create and maintain customer records
- Manage company and contact information
- Store address, country, and VAT information
- Search customers by business information
- Activate or deactivate customer accounts
- Use customer data across logistics business processes

### 🏭 Suppliers

Suppliers represent companies providing products or materials to the organization.

Key capabilities:

- Create and maintain supplier records
- Manage company, contact, address, and VAT information
- Search and filter suppliers
- Activate or deactivate suppliers
- Validate supplier availability before procurement operations
- Integrate suppliers with the **Purchase Order** workflow

Supplier integration with Procurement:

```text
Supplier
   ↓
Purchase Order
   ↓
Purchase Order Items
   ↓
Goods Reception
   ↓
Inventory
```
## 📦 Product & Inventory Management

A complete inventory management module designed around real-world logistics and warehouse operations, with a strong focus on **stock traceability, business rules, and data consistency**.

### Product Catalog Management

- Product category management
- Product master data with unique SKU identification
- Product classification by category
- Multiple units of measure
- Decimal quantities for weight, volume, length, and piece-based products
- Product activation / deactivation lifecycle
- Search and filtering by SKU, name, and category
- Role-based access to management operations

### Multi-Warehouse Inventory

- Inventory tracking by **Product × Warehouse**
- Physical stock quantity tracking
- Reserved quantity management
- Automatic available stock calculation
- Configurable minimum stock levels
- Automatic **Low Stock** detection
- Inventory filtering by product and warehouse
- Operational stock overview across warehouses

![Inventory Management](frontend/docs/screenshots/inventory-list.png)

### Stock Movement & Traceability

Inventory quantities are not directly edited after initialization.

Every physical stock change is recorded through an immutable stock movement:

- `STOCK_IN` — incoming inventory
- `STOCK_OUT` — outgoing inventory
- `ADJUSTMENT_IN` — positive inventory correction
- `ADJUSTMENT_OUT` — negative inventory correction

Each movement keeps operational information such as:

- Product
- Warehouse
- Movement type
- Decimal quantity
- Business reference
- Reason / notes
- Movement date
- User responsible for the operation

This approach provides a clear **audit trail** and prevents uncontrolled manual modification of inventory quantities.

![Stoc Movement Management](frontend/docs/screenshots/stock-movement-list.png)

### Business Rules

The module enforces key inventory rules at the backend level:

- Stock quantities cannot become negative
- Reserved quantities cannot exceed physical stock
- Outgoing movements are validated against available inventory
- Product and warehouse relationships are validated before stock operations
- Duplicate inventory positions for the same Product × Warehouse combination are prevented
- Inventory adjustments require explicit business justification
- Product activation/deactivation is separated from master-data updates
- Stock corrections are performed through movements rather than rewriting historical inventory data

Read and write permissions are enforced by the backend and reflected in the Angular user interface.

### Architecture

```text
Product Category
       │
       ▼
    Product
       │
       ▼
Inventory Position ◄──── Warehouse
       │
       ▼
 Stock Movement
       │
       ├── STOCK_IN
       ├── STOCK_OUT
       ├── ADJUSTMENT_IN
       └── ADJUSTMENT_OUT
```

## 📦 Procurement Management

The **Procurement module** manages the purchasing lifecycle from supplier purchase orders to warehouse goods reception and inventory updates.

It is fully integrated with the **Supplier, Product, Warehouse, Inventory, and Stock Movement** modules.

### 🔄 Cross-Module Business Workflows

```text
Supplier
   ↓
Purchase Order
   ↓
Purchase Order Items
   ↓
Submit
   ↓
Approve
   ↓
Goods Reception
   ↓
Stock Movement (STOCK_IN)
   ↓
Inventory Update

```
### 🧾 Purchase Orders
- Create purchase orders for active suppliers and warehouses
- Add multiple products with quantity and unit price
- Automatically calculate line totals and total order amount
- Edit purchase order items while the order is in DRAFT
- Submit and approve purchase orders
- Track ordered, received, and remaining quantities
- Support cancellation according to business rules
### 📥 Goods Reception
- Receive goods from approved purchase orders
- Support partial and multiple receptions
- Prevent quantities from exceeding the ordered quantity
- Automatically update received and remaining quantities
- Automatically transition orders to PARTIALLY_RECEIVED or RECEIVED
- Record reception reference, date, notes, and authenticated user
  
### 📊 Inventory Integration

- Goods reception is directly connected to inventory management.
- When goods are received, the system automatically:
  1. Validates the purchase order and remaining quantity
  2. Creates a STOCK_IN movement
  3. Updates the corresponding Product × Warehouse inventory position
  4. Updates the received quantity of the purchase order item
  5. Recalculates the purchase order status

The operation is handled transactionally to maintain consistency between procurement, stock movements, and inventory.

## Future Technical Improvements
Planned technical improvements include:
- Docker / Docker Compose
- CI/CD pipeline
- Cloud deployment
- Expanded integration testing
- Security integration tests
- Audit logging
- Email notifications
- Barcode / QR code support
- Advanced reporting
- Mobile-friendly interface
- Multi-language support
- Mapping / geolocation integration

## Architecture

The application follows a layered full-stack architecture.

```text
┌───────────────────────────────────────┐
│            Angular Frontend           │
│                                       │
│ Components • Services • Guards        │
│ Interceptors • Reactive Forms         │
└──────────────────┬────────────────────┘
                   │
                   │ REST / JSON
                   │ JWT
                   ▼
┌───────────────────────────────────────┐
│          Spring Boot REST API         │
│                                       │
│ Controllers • DTOs • Validation       │
│ Security • Exception Handling         │
└──────────────────┬────────────────────┘
                   │
                   ▼
┌───────────────────────────────────────┐
│            Service Layer              │
│                                       │
│ Business Rules • Transactions         │
│ Resource Validation                   │
└──────────────────┬────────────────────┘
                   │
                   ▼
┌───────────────────────────────────────┐
│          Persistence Layer            │
│                                       │
│ Spring Data JPA • Hibernate           │
└──────────────────┬────────────────────┘
                   │
                   ▼
┌───────────────────────────────────────┐
│              PostgreSQL               │
└───────────────────────────────────────┘
```

  
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
- Angular 22
- TypeScript
- Angular Material
- RxJS
- Reactive Forms
- Angular Router
- HTTP Interceptors
- Route Guards
### Testing
- JUnit 5
- Mockito
- Spring Boot Test
- Testcontainers
- PostgreSQL integration testing
Testing is being expanded progressively to cover service-layer business
rules, integration scenarios and security behavior.
### DevOps & Tools
- Docker & Docker Compose
- Git & GitHub
- GitHub Actions(CI/CD ready)
- Postman  (API testing)
- IntelliJ IDEA
- Visual Studio Code


## Getting Started
### Prerequisites
Install:
- Java 21
- Maven
- Node.js
- Angular CLI
- PostgreSQL

## Database Migrations

Database schema changes are managed with Flyway.

Migration scripts are versioned with the application to provide reproducible
database evolution across environments.

## 🧠 Skills Demonstrated by This Project

### 💻 Software Engineering

- Full-stack enterprise application development
- Object-oriented programming and domain modeling
- Layered backend architecture
- RESTful API design
- DTO-based API contracts and MapStruct mapping
- Relational database modeling with PostgreSQL
- Database versioning and migrations with Flyway
- Authentication with JWT
- Role-Based Access Control (RBAC) with Spring Security
- Business-rule implementation and workflow validation
- Transactional operations and data consistency
- Centralized exception handling and API error responses
- Server-side pagination, filtering, and sorting
- Angular standalone component architecture
- Reactive Forms and client-side validation
- Reactive UI state management with Angular Signals
- Frontend/backend API integration
- Git-based feature development workflow

### 🚚 Logistics & Supply Chain

- Warehouse operations
- Fleet and driver management
- Transport planning and lifecycle management
- Resource allocation and operational availability
- Product and inventory management
- Stock movement tracking
- Supplier and business partner management
- Purchase order lifecycle management
- Goods reception and partial reception workflows
- Procurement-to-inventory integration
- Logistics process modeling
- Supply chain ERP concepts
- Cargo & Shipment
- Delivery
- Reporting & Analytics

## 👨‍💻 About the Developer

Computer Science graduate specialized in **Application Development**, with an additional academic background in **Transport & Logistics**.

Interested in opportunities at the intersection of **software engineering, enterprise applications, logistics, transport, and supply chain operations**.

This project demonstrates my ability to translate real-world business requirements into secure, structured, and interconnected full-stack software solutions.
