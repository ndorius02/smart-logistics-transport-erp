# Customer Management

## Overview

The Customer Management module is part of the **Business Partners** domain of the Smart Logistics & Transport ERP.

Its purpose is to manage companies or organizations that receive logistics, transport, warehousing or delivery services.

Customers can later be referenced by other ERP modules such as:

- Shipment Management
- Delivery Management
- Cargo Management
- Sales or service orders
- Reporting
- Billing-related processes

The Customer entity represents a business partner receiving logistics or transport services.

---

## Business Objectives

The Customer Management module must allow authorized users to:

- create customers;
- view customer information;
- search customers;
- update customer information;
- activate or deactivate customers;
- maintain unique business identifiers;
- provide reusable customer references for future logistics modules.

---

## Customer Data

A customer contains the following information:

| Field | Description |
|---|---|
| `id` | Unique technical identifier |
| `code` | Unique business code identifying the customer |
| `companyName` | Legal or commercial company name |
| `contactName` | Main customer contact person |
| `email` | Main contact email |
| `phoneNumber` | Main contact phone number |
| `address` | Street address |
| `city` | City |
| `postalCode` | Postal code |
| `country` | Country |
| `vatNumber` | VAT / tax identification number |
| `active` | Indicates whether the customer can be used operationally |
| `createdAt` | Creation timestamp |
| `updatedAt` | Last modification timestamp |

---

# Business Rules

## BR-CUSTOMER-001 — Customer code is mandatory

Every customer must have a business code.

The code is used as a human-readable identifier in the ERP.

Example:

```text
CUS-BE-001
CUS-BE-002
CUS-FR-001
```

The customer code must not be blank.

---

## BR-CUSTOMER-002 — Customer code must be unique

Two customers cannot have the same customer code.

The uniqueness rule must be case-insensitive.

For example:

```text
CUS-BE-001
cus-be-001
Cus-Be-001
```

must be considered the same business code.

---

## BR-CUSTOMER-003 — Customer code is normalized

Before saving a customer, the code must be normalized.

Normalization rules:

```text
trim leading and trailing spaces
convert to uppercase
```

Example:

```text
" cus-be-001 "
```

becomes:

```text
CUS-BE-001
```

---

## BR-CUSTOMER-004 — Company name is mandatory

Every customer must have a company name.

Examples:

```text
STEF Belgium
FedEx Express Belgium
GSK Belgium
ABC Logistics NV
```

The company name must not be blank.

---

## BR-CUSTOMER-005 — Customer address is mandatory

A customer must have sufficient address information for logistics operations.

Mandatory address fields:

- address;
- city;
- country.

Postal code may be optional in V1.

---

## BR-CUSTOMER-006 — Email must be valid when provided

The email address is optional.

However, when an email is provided, it must have a valid email format.

Example:

```text
operations@example.com
```

Invalid example:

```text
operations-example
```

---

## BR-CUSTOMER-007 — VAT number is optional

The VAT number is optional because some customers may not have one available when they are first created.

Example:

```text
BE0123456789
```

---

## BR-CUSTOMER-008 — VAT number must be unique when provided

When a VAT number is provided, another customer must not already use the same VAT number.

The comparison should be case-insensitive after normalization.

---

## BR-CUSTOMER-009 — VAT number is normalized

When provided, the VAT number must be normalized before persistence.

Recommended normalization:

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

## BR-CUSTOMER-010 — Customer is active by default

When a new customer is created:

```text
active = true
```

The client application must not decide the initial active status.

The backend owns this business rule.

---

## BR-CUSTOMER-011 — Customers are not physically deleted

For V1, customers should not be deleted from the database through the REST API.

Instead, they can be:

```text
ACTIVE
    ↓
INACTIVE
```

This preserves historical references for future modules such as shipments and deliveries.

---

## BR-CUSTOMER-012 — Inactive customers remain readable

An inactive customer must remain visible in historical and administrative queries.

Deactivation means the customer should no longer be selected for new operational transactions.

It does not mean that its historical information disappears.

---

## BR-CUSTOMER-013 — Customer information can be updated

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
- VAT number.

Uniqueness rules must still be validated during updates.

---

## BR-CUSTOMER-014 — Updating a customer must not create duplicate business identifiers

When updating a customer:

- the current customer may keep its existing code;
- the current customer may keep its existing VAT number;
- another customer's code cannot be reused;
- another customer's VAT number cannot be reused.

---

## BR-CUSTOMER-015 — Search by company name

Users must be able to search customers by company name.

The search should:

- be case-insensitive;
- support partial matching;
- support pagination.

Example:

```text
Search: "log"
```

may return:

```text
ABC Logistics
European Logistics Group
Global Logistics Services
```

---

## BR-CUSTOMER-016 — Search by customer code

Users should be able to search customers by customer code.

The search should:

- be case-insensitive;
- support partial matching;
- support pagination.

Example:

```text
Search: "BE"
```

may return:

```text
CUS-BE-001
CUS-BE-002
CUS-BE-010
```

---

## BR-CUSTOMER-017 — Customer lists must support pagination

Customer list endpoints must support Spring Data pagination.

Example:

```http
GET /api/customers?page=0&size=10
```

This avoids returning the entire customer database at once.

---

# Customer Lifecycle

The customer lifecycle is intentionally simple in V1.

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

Customer Management belongs to the Business Partners domain.

For V1, the proposed access model is:

| Operation | ADMIN | MANAGER | WAREHOUSE_OFFICER | TRANSPORT_COORDINATOR |
|---|---:|---:|---:|---:|
| View customers | ✅ | ✅ | ✅ | ✅ |
| Search customers | ✅ | ✅ | ✅ | ✅ |
| View customer details | ✅ | ✅ | ✅ | ✅ |
| Create customer | ✅ | ✅ | ❌ | ❌ |
| Update customer | ✅ | ✅ | ❌ | ❌ |
| Activate customer | ✅ | ✅ | ❌ | ❌ |
| Deactivate customer | ✅ | ✅ | ❌ | ❌ |

These rules can evolve later when dedicated business-partner or sales roles are introduced.

---

# Planned REST API

## Create Customer

```http
POST /api/customers
```

Example request:

```json
{
  "code": "CUS-BE-001",
  "companyName": "ABC Logistics Belgium",
  "contactName": "Sophie Martin",
  "email": "operations@abclogistics.be",
  "phoneNumber": "+32 2 555 01 01",
  "address": "100 Logistics Avenue",
  "city": "Brussels",
  "postalCode": "1000",
  "country": "Belgium",
  "vatNumber": "BE0123456789"
}
```

---

## Get Customers

```http
GET /api/customers
```

Pagination:

```http
GET /api/customers?page=0&size=10
```

---

## Get Customer by ID

```http
GET /api/customers/{id}
```

---

## Search by Company Name

```http
GET /api/customers/search/company-name?companyName=logistics
```

---

## Search by Customer Code

```http
GET /api/customers/search/code?code=BE
```

---

## Update Customer

```http
PUT /api/customers/{id}
```

---

## Activate Customer

```http
PATCH /api/customers/{id}/activate
```

---

## Deactivate Customer

```http
PATCH /api/customers/{id}/deactivate
```

---

# Future Relationships

Customer is initially implemented as an independent Business Partner entity.

Future modules may reference Customer through relationships such as:

```text
Customer
   |
   +---- Shipment
   |
   +---- Delivery
   |
   +---- Cargo
   |
   +---- Sales / Service Order
   |
   +---- Invoice
```

The exact relationships will be introduced only when the corresponding modules are implemented.

---

# Future Improvements

Possible future extensions include:

- multiple customer contacts;
- multiple customer addresses;
- billing address;
- delivery address;
- customer-specific transport agreements;
- credit limits;
- payment terms;
- preferred carrier;
- customer contracts;
- SLA management;
- customer portal;
- customer-specific pricing;
- customer shipment history;
- customer KPIs.

These features are intentionally outside the V1 Customer scope.

---

# Summary

The Customer module provides the first foundation of the **Business Partners**
domain.

Its main responsibilities are:

- maintaining customer master data;
- protecting unique business identifiers;
- supporting customer activation/deactivation;
- providing searchable and pageable customer records;
- preparing customer references for future Shipment, Delivery and Cargo modules.
