# Virtual Lab Platform

## Overview

The application is a modular web platform designed for university laboratories, where users will be
able to remotely access virtual environments (instances). Within these environments, users will have
access to specialized software such as **Vivado**, **Quartus**, and **KiCad**, as well as
specialized hardware resources.

---

## Project Objective

> **Design and Implementation of a Web Platform for Remote Management and Secure Virtualization of Computational Resources Using Containers**

## Specific Objectives

1. Analyze the technical and cybersecurity requirements involved in the virtualization of
   computational environments in university laboratories in order to define the minimum software and
   hardware tools required for its development.
2. Design the architecture of a web platform that integrates user management, authentication, access
   control, and resource administration.
3. Implement secure and configurable virtual environments for the execution of specialized software
   and access to physical devices (e.g., FPGAs, Raspberry Pi, and Arduinos), facilitating their use.
4. Validate the platform through a use case in the Digital Architectures laboratory, evaluating
   remote access to computational resources and physical devices.

---

## Tech Stack

* **Java 21**
* **Spring Boot 4.0.3**
* **Gradle 9.3.1** (Kotlin DSL)
* **PostgreSQL** — production database
* **H2** — embedded database (testing)
* **Spring Data JPA** — data access layer
* **Spring Security** — authentication and authorization
* **Lombok** — compile-time code generation
* **docker-java** — Docker client for workspace provisioning
* **MongoDB BSON** — object ID generation
* **Jackson** — JSON serialization
* **JUnit 5, AssertJ, Mockito** — testing
* **Checkstyle / SpotBugs** — static analysis

## Project Modules

```text
virtual-lab-platform
 ├── virtual-lab-platform-boot              # Spring Boot runtime / entry point
 ├── virtual-lab-platform-commons           # Shared utilities (ID generators, helpers)
 ├── virtual-lab-platform-users             # User management bounded context
 ├── virtual-lab-platform-instances         # Virtual environment management bounded context
 └── virtual-lab-platform-authentication    # JWT authentication bounded context
```

### Module Descriptions

- **virtual-lab-platform-boot**
  Main application module. Applies the Spring Boot plugin and is the only runnable module.
  Bootstraps the entire backend and contains `SecurityConfig` for Spring Security setup.

- **virtual-lab-platform-commons**
  Shared infrastructure utilities such as `UniqueIdGenerator`, `UuidGenerator`, and
  `ObjectIdGenerator`. Required as a compile dependency by `users`, `instances`, and `authentication`.

- **virtual-lab-platform-users**
  Handles user-related operations such as registration, profile management, roles, and
  permissions. Also contains `UserSecurityConfig` for user-specific security rules.

- **virtual-lab-platform-instances**
  Responsible for managing virtual environments, isolated instances, remote execution resources,
  and workspace provisioning via Docker.

- **virtual-lab-platform-authentication**
  Provides JWT-based authentication and authorization. Issues access and refresh tokens, validates
  Bearer tokens via a servlet filter, and manages refresh token lifecycle including revocation on
  logout. Depends on commons (for ID generation) and users (for credential verification and role
  loading).

> **Note:** Authentication is implemented as a dedicated module (`authentication`) that provides JWT
> tokens, refresh token persistence, and a servlet filter. Spring Security configuration lives in
> the `boot` module's `SecurityConfig`.

---

## Database Schema

The platform uses six tables organized around two primary entities (**users** and **instances**)
and four supporting tables:

| Table | Purpose |
|-------|---------|
| `users` | User accounts with authentication metadata and lifecycle state |
| `user_roles` | Maps users to roles (`ADMIN`, `STUDENT`, `TEACHER`) |
| `instances` | Virtual lab workspaces with container config and lifecycle timestamps |
| `instance_metrics` | Point-in-time resource utilization snapshots (CPU, memory, disk) |
| `instance_users` | Many-to-many association between users and instances |
| `refresh_tokens` | JWT refresh tokens with revocation support |

All primary keys are application-generated `VARCHAR(100)` strings via `UniqueIdGenerator`.
See [`architecture/DATABASE.md`](architecture/DATABASE.md) for the full schema reference and ERD.

---

# Getting Started

## Prerequisites

- Java 21 JDK
- PostgreSQL 14+ (or Docker to run it)
- Docker (for workspace provisioning and KiCad environments)

## Database Setup

Create the database using the provided scripts:

```bash
# Create database and user
psql -U postgres -f database/create-db.sql

# Create tables
psql -U postgres -d gadim_virtual_lab -f database/schema.sql
```

Connection settings are in `virtual-lab-platform-boot/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/gadim_virtual_lab
    username: postgres
    password: postgres
```

## Seed Default Users

Insert default test users (admin, teacher, student):

```bash
psql -U postgres -d gadim_virtual_lab -f database/seed.sql
```

Default credentials:

| Username | Password | Role |
|----------|----------|------|
| `admin@correounivalle.edu.co` | `admin.GADYM.2026` | ADMIN |
| `teacher@correounivalle.edu.co` | `teacher.GADYM.2026` | TEACHER |
| `student@correounivalle.edu.co` | `student.GADYM.2026` | STUDENT |

Login example:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin@correounivalle.edu.co","password":"admin.GADYM.2026"}'
```

## Build and Run

```bash
# Build the entire project
./gradlew build --no-daemon

# Run all tests
./gradlew test --no-daemon

# Run the application
./gradlew :virtual-lab-platform-boot:bootRun --no-daemon
```

---

# Backend Module Architecture

```text
module
 ├── api
 │   ├── type        # Domain interfaces (User, Role, Instance, etc.)
 │   └── service     # Service interfaces
 │
 ├── data
 │   ├── model       # JPA entities (UserJpa, InstanceJpa)
 │   └── repository  # Spring Data JPA repositories
 │
 ├── operation       # Service implementations (*Operation)
 │
 ├── web
 │   ├── ops         # Web operation interfaces (*WsOps)
 │   ├── operation   # Web operation implementations (*SpringWsOps)
 │   ├── model       # Request/response DTOs (Java records)
 │   └── controller  # REST controllers
 │
 └── config          # Spring @Configuration classes
```

## Layer Responsibilities

### `api`

Defines the public contracts of the module, including entity abstractions and service interfaces.
This layer helps decouple implementations from consumers.

### `data`

Contains persistence-related components such as JPA entities and repositories.
Responsible for database interaction and data access.

### `operation`

Implements the business logic and service behaviors defined in the `api` layer.

### `web`

Exposes the module functionality through REST APIs using controllers, DTOs, and web operation
interfaces/implementations.

**Web Layer Flow:**

```
Controller -> *WsOps interface -> *SpringWsOps impl -> Service interface -> *Operation impl
```

Controllers are thin and return `ResponseEntity<T>`. `IllegalArgumentException` is commonly caught
to produce `404 Not Found`.

### `config`

Contains module-specific configuration classes and application setup.

---

# Architectural Benefits

This structure provides a clear separation between:

- **Contracts** (`api`)
- **Persistence** (`data`)
- **Business Logic** (`operation`)
- **API Exposure** (`web`)

This modular approach improves:

- Maintainability
- Scalability
- Testability
- Separation of concerns
- Long-term extensibility

---

# Security

- **JWT authentication** with stateless sessions via `JwtAuthenticationFilter`
- **Public endpoints:** `POST /api/auth/login`, `POST /api/auth/refresh`
- **Role-based access:** `/api/users/**` and `/api/user-roles/**` require `ADMIN` role
- **All other endpoints** require a valid Bearer token
- **Passwords** hashed with `BCryptPasswordEncoder`
- **Refresh tokens** stored in `refresh_tokens` table; explicitly revoked on logout
- **Soft-delete users:** Status transitions to `DELETED` only from `INACTIVE`; records are never
  physically removed
- **Custom error responses:** JSON `401` for unauthenticated requests, JSON `403` for insufficient
  roles

---

# Additional Documentation

| Document | Description |
|----------|-------------|
| [`architecture/ARCHITECTURE.md`](architecture/ARCHITECTURE.md) | System architecture, module diagrams, component relationships |
| [`architecture/DATABASE.md`](architecture/DATABASE.md) | Physical database schema, ERD diagram, DDL-to-JPA mapping |
| [`architecture/openapi/openapi.yaml`](architecture/openapi/openapi.yaml) | Full OpenAPI 3.0 specification for the REST API |
| [`architecture/virtual-lab-users-module.mermaid`](architecture/virtual-lab-users-module.mermaid) | Mermaid class diagram for the users module |
| [`architecture/virtual-lab-instances-module.mermaid`](architecture/virtual-lab-instances-module.mermaid) | Mermaid class diagram for the instances module |
| [`architecture/virtual-lab-authentication.mermaid`](architecture/virtual-lab-authentication.mermaid) | Mermaid class diagram for the authentication module |
| [`architecture/virtual-lab-module-integration.mermaid`](architecture/virtual-lab-module-integration.mermaid) | Mermaid module integration diagram |

---

# Code Quality

- **Checkstyle:** Google Java Style enforced with `maxWarnings = 0`. Configuration in
  `build-tools/checkstyle/checkstyle.xml`.
- **SpotBugs:** Static analysis with exclusions in `build-tools/spotbugs/spotbugs-exclude.xml`.
- **Line length:** 100 characters.
- **Indentation:** 2 spaces.
- **No wildcard imports**.
- **Mandatory Javadoc** for public types and methods.

---

# Docker Workspaces

A specialized Dockerfile exists for KiCad environments:

```text
virtual-lab-platform-instances/docker/kicad/
 ├── Dockerfile       # Ubuntu 24.04 + KiCad + LXDE + VNC + noVNC
 └── supervisord.conf # Process management for Xvfb, LXDE, X11vnc, websockify
```

This image exposes port `8080` for noVNC remote desktop access.

---
