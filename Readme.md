# Courier Management System (CMS)

## 1. Overview

The **Courier Management System (CMS)** is a backend RESTful application built using **Spring Boot** that manages courier operations such as shipment creation, courier assignment, tracking, authentication, and administration. This document provides a **high-level yet complete functional and technical specification** of the system, intended to guide an LLM or a developer in generating the full project.

The system is designed as a **modular, scalable, role-based backend** suitable for real-world courier or logistics platforms.

---

## 2. Goals & Non-Goals

### Goals

- Provide a complete backend for courier operations
- Role-based access control (Admin, Customer, Courier)
- Shipment lifecycle and real-time tracking
- Clean REST API design
- Production-ready architecture

### Non-Goals

- Frontend UI
- Real-time GPS tracking (simulated via updates)
- External integrations (SMS/email gateways are abstracted)

---

## 3. Actors & Roles

### 3.1 Roles

| Role     | Description                                     |
| -------- | ----------------------------------------------- |
| ADMIN    | Manages users, couriers, shipments, reports     |
| CUSTOMER | Creates and tracks shipments                    |
| COURIER  | Accepts assignments and updates shipment status |

Each authenticated user has **exactly one role**.

---

## 4. Core Domain Concepts

### 4.1 User

Base authentication entity. Every actor in the system is a user.

Key fields:

- id
- email (unique)
- password (hashed)
- role (ADMIN, CUSTOMER, COURIER)
- active

---

### 4.2 Customer

Represents a customer sending shipments.

Key fields:

- id
- userId (FK → User)
- name
- phone
- address

---

### 4.3 Courier

Represents a delivery agent.

Key fields:

- id
- userId (FK → User)
- vehicleType
- status (AVAILABLE, BUSY, OFFLINE)

---

### 4.4 Shipment

The central business entity.

Key fields:

- id
- trackingNumber (unique, public-facing)
- senderCustomerId
- receiverName
- receiverPhone
- pickupAddress
- deliveryAddress
- weight
- status
- createdAt

#### Shipment Status Lifecycle

```
CREATED → PICKED_UP → IN_TRANSIT → OUT_FOR_DELIVERY → DELIVERED
                       ↘️ CANCELLED
```

---

### 4.5 Assignment

Links a courier to a shipment.

Key fields:

- id
- shipmentId
- courierId
- status (ASSIGNED, ACCEPTED, REJECTED)

---

### 4.6 TrackingEvent

Immutable events that record shipment progress.

Key fields:

- id
- shipmentId
- status
- location
- remarks
- timestamp

---

## 5. Functional Requirements

### 5.1 Authentication & Security

- JWT-based authentication
- Role-based authorization via Spring Security
- Password hashing (BCrypt)
- Token-based stateless sessions

---

### 5.2 Shipment Management

- Customers can create shipments
- Admins can view all shipments
- Shipments generate a unique tracking number
- Shipment status can only move forward (except cancellation)

---

### 5.3 Courier Assignment

- Admin assigns courier to shipment
- Courier must accept or reject assignment
- Only accepted couriers can update shipment status

---

### 5.4 Tracking

- Each shipment status update creates a tracking event
- Public tracking via tracking number
- Tracking history is immutable

---

### 5.5 Admin Capabilities

- Manage users
- View shipment statistics
- Monitor courier availability

---

## 6. REST API Specification (High-Level)

### 6.1 Auth APIs

```
POST   /api/v1/auth/register
POST   /api/v1/auth/login
GET    /api/v1/auth/me
```

---

### 6.2 Customer APIs

```
POST   /api/v1/customers
GET    /api/v1/customers/{id}
GET    /api/v1/customers/{id}/shipments
```

---

### 6.3 Courier APIs

```
POST   /api/v1/couriers
GET    /api/v1/couriers
PATCH  /api/v1/couriers/{id}/status
```

---

### 6.4 Shipment APIs

```
POST   /api/v1/shipments
GET    /api/v1/shipments/{id}
GET    /api/v1/shipments
PATCH  /api/v1/shipments/{id}/status
```

---

### 6.5 Assignment APIs

```
POST   /api/v1/assignments
PATCH  /api/v1/assignments/{id}/accept
PATCH  /api/v1/assignments/{id}/reject
```

---

### 6.6 Tracking APIs

```
GET    /api/v1/track/{trackingNumber}
GET    /api/v1/shipments/{id}/tracking-history
POST   /api/v1/track/{shipmentId}/update
```

---

## 7. Backend Architecture

### 7.1 Layered Architecture

```
Controller → Service → Repository → Database
```

- **Controller**: Request/response mapping
- **Service**: Business logic
- **Repository**: JPA data access

---

### 7.2 Package Structure

```
com.example.courier
│
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── repository
├── service
├── exception
├── util
└── CourierApplication
```

---

## 8. DTO Strategy

- Entities are never exposed directly
- Separate DTOs for request and response
- Validation via `@Valid`

Example:

- ShipmentRequest
- ShipmentResponse

---

## 9. Exception Handling

Centralized via `@ControllerAdvice`

Handled cases:

- ResourceNotFoundException → 404
- UnauthorizedException → 401
- ValidationException → 400

---

## 10. Database Design (Logical)

Tables:

- users
- customers
- couriers
- shipments
- assignments
- tracking_events

Relationships:

- User 1–1 Customer / Courier
- Shipment 1–Many TrackingEvents
- Shipment 1–1 Assignment

---

## 11. Security Rules (Examples)

| Endpoint               | Access   |
| ---------------------- | -------- |
| Create shipment        | CUSTOMER |
| Assign courier         | ADMIN    |
| Update shipment status | COURIER  |
| Track shipment         | PUBLIC   |

---

## 12. Non-Functional Requirements

- Stateless APIs
- Pagination for list endpoints
- API versioning (`/api/v1`)
- OpenAPI / Swagger documentation

---

## 13. Suggested Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL / MySQL
- Lombok
- MapStruct
- Flyway
- Swagger (OpenAPI)

---

## 14. Extension Points

Future enhancements:

- Payment integration
- Notification services (email/SMS)
- Real-time tracking via WebSockets
- Microservices split

---

## 15. Summary

This document fully defines the **structure, behavior, APIs, and architecture** of the Courier Management System. It is intended to be sufficiently detailed for an LLM or developer to generate:

- Database schema
- Entity models
- Controllers, services, repositories
- Security configuration
- End-to-end working backend

---

**End of Specification**
