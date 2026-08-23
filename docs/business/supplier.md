# Supplier Management

## Overview

The Supplier Management module is part of the **Business Partners** domain of the Smart Logistics & Transport ERP.

Its purpose is to manage companies or organizations that provide goods, materials or services to the company.

Suppliers can later be referenced by modules such as:

- Product Management
- Inventory Management
- Purchase Order Management
- Goods Reception
- Procurement
- Reporting

The Supplier entity represents a business partner from whom the company purchases goods or services.

---

## Business Objectives

The Supplier Management module must allow authorized users to:

- create suppliers;
- view supplier information;
- search suppliers;
- update supplier information;
- activate or deactivate suppliers;
- maintain unique supplier identifiers;
- provide reusable supplier references for procurement and inventory modules.

---

## Supplier Data

A supplier contains the following information:

| Field | Description |
|---|---|
| `id` | Unique technical identifier |
| `code` | Unique business code identifying the supplier |
| `companyName` | Legal or commercial company name |
| `contactName` | Main supplier contact person |
| `email` | Main contact email |
| `phoneNumber` | Main contact phone number |
| `address` | Street address |
| `city` | City |
| `postalCode` | Postal code |
| `country` | Country |
| `vatNumber` | VAT / tax identification number |
| `active` | Indicates whether the supplier can be used operationally |
| `createdAt` | Creation timestamp |
| `updatedAt` | Last modification timestamp |

---

# Business Rules

## BR-SUPPLIER-001 — Supplier code is mandatory

Every supplier must have a business code.

Example:

```text
SUP-BE-001
SUP-BE-002
SUP-DE-001
```

The supplier code must not be blank.

---

## BR-SUPPLIER-002 — Supplier code must be unique

Two suppliers cannot have the same supplier code.

The uniqueness rule must be case-insensitive.

For example:

```text
SUP-BE-001
sup-be-001
Sup-Be-001
```

must be considered the same business code.

---

## BR-SUPPLIER-003 — Supplier code is normalized

Before persistence, the supplier code must be normalized:

```text
trim leading and trailing spaces
convert to uppercase
```

Example:

```text
" sup-be-001 "
```

becomes:

```text
SUP-BE-001
```

---

## BR-SUPPLIER-004 — Company name is mandatory

Every supplier must have a company name.

Examples:

```text
Food Supply Belgium
Cold Chain Equipment NV
European Packaging Solutions
```

The company name must not be blank.

---

## BR-SUPPLIER-005 — Supplier address is mandatory

A supplier must contain sufficient address information.

Mandatory address fields:

- address;
- city;
- country.

Postal code may be optional in V1.

---

## BR-SUPPLIER-006 — Email must be valid when provided

The email address is optional.

When provided, it must have a valid email format.

Example:

```text
orders@supplier-example.be
```

---

## BR-SUPPLIER-007 — VAT number is optional

A supplier may be created without a VAT number.

This supports cases where complete fiscal information is not yet available.

---

## BR-SUPPLIER-008 — VAT number must be unique when provided

When a VAT number is provided, another supplier must not already use the same VAT number.

The comparison must be case-insensitive after normalization.

---

## BR-SUPPLIER-009 — VAT number is normalized

When provided, the VAT number must be:

```text
trimmed
converted to uppercase
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

## BR-SUPPLIER-010 — Supplier is active by default

When a supplier is created:

```text
active = true
```

The backend owns this rule.

The client must not choose the initial active status.

---

## BR-SUPPLIER-011 — Suppliers are not physically deleted

For V1, suppliers must not be removed using hard delete operations.

Instead, they are deactivated.

This preserves references required by future modules such as:

- purchase orders;
- inventory receipts;
- procurement history;
- product sourcing.

---

## BR-SUPPLIER-012 — Inactive suppliers remain readable

An inactive supplier remains available for:

- historical consultation;
- reporting;
- existing purchase orders;
- audit purposes.

Deactivation means the supplier must not be selected for new procurement transactions.

---

## BR-SUPPLIER-013 — Supplier information can be updated

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
- active status.

Uniqueness rules must still be enforced.

---

## BR-SUPPLIER-014 — Updating a supplier must preserve identifier uniqueness

During update:

- a supplier may keep its current code;
- a supplier may keep its current VAT number;
- another supplier's code cannot be reused;
- another supplier's VAT number cannot be reused.

---

## BR-SUPPLIER-015 — Search by company name

Users must be able to search suppliers by company name.

The search must:

- be case-insensitive;
- support partial matching;
- support pagination.

Example:

```text
Search: "food"
```

may return:

```text
European Food Supply
Belgium Food Services
Global Frozen Foods
```

---

## BR-SUPPLIER-016 — Search by supplier code

Users must be able to search suppliers by code.

The search must:

- be case-insensitive;
- support partial matching;
- support pagination.

Example:

```text
Search: "BE"
```

may return:

```text
SUP-BE-001
SUP-BE-002
SUP-BE-010
```

---

## BR-SUPPLIER-017 — Supplier lists must support pagination

Supplier list endpoints must support Spring Data pagination.

Example:

```http
GET /api/suppliers?page=0&size=10
```

This prevents loading the whole supplier table at once.

---

# Supplier Lifecycle

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

---

# Authorization Rules

Supplier Management belongs to the Business Partners domain.

For V1, the proposed access model is:

| Operation | ADMIN | MANAGER | WAREHOUSE_OFFICER | TRANSPORT_COORDINATOR |
|---|---:|---:|---:|---:|
| View suppliers | ✅ | ✅ | ✅ | ✅ |
| Search suppliers | ✅ | ✅ | ✅ | ✅ |
| View supplier details | ✅ | ✅ | ✅ | ✅ |
| Create supplier | ✅ | ✅ | ❌ | ❌ |
| Update supplier | ✅ | ✅ | ❌ | ❌ |
| Activate supplier | ✅ | ✅ | ❌ | ❌ |
| Deactivate supplier | ✅ | ✅ | ❌ | ❌ |

These permissions may evolve when procurement-specific roles are introduced.

---

# Planned REST API

## Create Supplier

```http
POST /api/suppliers
```

Example request:

```json
{
  "code": "SUP-BE-001",
  "companyName": "European Food Supply",
  "contactName": "Laura Martin",
  "email": "orders@europeanfood-example.be",
  "phoneNumber": "+32 2 555 30 01",
  "address": "Industrial Park 12",
  "city": "Brussels",
  "postalCode": "1000",
  "country": "Belgium",
  "vatNumber": "BE0200000001"
}
```

---

## Get Suppliers

```http
GET /api/suppliers
```

Pagination:

```http
GET /api/suppliers?page=0&size=10
```

---

## Get Supplier by ID

```http
GET /api/suppliers/{id}
```

---

## Search by Company Name

```http
GET /api/suppliers/search/company-name?companyName=food
```

---

## Search by Supplier Code

```http
GET /api/suppliers/search/code?code=BE
```

---

## Update Supplier

```http
PUT /api/suppliers/{id}
```

---

## Activate Supplier

```http
PATCH /api/suppliers/{id}/activate
```

---

## Deactivate Supplier

```http
PATCH /api/suppliers/{id}/deactivate
```

---

# Future Relationships

Supplier is initially implemented as an independent Business Partner entity.

Future modules may reference Supplier through relationships such as:

```text
Supplier
   |
   +---- Product
   |
   +---- Purchase Order
   |
   +---- Goods Reception
   |
   +---- Inventory
   |
   +---- Procurement
```

These relationships will be introduced only when the corresponding modules are implemented.

---

# Future Improvements

Possible future extensions include:

- supplier categories;
- preferred supplier status;
- supplier rating;
- payment terms;
- delivery lead time;
- supplier contracts;
- supplier-specific product catalog;
- purchase history;
- supplier performance KPIs;
- quality rating;
- procurement agreements;
- multi-contact support;
- multi-address support.

These features are outside the V1 scope.

---

# Summary

The Supplier module is the second component of the **Business Partners** domain.

Its responsibilities are:

- maintaining supplier master data;
- protecting unique business identifiers;
- supporting activation and deactivation;
- providing search and pagination;
- preparing supplier references for future Product, Inventory and Procurement modules.

