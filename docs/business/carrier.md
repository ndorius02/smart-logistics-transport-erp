# Carrier Management

## Overview

The Carrier Management module is part of the **Business Partners** domain of the Smart Logistics & Transport ERP.

Its purpose is to manage external transport companies that can provide transport services for logistics operations.

A carrier is different from an internal vehicle or driver.

Internal transport resources are managed through:

- Vehicle Management
- Driver Management

Carrier Management represents third-party transport companies that may later be assigned to shipments or outsourced transport operations.

Future modules may reference carriers from:

- Shipment Management
- Delivery Management
- Transport Planning
- Carrier Assignment
- Freight Procurement
- Reporting

---

## Business Objectives

The Carrier Management module must allow authorized users to:

- create carriers;
- view carrier information;
- search carriers;
- update carrier information;
- activate or deactivate carriers;
- maintain unique carrier identifiers;
- maintain external transport partner information;
- prepare carrier references for future shipment and delivery modules.

---

## Carrier Data

A carrier contains the following information:

| Field | Description |
|---|---|
| `id` | Unique technical identifier |
| `code` | Unique business code identifying the carrier |
| `companyName` | Legal or commercial company name |
| `contactName` | Main carrier contact person |
| `email` | Main contact email |
| `phoneNumber` | Main contact phone number |
| `address` | Street address |
| `city` | City |
| `postalCode` | Postal code |
| `country` | Country |
| `vatNumber` | VAT / tax identification number |
| `licenseNumber` | Transport / carrier licence identifier |
| `active` | Indicates whether the carrier can be used operationally |
| `createdAt` | Creation timestamp |
| `updatedAt` | Last modification timestamp |

---

# Business Rules

## BR-CARRIER-001 — Carrier code is mandatory

Every carrier must have a business code.

Examples:

```text
CAR-BE-001
CAR-BE-002
CAR-FR-001
```

The carrier code must not be blank.

---

## BR-CARRIER-002 — Carrier code must be unique

Two carriers cannot have the same carrier code.

The comparison must be case-insensitive.

For example:

```text
CAR-BE-001
car-be-001
Car-Be-001
```

must be considered identical.

---

## BR-CARRIER-003 — Carrier code is normalized

Before persistence, the carrier code must be:

```text
trimmed
converted to uppercase
```

Example:

```text
" car-be-001 "
```

becomes:

```text
CAR-BE-001
```

---

## BR-CARRIER-004 — Company name is mandatory

Every carrier must have a company name.

Examples:

```text
Benelux Freight Services
European Cold Transport
Belgium Road Logistics
```

The company name must not be blank.

---

## BR-CARRIER-005 — Carrier address is mandatory

A carrier must have sufficient business address information.

Required fields:

- address;
- city;
- country.

Postal code may remain optional in V1.

---

## BR-CARRIER-006 — Email must be valid when provided

The email address is optional.

When provided, it must have a valid email format.

---

## BR-CARRIER-007 — VAT number is optional

The carrier VAT number may be omitted when unavailable.

---

## BR-CARRIER-008 — VAT number must be unique when provided

When a VAT number is provided, another carrier must not already use the same VAT number.

Comparison must be case-insensitive after normalization.

---

## BR-CARRIER-009 — VAT number is normalized

When provided:

```text
trim spaces
convert to uppercase
```

Example:

```text
" be0123456789 "
```

becomes:

```text
BE0123456789
```

---

## BR-CARRIER-010 — Carrier licence number is mandatory

A carrier represents a professional transport company.

For V1, every carrier must therefore have a transport licence identifier.

Example:

```text
LIC-BE-2026-001
```

The licence number must not be blank.

---

## BR-CARRIER-011 — Carrier licence number must be unique

Two carriers cannot have the same transport licence number.

Comparison must be case-insensitive.

---

## BR-CARRIER-012 — Carrier licence number is normalized

Before persistence:

```text
trim spaces
convert to uppercase
```

Example:

```text
" lic-be-2026-001 "
```

becomes:

```text
LIC-BE-2026-001
```

---

## BR-CARRIER-013 — Carrier is active by default

When a carrier is created:

```text
active = true
```

The initial active status is controlled by the backend.

---

## BR-CARRIER-014 — Carriers are not physically deleted

For V1, carriers must not be removed using hard delete operations.

They are deactivated instead.

This preserves historical relationships for future:

- shipments;
- deliveries;
- outsourced transports;
- reporting;
- procurement records.

---

## BR-CARRIER-015 — Inactive carriers remain readable

An inactive carrier must remain visible for historical consultation.

Deactivation means the carrier cannot be selected for new operations.

Existing historical records must remain unchanged.

---

## BR-CARRIER-016 — Carrier information can be updated

Authorized users may update:

- code;
- company name;
- contact name;
- email;
- phone number;
- address;
- city;
- postal code;
- country;
- VAT number;
- licence number;
- active status.

All uniqueness rules remain applicable.

---

## BR-CARRIER-017 — Updating a carrier must preserve identifier uniqueness

When updating:

- the carrier may retain its own code;
- the carrier may retain its own VAT number;
- the carrier may retain its own licence number;
- another carrier's identifiers cannot be reused.

---

## BR-CARRIER-018 — Search by company name

Users must be able to search carriers by company name.

The search must:

- be case-insensitive;
- support partial matching;
- support pagination.

Example:

```text
Search: "transport"
```

may return:

```text
European Cold Transport
Belgian Transport Services
International Transport Group
```

---

## BR-CARRIER-019 — Search by carrier code

Users must be able to search carriers by carrier code.

The search must:

- be case-insensitive;
- support partial matching;
- support pagination.

---

## BR-CARRIER-020 — Search by licence number

Users should be able to search carriers by transport licence number.

The search must:

- be case-insensitive;
- support partial matching;
- support pagination.

---

## BR-CARRIER-021 — Carrier lists must support pagination

Example:

```http
GET /api/carriers?page=0&size=10
```

Carrier list endpoints must not return the entire dataset by default.

---

# Carrier Lifecycle

```text
       CREATE
         |
         v
      ACTIVE
         |
         | deactivate
         v
     INACTIVE
         |
         | activate
         v
      ACTIVE
```

There is no hard delete operation in V1.

---


# Authorization Rules

For V1:

| Operation | ADMIN | MANAGER | WAREHOUSE_OFFICER | TRANSPORT_COORDINATOR |
|---|---:|---:|---:|---:|
| View carriers | ✅ | ✅ | ✅ | ✅ |
| Search carriers | ✅ | ✅ | ✅ | ✅ |
| View carrier details | ✅ | ✅ | ✅ | ✅ |
| Create carrier | ✅ | ✅ | ❌ | ❌ |
| Update carrier | ✅ | ✅ | ❌ | ❌ |
| Activate carrier | ✅ | ✅ | ❌ | ❌ |
| Deactivate carrier | ✅ | ✅ | ❌ | ❌ |

These rules may evolve later if dedicated procurement or carrier-management roles are introduced.

---

# Future Relationships

Carrier will initially remain an independent Business Partner entity.

Future relationships may include:

```text
Carrier
   |
   +---- Shipment
   |
   +---- Delivery
   |
   +---- Outsourced Transport
   |
   +---- Freight Contract
   |
   +---- Carrier Performance
```

A future transport operation may therefore use either:

```text
Internal Transport
    ↓
Vehicle + Driver
```

or:

```text
External Transport
    ↓
Carrier
```

This distinction will be introduced when the Shipment / Delivery domain is implemented.

---

# Future Improvements

Possible future extensions include:

- carrier service areas;
- vehicle categories provided by carrier;
- temperature-controlled transport capability;
- ADR / dangerous-goods capability;
- international transport capability;
- carrier contracts;
- freight rates;
- insurance information;
- licence expiration date;
- carrier performance rating;
- delivery reliability KPI;
- claims tracking;
- preferred carrier status;
- carrier documents;
- multi-contact support.

These features are outside the V1 scope.

---