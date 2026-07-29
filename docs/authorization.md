# Authorization Rules

## BR-001

Each user must have exactly one role.

Status

Implemented

Implementation

@ManyToOne Role

---

## BR-002

Only active users can log in.

Status

Pending

---

## BR-003

Only ADMIN users can manage users.

Status

Pending

Implementation

Spring Security
@PreAuthorize