# Database Design

## Introduction

The Smart Logistics & Transport ERP database is designed using a relational database model to support logistics, transport, cargo and supply chain operations.

The database follows normalization principles to reduce redundancy, improve data consistency and maintain high performance.

PostgreSQL is used as the database management system.

## Database Technology

- Database Management System: PostgreSQL
- ORM: Spring Data JPA / Hibernate
- Naming Convention: English
- Table Naming: Singular
- Primary Keys: UUID
- Relationships: Foreign Keys

## Database Design Principles

The database follows these principles:

- Normalized design (3NF)
- Referential integrity
- Data consistency
- Scalability
- Maintainability
- Auditability

## Core Business Entities

| Entity | Purpose |
|---------|----------|
| User | System users |
| Role | User permissions |
| Customer | Customers receiving deliveries |
| Supplier | Product suppliers |
| Carrier | External transport companies |
| Product | Goods managed by the ERP |
| Warehouse | Physical storage locations |
| Inventory | Product quantities |
| PurchaseOrder | Supplier orders |
| PurchaseOrderLine | Ordered products |
| Cargo | Physical cargo |
| Shipment | Transport operation |
| Delivery | Customer delivery |
| DeliveryStatus | Delivery state |
| Vehicle | Transport vehicles |
| Driver | Drivers |
| Route | Transport routes |

## Constraints

Unique email
Unique product reference
Unique cargo reference
Unique shipment number
Unique purchase order number
Inventory quantity >= 0
Warehouse capacity > 0

## Audit Fields
createdAt
updatedAt
createdBy
updatedBy

## Indexing Strategy

customer.email
product.reference
shipment.reference
cargo.reference
purchaseOrder.number

## Future Database Evolution
Future versions may include:
Invoices
Payments
Transport Costs
Warehouse Zones
Container Management
Air Cargo (ULD)
Incoterms
Customs Documents
GPS Tracking
Notifications

