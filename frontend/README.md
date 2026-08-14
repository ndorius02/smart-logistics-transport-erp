# Smart Logistics & Transport ERP — Frontend

Angular frontend for the **Smart Logistics & Transport ERP** application.

This frontend provides the user interface for managing logistics and transport operations such as warehouses, vehicles, drivers and transports.

The application communicates with a Spring Boot REST API secured with JWT authentication and Role-Based Access Control (RBAC).

---

## Tech Stack

- Angular 22
- TypeScript
- Angular Material
- SCSS
- RxJS
- Reactive Forms
- Angular Router
- REST API
- JWT Authentication

---

## Backend

The frontend communicates with the Smart Logistics & Transport ERP Spring Boot backend.

Development API:

```text
http://localhost:8038
```

The backend provides:

- REST APIs
- JWT Authentication
- Role-Based Access Control (RBAC)
- Business validation
- PostgreSQL persistence
- Flyway database migrations

---

## Frontend Architecture

The application follows a feature-oriented Angular architecture.

Planned structure:

```text
src/app/
│
├── core/
│   ├── auth/
│   ├── guards/
│   ├── interceptors/
│   ├── models/
│   └── services/
│
├── shared/
│   ├── components/
│   └── material/
│
├── layout/
│   ├── header/
│   ├── sidebar/
│   └── main-layout/
│
└── features/
    ├── auth/
    ├── dashboard/
    ├── warehouses/
    ├── vehicles/
    ├── drivers/
    └── transports/
```

### Core

Contains application-wide services and infrastructure:

- Authentication
- JWT handling
- HTTP interceptors
- Route guards
- Shared models
- API services

### Shared

Contains reusable UI elements used across multiple features.

Examples:

- Reusable components
- Angular Material imports
- Common UI utilities

### Layout

Contains the main ERP application structure:

- Header
- Sidebar
- Main content area
- Navigation

### Features

Contains business-oriented application modules:

- Authentication
- Dashboard
- Warehouse Management
- Vehicle Management
- Driver Management
- Transport Management

---

## Authentication

Authentication is handled by the Spring Boot backend using JWT.

Login flow:

```text
User
  |
  v
Angular Login
  |
  | POST credentials
  v
Spring Boot API
  |
  | JWT
  v
Angular Auth Service
  |
  v
Authenticated Requests
```

Authenticated API requests will include:

```http
Authorization: Bearer <JWT>
```

The frontend will use:

- Authentication service
- HTTP interceptor
- Route guards
- Role-based navigation

---

## Roles

The frontend will respect the backend RBAC model.

Current roles include:

- ADMIN
- MANAGER
- WAREHOUSE_OFFICER
- TRANSPORT_COORDINATOR

UI elements and routes will be displayed or protected according to the authenticated user's permissions.

Backend authorization remains the final security authority.

---

## Main Features

### Authentication

- Login
- Logout
- JWT management
- Protected routes
- Role-based navigation

### Dashboard

Planned KPIs include:

- Warehouses
- Available vehicles
- Available drivers
- Planned transports
- Transports in progress
- Completed transports

### Warehouse Management

- List warehouses
- Search
- Pagination
- Create
- Update
- Activate / deactivate

### Vehicle Management

- List vehicles
- Search
- Pagination
- Create
- Update
- Activate / deactivate
- Vehicle status

### Driver Management

- List drivers
- Search
- Pagination
- Create
- Update
- Activate / deactivate
- Driver status

### Transport Management

- List transports
- Create transport
- Update transport
- Start transport
- Complete transport
- Cancel transport
- Transport status visualization

---

## UI

The application uses **Angular Material**.

Initial theme:

```text
Azure / Blue
```

The target interface is a professional ERP-style dashboard with:

- Sidebar navigation
- Top application bar
- Responsive content area
- Material tables
- Forms and dialogs
- Status indicators
- KPI cards
- Loading states
- User feedback notifications

---

## Development

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm start
```

or:

```bash
ng serve
```

The application is available at:

```text
http://localhost:4200
```

---

## Backend Development Server

During local full-stack development:

```text
Frontend
http://localhost:4200

        |
        | REST / JSON
        v

Backend
http://localhost:8038

        |
        v

PostgreSQL
```
---
### Frontend Progress

- ✅ Angular project setup
- ✅ Angular Material
- ✅ Authentication UI
- ✅ JWT authentication integration
- ✅ HTTP JWT interceptor
- ✅ Authentication guards
- ✅ Role-based route protection
- ✅ ERP layout and sidebar navigation
- ✅ Warehouse list
- ✅ Warehouse search
- ✅ Warehouse pagination
- ✅ Warehouse create/update
- ✅ Warehouse activation/deactivation
- ⬜ Vehicle Management
- ⬜ Driver Management
- ⬜ Transport Management
- ⬜ Dashboard KPIs

---

## Planned Development Order

```text
Vehicle Management
     ↓
Driver Management
     ↓
Transport Management
     ↓
Testing
     ↓
Docker / Deployment
```
---

## Related Project

This frontend is part of the **Smart Logistics & Transport ERP** full-stack project.

Repository structure:

```text
smart-logistics-transport-erp/
│
├── backend/
│   └── Spring Boot
│
├── frontend/
│   └── Angular
│
└── README.md
```

The root README contains the complete business and technical overview of the project.
