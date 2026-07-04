# Entity Relationship Diagram (ERD)

## Purpose

This document describes the first version of the Entity Relationship Diagram for the Smart Logistics & Transport ERP project.

The database contains 17 main entities covering administration, business partners, warehouse management, procurement, cargo, shipment, delivery and transport operations.

## ERD - Mermaid Diagram

```mermaid
erDiagram

    Role ||--o{ User : has

    Customer ||--o{ Cargo : owns
    Customer ||--o{ Delivery : receives

    Supplier ||--o{ PurchaseOrder : receives
    PurchaseOrder ||--o{ PurchaseOrderLine : contains
    Product ||--o{ PurchaseOrderLine : ordered_as

    Product ||--o{ Inventory : stored_as
    Warehouse ||--o{ Inventory : contains

    Product ||--o{ Cargo : included_in
    Warehouse ||--o{ Cargo : stored_in

    Cargo }o--|| Shipment : assigned_to
    Carrier ||--o{ Shipment : handles
    Driver ||--o{ Shipment : drives
    Vehicle ||--o{ Shipment : used_for
    Route ||--o{ Shipment : follows

    Shipment ||--o{ Delivery : includes
    DeliveryStatus ||--o{ Delivery : defines

    Role {
        UUID id PK
        string name
        string description
        datetime createdAt
        datetime updatedAt
    }

    User {
        UUID id PK
        string firstName
        string lastName
        string email UK
        string password
        string phoneNumber
        boolean active
        datetime createdAt
        datetime updatedAt
        UUID roleId FK
    }

    Customer {
        UUID id PK
        string companyName
        string contactFirstName
        string contactLastName
        string email UK
        string phoneNumber
        string address
        string city
        string postalCode
        string country
        boolean active
        datetime createdAt
        datetime updatedAt
    }

    Supplier {
        UUID id PK
        string companyName
        string contactPerson
        string email UK
        string phoneNumber
        string address
        string city
        string postalCode
        string country
        boolean active
        datetime createdAt
        datetime updatedAt
    }

    Carrier {
        UUID id PK
        string name
        string contactPerson
        string email UK
        string phoneNumber
        string country
        boolean active
        datetime createdAt
        datetime updatedAt
    }

    Product {
        UUID id PK
        string reference UK
        string name
        string description
        decimal unitPrice
        decimal weightKg
        decimal volumeM3
        boolean active
        datetime createdAt
        datetime updatedAt
    }

    Warehouse {
        UUID id PK
        string name
        string address
        string city
        string postalCode
        string country
        decimal capacityM3
        decimal currentOccupancyM3
        boolean active
        datetime createdAt
        datetime updatedAt
    }

    Inventory {
        UUID id PK
        int quantity
        int minimumStockLevel
        datetime lastUpdatedAt
        UUID productId FK
        UUID warehouseId FK
    }

    PurchaseOrder {
        UUID id PK
        string orderNumber UK
        date orderDate
        date expectedDeliveryDate
        string status
        decimal totalAmount
        datetime createdAt
        datetime updatedAt
        UUID supplierId FK
    }

    PurchaseOrderLine {
        UUID id PK
        int quantity
        decimal unitPrice
        decimal lineTotal
        UUID purchaseOrderId FK
        UUID productId FK
    }

    Cargo {
        UUID id PK
        string referenceNumber UK
        string description
        string cargoType
        decimal weightKg
        decimal volumeM3
        int quantity
        string originCountry
        string destinationCountry
        string temperatureRequirement
        boolean dangerousGoods
        string priorityLevel
        datetime createdAt
        datetime updatedAt
        UUID productId FK
        UUID customerId FK
        UUID warehouseId FK
        UUID shipmentId FK
    }

    Shipment {
        UUID id PK
        string shipmentNumber UK
        string shipmentType
        string origin
        string destination
        date plannedDepartureDate
        date plannedArrivalDate
        date actualDepartureDate
        date actualArrivalDate
        string status
        datetime createdAt
        datetime updatedAt
        UUID carrierId FK
        UUID driverId FK
        UUID vehicleId FK
        UUID routeId FK
    }

    Delivery {
        UUID id PK
        string deliveryNumber UK
        string pickupAddress
        string deliveryAddress
        date plannedDeliveryDate
        date actualDeliveryDate
        string notes
        datetime createdAt
        datetime updatedAt
        UUID customerId FK
        UUID shipmentId FK
        UUID deliveryStatusId FK
    }

    DeliveryStatus {
        UUID id PK
        string name UK
        string description
        datetime createdAt
        datetime updatedAt
    }

    Driver {
        UUID id PK
        string firstName
        string lastName
        string phoneNumber
        string email UK
        string licenseNumber UK
        string licenseCategory
        string availabilityStatus
        boolean active
        datetime createdAt
        datetime updatedAt
    }

    Vehicle {
        UUID id PK
        string registrationNumber UK
        string brand
        string model
        string vehicleType
        decimal capacityKg
        decimal volumeM3
        string fuelType
        string status
        datetime createdAt
        datetime updatedAt
    }

    Route {
        UUID id PK
        string departureLocation
        string arrivalLocation
        decimal distanceKm
        int estimatedDurationMinutes
        datetime createdAt
        datetime updatedAt
    }

## Main Relationships
Relationship	Meaning
Role 1 - N User	One role can be assigned to many users
Supplier 1 - N PurchaseOrder	One supplier can receive many purchase orders
PurchaseOrder 1 - N PurchaseOrderLine	One purchase order contains several lines
Product 1 - N Inventory	One product can be stored in several warehouses
Warehouse 1 - N Inventory	One warehouse contains several inventory records
Customer 1 - N Cargo	One customer can own multiple cargo records
Product 1 - N Cargo	One product can be part of multiple cargo records
Shipment 1 - N Cargo	One shipment can contain multiple cargo records
Carrier 1 - N Shipment	One carrier can handle multiple shipments
Driver 1 - N Shipment	One driver can be assigned to several shipments
Vehicle 1 - N Shipment	One vehicle can be used for several shipments
Route 1 - N Shipment	One route can be used by several shipments
Shipment 1 - N Delivery	One shipment can generate one or more deliveries
Customer 1 - N Delivery	One customer can receive many deliveries
DeliveryStatus 1 - N Delivery	One status can be used by many deliveries       