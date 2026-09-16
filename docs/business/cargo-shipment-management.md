# Cargo & Shipment Management

## Overview

The **Cargo & Shipment Management** module manages goods that must be transported and organizes them into shipments assigned to transport operations.

This module connects existing ERP domains such as:

- Customers
- Warehouses
- Vehicles
- Drivers
- Transport operations

It also prepares the foundation for future **Delivery** and **Shipment Tracking** capabilities.

### Business Flow

```text
Customer
   ↓
Shipment
   ↓
Cargo
   ↓
Transport Assignment
   ↓
Transport
   ↓
Shipment Lifecycle
   ↓
Delivery
```

---

## Cargo Management

A **Cargo** represents physical goods that must be transported as part of a shipment.

### Main Information

A cargo contains:

- Unique cargo number
- Description
- Cargo type
- Quantity
- Unit
- Weight
- Volume
- Special instructions
- Active status
- Creation and update timestamps

Example cargo number:

```text
CG-2026-000001
```

### Cargo Types

Supported cargo types:

```text
GENERAL
FRAGILE
PERISHABLE
HAZARDOUS
REFRIGERATED
```

### Business Rules

- Cargo number is required and unique.
- Cargo number is normalized to uppercase.
- Cargo number cannot be changed after creation.
- Description is required.
- Quantity must be greater than zero.
- Weight cannot be negative.
- Volume cannot be negative.
- Special instructions are optional.
- Cargo used in business operations must not be physically deleted.
- A cargo belongs to one shipment in the initial version.

---

## Shipment Management

A **Shipment** represents a logistics operation grouping one or more cargo items that must be transported for a customer between an origin and a destination.

### Main Information

A shipment contains:

- Unique shipment number
- Customer
- Origin warehouse
- Destination warehouse
- Planned pickup date
- Planned delivery date
- Shipment status
- Notes
- Cargo items
- Assigned transport
- Creation and update timestamps

Example shipment number:

```text
SHP-2026-000001
```

### Business Rules

- Shipment number is required and unique.
- Shipment number is normalized to uppercase.
- Shipment number cannot be changed after creation.
- Customer is required and must be active.
- Origin warehouse is required and must be active.
- Destination warehouse is required and must be active.
- Origin and destination warehouses must be different.
- Planned delivery date cannot be earlier than the planned pickup date.
- A new shipment starts with `DRAFT` status.
- A shipment must contain at least one cargo before it can be planned.

---

## Shipment & Cargo Relationship

A shipment can contain multiple cargo items.

```text
Shipment
   │
   ├── Cargo A
   ├── Cargo B
   └── Cargo C
```

Relationship:

```text
Shipment 1 ───────── N Cargo
```

For the initial version:

- One shipment can contain multiple cargo items.
- One cargo belongs to only one shipment.

---

## Shipment Lifecycle

Supported shipment statuses:

```text
DRAFT
PLANNED
ASSIGNED
IN_TRANSIT
DELIVERED
CANCELLED
```

Lifecycle:

```text
DRAFT
  │
  │ Plan shipment
  ▼
PLANNED
  │
  │ Assign transport
  ▼
ASSIGNED
  │
  │ Start transport
  ▼
IN_TRANSIT
  │
  │ Complete transport
  ▼
DELIVERED
```

Cancellation is allowed before transport execution:

```text
DRAFT ─────────→ CANCELLED
PLANNED ───────→ CANCELLED
ASSIGNED ──────→ CANCELLED
```

`DELIVERED` and `CANCELLED` are terminal statuses.

---

## Transport Assignment

Shipments are integrated with the existing **Transport Management** module.

Relationship:

```text
1 Shipment  → 0..1 Transport
1 Transport → 0..N Shipments
```

This allows one transport operation to carry multiple shipments.

Example:

```text
Transport TR-2026-0010
│
├── SHP-2026-000010
├── SHP-2026-000011
└── SHP-2026-000012
```

### Assignment Rules

A shipment can be assigned to a transport only when:

- Shipment status is `PLANNED`.
- Transport is in a status allowing assignment.
- Shipment origin matches the transport origin.
- Shipment destination matches the transport destination.

After successful assignment:

```text
Shipment
PLANNED → ASSIGNED
```

---

## Transport & Shipment Synchronization

Shipment status is synchronized with the execution of its assigned transport.

### Starting a Transport

When a transport starts:

```text
Transport
PLANNED → IN_PROGRESS

Assigned Shipments
ASSIGNED → IN_TRANSIT
```

### Completing a Transport

When a transport is completed:

```text
Transport
IN_PROGRESS → COMPLETED

Assigned Shipments
IN_TRANSIT → DELIVERED
```

This ensures consistency between transport execution and shipment tracking.

---

## Transactional Consistency

Operations that modify both Transport and Shipment states must be executed transactionally.

Example:

```text
Start Transport
     │
     ├── Transport → IN_PROGRESS
     │
     └── Shipments → IN_TRANSIT
```

If one operation fails:

```text
ROLLBACK
```

The same principle applies when completing a transport:

```text
Complete Transport
     │
     ├── Transport → COMPLETED
     │
     └── Shipments → DELIVERED
```

This prevents inconsistent states between transport operations and their assigned shipments.

---

## Cross-Module Integration

The Cargo & Shipment module connects several existing ERP modules:

```text
Customer
   │
   ▼
Shipment
   │
   ├──── Cargo
   │
   ├──── Origin Warehouse
   │
   ├──── Destination Warehouse
   │
   ▼
Transport
   │
   ├──── Vehicle
   └──── Driver
```

The resulting business workflow is:

```text
Customer
   ↓
Shipment (DRAFT)
   ↓
Add Cargo
   ↓
PLANNED
   ↓
Assign Transport
   ↓
ASSIGNED
   ↓
Start Transport
   ↓
IN_TRANSIT
   ↓
Complete Transport
   ↓
DELIVERED
```

---

## Future Enhancements

The following capabilities can be introduced in later versions:

- Vehicle capacity validation based on total cargo weight and volume
- Advanced cargo compatibility rules
- Refrigerated transport requirements
- Hazardous goods handling rules
- Multi-stop shipments
- Shipment tracking events
- Delivery management
- Proof of delivery
- Shipment history and traceability
- Shipment and transport performance analytics