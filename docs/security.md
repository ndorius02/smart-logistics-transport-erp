# Security Documentation

## Purpose

This document describes how authentication is implemented in the Smart Logistics Backend.

---

# Authentication

The application uses Spring Security with JWT (JSON Web Token).

Authentication is stateless.

No HTTP Session is stored on the server.

---

# Authentication Flow

```
Client
   │
   │ POST /api/auth/login
   ▼
AuthenticationController
   │
   ▼
AuthenticationManager
   │
   ▼
CustomUserDetailsService
   │
   ▼
PasswordEncoder (BCrypt)
   │
Password verified
   │
   ▼
JwtService
   │
JWT generated
   │
   ▼
Client receives JWT
```

Every protected request must include

Authorization: Bearer <jwt>

---

# Password Security

Passwords are never stored in plain text.

Spring Security BCryptPasswordEncoder is used.

Example

```
Password147!
```

stored as

```
$2a$10$...
```

---

# JWT

The generated JWT contains

- subject (email)
- authorities (roles)
- issuedAt
- expiration

Example payload

```json
{
  "sub": "alice.martin@example.com",
  "authorities": [
    "ROLE_MANAGER"
  ],
  "iat": 1752574100,
  "exp": 1752577700
}
```

---

# JwtAuthenticationFilter

The JwtAuthenticationFilter executes once for every request.

Responsibilities

- Read Authorization header
- Extract JWT
- Validate signature
- Check expiration
- Load user
- Create Authentication
- Store Authentication in SecurityContext

---

# Session Management

Spring Security is configured as

```
SessionCreationPolicy.STATELESS
```

No user session is stored on the server.

---

# Password Encoder

Implementation

```
BCryptPasswordEncoder
```

Advantages

- Salt generated automatically
- Strong hashing
- Resistant to rainbow table attacks

---

# Current Public Endpoints

| Endpoint | Access |
|-----------|--------|
| POST /api/auth/login | Public |

---

# Current Protected Endpoints

All remaining endpoints require a valid JWT.

Examples

GET /api/users

POST /api/users

PUT /api/users/{id}

DELETE /api/users/{id}

GET /api/roles

POST /api/roles

...

---

# Technologies

- Spring Security
- JWT (jjwt)
- BCrypt
- AuthenticationManager
- DaoAuthenticationProvider
- UserDetailsService