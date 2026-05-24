# Virtual Lab Platform

Objective: “Design and Implementation of a Web Platform for Remote Management and Secure
Virtualization of Computational Resources Using Containers”

The application is a modular web platform designed for university laboratories, where users will be
able to remotely access virtual environments (instances). Within these environments, users will have
access to specialized software such as **Vivado**, **Quartus**, and **KiCad**, as well as
specialized hardware resources.

## Stack Tech

* Java 21
* Spring Boot 4.0.3
* Gradle 9.3.1 (Kotlin DSL)

## Main Structure

```text
virtual-lab-platform
 ├── virtual-lab-platform-boot         # Main application module (entry point)
 ├── virtual-lab-platform-users        # User management module
 ├── virtual-lab-platform-security     # Security module (Not implemented yet) (Spring Security + JWT)
 ├── virtual-lab-platform-instances    # Virtual environments management module
```

### Module Descriptions

- **virtual-lab-platform-boot**  
  Main application module responsible for bootstrapping and running the backend system.

- **virtual-lab-platform-users**  
  Handles user-related operations such as registration, profile management, roles, and permissions.

- **virtual-lab-platform-security**  
  Planned security layer that will manage authentication and authorization using Spring Security and
  JWT.

- **virtual-lab-platform-instances**  
  Responsible for managing virtual environments, isolated instances, and remote execution resources.

---

# Backend Module Architecture

```text
module
 ├── api
 │   ├── type        # Entity interfaces (User, Role, etc.)
 │   └── service     # Service interfaces
 │
 ├── data
 │   ├── model       # JPA entities (UserJpa)
 │   └── repository  # Repositories
 │
 ├── operation       # Service implementations
 │
 ├── web
 │   ├── model       # DTOs
 │   └── controller  # REST controllers
 │
 └── config          # Configuration
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

Exposes the module functionality through REST APIs using controllers and DTOs.

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

