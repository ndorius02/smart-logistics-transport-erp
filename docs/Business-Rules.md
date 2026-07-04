# Business Rules

## Project Name

Smart Logistics & Transport ERP

## Purpose

This document defines the main business rules of the Smart Logistics & Transport ERP application.

Business rules describe how the system must behave according to logistics, transport, cargo and supply chain operations.

---

## 1. User and Role Rules

### BR-001: Each user must have one role

Every user must be linked to one role.

Examples of roles:

- ADMIN
- MANAGER
- WAREHOUSE_OFFICER
- TRANSPORT_COORDINATOR
- BACK_OFFICE_USER

### BR-002: Only active users can access the system

A user with an inactive account must not be able to log in.

### BR-003: Admin users can manage other users

Only users with the ADMIN role can create, update, deactivate or assign roles to users.

---

## 2. Customer Rules

### BR-004: A customer must have a unique email

Two customers cannot have the same email address.

### BR-005: A customer can have multiple deliveries

One customer may be linked to many deliveries.

### BR-006: A customer can be linked to multiple cargo records

A customer may be associated with several cargo operations.

---

## 3. Supplier Rules

### BR-007: A supplier can have multiple purchase orders

One supplier may be linked to many purchase orders.

### BR-008: A supplier must have valid contact information

A supplier should have at least a name, email, phone number or contact person.

---

## 4. Carrier Rules

### BR-009: A carrier represents an external transport provider

A carrier is used when transport is handled by an external company.

Examples:

- DHL
- FedEx
- DPD
- External trucking company

### BR-010: A shipment may be assigned to a carrier

A shipment can be handled by an internal transport team or by an external carrier.

---

## 5. Product Rules

### BR-011: A product must have a unique reference

Each product must have a unique product reference or SKU.

### BR-012: A product can be stored in multiple warehouses

The same product may exist in several warehouses through the Inventory entity.

---

## 6. Warehouse Rules

### BR-013: A warehouse has a maximum capacity

A warehouse must have a defined storage capacity.

### BR-014: Warehouse occupancy cannot exceed capacity

The current warehouse occupancy must not exceed its maximum capacity.

### BR-015: A warehouse can contain multiple products

Products stored in a warehouse are managed through Inventory records.

---

## 7. Inventory Rules

### BR-016: Inventory links a product to a warehouse

Each inventory record represents the stock level of one product in one warehouse.

### BR-017: Inventory quantity cannot be negative

The available quantity of a product must never be below zero.

### BR-018: Inventory must be updated after stock movement

Inventory must be updated when products are received, shipped or adjusted.

---

## 8. Purchase Order Rules

### BR-019: A purchase order must be linked to a supplier

Every purchase order must have one supplier.

### BR-020: A purchase order must contain at least one line

A purchase order cannot be valid without purchase order lines.

### BR-021: A purchase order line must be linked to a product

Each purchase order line must refer to one product.

### BR-022: Ordered quantity must be greater than zero

The quantity in a purchase order line must always be greater than zero.

### BR-023: Purchase order status must be tracked

A purchase order may have statuses such as:

- DRAFT
- SUBMITTED
- CONFIRMED
- RECEIVED
- CANCELLED

---

## 9. Cargo Rules

### BR-024: Cargo represents goods prepared for transport

Cargo represents a physical lot of goods to be transported.

### BR-025: Cargo must have a unique reference number

Each cargo record must have a unique reference number.

### BR-026: Cargo must have weight and volume

Each cargo record must include weight and volume information.

### BR-027: Dangerous goods must be clearly identified

If cargo contains dangerous goods, it must be marked accordingly.

### BR-028: Temperature requirements must be specified when needed

If cargo requires controlled temperature, the required temperature must be defined.

---

## 10. Shipment Rules

### BR-029: A shipment groups cargo for transport

A shipment represents the transport operation of one or more cargo records.

### BR-030: A shipment must have an origin and destination

Each shipment must define where goods come from and where they are going.

### BR-031: A shipment can be import or export

Shipment type may be:

- IMPORT
- EXPORT
- DOMESTIC

### BR-032: Shipment status must be tracked

A shipment may have statuses such as:

- CREATED
- PLANNED
- IN_TRANSIT
- ARRIVED
- COMPLETED
- CANCELLED

---

## 11. Delivery Rules

### BR-033: A delivery must be linked to a customer

Each delivery must have a destination customer.

### BR-034: A delivery must have a delivery status

Every delivery must be linked to one delivery status.

### BR-035: Planned delivery date is required

Each delivery must have a planned delivery date.

### BR-036: A delivery is delayed if the planned date is exceeded

If the planned delivery date has passed and the delivery is not completed, it must be considered delayed.

### BR-037: A completed delivery must have an actual delivery date

When a delivery is marked as delivered, the actual delivery date must be recorded.

---

## 12. Delivery Status Rules

### BR-038: Delivery status must be predefined

Delivery statuses must be managed in a controlled list.

Examples:

- PENDING
- PLANNED
- IN_TRANSIT
- DELIVERED
- DELAYED
- CANCELLED

### BR-039: A delivery can have only one current status

At any given time, a delivery must have one active status.

---

## 13. Driver Rules

### BR-040: A driver must have a valid license number

Each driver must have a license number.

### BR-041: An inactive driver cannot be assigned to a delivery

Only active and available drivers can be assigned to deliveries.

### BR-042: A driver cannot be assigned to two deliveries at the same time

A driver must not have overlapping delivery assignments.

---

## 14. Vehicle Rules

### BR-043: A vehicle must have a unique registration number

Each vehicle must be identified by a unique registration number.

### BR-044: A vehicle must have a capacity

Each vehicle must have a defined weight and/or volume capacity.

### BR-045: A vehicle under maintenance cannot be assigned

Vehicles with maintenance or out-of-service status cannot be assigned to deliveries.

### BR-046: Cargo weight must not exceed vehicle capacity

The total cargo weight assigned to a vehicle must not exceed its maximum capacity.

---

## 15. Route Rules

### BR-047: A route must have departure and arrival locations

Each route must define a departure point and an arrival point.

### BR-048: A route must have an estimated distance

Each route should include an estimated distance in kilometers.

### BR-049: A route may have an estimated duration

Routes may include an estimated duration in minutes.

---

## 16. Reporting Rules

### BR-050: Dashboard must display operational KPIs

The dashboard should display key indicators such as:

- Number of deliveries in progress
- Number of delayed deliveries
- Number of available vehicles
- Warehouse occupancy rate
- Number of open purchase orders
- Number of shipments in transit

### BR-051: Data used in reports must come from system entities

Reports must be generated from application data such as deliveries, shipments, inventory and purchase orders.

---

## 17. General Data Rules

### BR-052: Created date must be recorded

Each main entity should have a creation date.

### BR-053: Updated date must be recorded

Each main entity should have an update date.

### BR-054: Important references must be unique

Business references such as product reference, cargo reference, shipment reference and purchase order number must be unique.

### BR-055: Required fields must be validated

The system must validate required fields before saving data.

### BR-056: Deleted business data should preferably be deactivated

For important business data, soft delete or deactivation is preferred instead of permanent deletion.