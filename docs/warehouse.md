# Warehouse Module

## Purpose

The Warehouse module manages physical storage locations used by the Smart Logistics platform.

Warehouses can later be associated with inventory, shipments and transport operations.

---

## Entity Overview

A warehouse contains the following information:

- ID
- Code
- Name
- Address
- City
- Country
- Capacity
- Active status
- Creation date
- Last update date

---

## Business Rules

### BR-WH-001 - Warehouse Identity

Each warehouse must have a unique identifier and a business code.

Required information:

- code
- name
- address
- city
- country

---

### BR-WH-002 - Unique Warehouse Code

A warehouse code must be unique.

Example:

BRU-WH-001

Two warehouses cannot share the same code.

---

### BR-WH-003 - Positive Capacity

Warehouse capacity must be greater than zero.

Valid:

10000

Invalid:

0

-500

---

### BR-WH-004 - Active Status

A newly created warehouse is active by default.

active = true

---

### BR-WH-005 - Warehouse Deactivation

Warehouses should not normally be physically deleted.

Instead, they are deactivated:

active = false

This preserves historical business data.

---

### BR-WH-006 - Warehouse Update

The following information can be updated:

- name
- address
- city
- country
- capacity
- active status

---

### BR-WH-007 - Authorization

| Operation | ADMIN | MANAGER | WAREHOUSE_OFFICER |
|-----------|-------|---------|-------------------|
| View warehouses | Yes | Yes | Yes |
| Create warehouse | Yes | Yes | No |
| Update warehouse | Yes | Yes | No |
| Deactivate warehouse | Yes | No | No |

Authorization is enforced using Spring Security RBAC.

---

### BR-WH-008 - Audit Information

Each warehouse stores:

- createdAt
- updatedAt

These values are managed automatically by the backend.

---

## Planned API

GET /api/warehouses

GET /api/warehouses/{id}

POST /api/warehouses

PUT /api/warehouses/{id}

DELETE /api/warehouses/{id}

The DELETE endpoint will represent a logical deactivation rather than a physical database deletion.

---

## Future Relationships

The Warehouse module may later be connected to:

- Inventory
- Shipments
- Transports
- Loading operations
- Unloading operations

These relationships are not implemented in the first version.