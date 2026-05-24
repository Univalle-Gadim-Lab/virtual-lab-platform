# Java Project Structure Concepts and Package Patterns

This document outlines the hierarchical structure, core concepts, and strict package patterns for organizing Java-based
software projects, ensuring a scalable path from the root repository down to deployable applications.

---

## 1. The Macro Level: Projects & Components

### The Project

The highest organizational tier, representing the root of the codebase and the boundary for version control and CI/CD.

* **Infrastructure:** Maps 1:1 with a single Git repository and resides in the root folder.
* **Automation:** Governed by build tools (Gradle, npm, bun) and CI/CD pipelines (GitHub Actions).
* **Composition:** Acts as the container for 1 to *n* Components or Modules.
* **Example:** `docman`

### The Component

A logical grouping mechanism used to organize code by domain rather than strictly by technical function.

* **Purpose:** Defines a conceptual functional group.
* **Composition:** Contains 1 to *n* underlying Modules.
* **Examples:** `core`, `schema`, `store`, `ws`, `commons`

---

## 2. The Building Blocks: Modules

### The Module

The fundamental unit of implementation where the actual source code and core logic reside.
> **Key Rule:** Modules should remain isolated from runtime concerns so they can be easily shared and tested.

* **Build Integration:** Mapped directly to a sub-project in Gradle.
* **Distribution:** Can be independently published as a packaged library to an artifact repository.
* **Naming Convention:** Prefixed with the parent Component name for clear namespace organization.
* **Examples:** `core-api`, `core`, `schema-api`, `schema-folder`, `schema-file`, `store-api`, `store-filesystem`,
  `store-s3`, `web`, `commons`

---

## 3. Module Categories & Naming Rules

Modules are categorized based on whether they belong to a web service component (`ws`) and the specific suffix applied
to the module's name.

* **API Module:** Used for core, non-web components.
    * **Rule:** The Component name is *not* `web`, and the Module suffix is `api`.
* **Implementation Module:** Holds the concrete logic for non-web components.
    * **Rule:** The Component name is *not* `web`.
* **Web Service API Module:** Defines the contracts and models specifically for web-facing services.
    * **Rule:** The Component name *must be* `web`.

---

## 4. The Execution Layer: Services & Applications

Both Services and Applications are specialized modules. Their primary job is *not* to hold core business logic, but
rather to provide the runtime environment (typically via **Spring Boot**) to execute the logic built in the underlying
Modules.

| Feature             | Service                                                                               | Application                                                                                  |
|:--------------------|:--------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------|
| **Primary Role**    | Exposes specific, tightly-related backend capabilities over a network (usually HTTP). | The top-level executable unit; ties everything together for end-users, often including a UI. |
| **Scope**           | Narrow and cohesive.                                                                  | Broad; designed to solve multiple, complex use cases and cross-domain business rules.        |
| **Dependencies**    | Relies entirely on underlying Components/Modules for business logic.                  | Consumes both underlying Components/Modules *and* network Services.                          |
| **Configuration**   | Adds Spring Boot bootstrapping and server configs.                                    | Adds Spring Boot bootstrapping, orchestration logic, and UI assets.                          |

---

## 5. Java Package Patterns

The organizational strategy relies on a consistent base namespace, structured as *
*`edu.univalle.gadim.$project.$component[.$moduleSuffix]`**. From this root namespace, the project is divided into the module
categories defined above, each with its own predefined package hierarchy.

### API Module

Defines the core contracts and internal structures.

* **`$namespace.type`**: Component Types (Interfaces, Abstract Classes, Enums, Immutable Classes).
* **`$namespace.builder`**: Default builders for Component Types.
* **`$namespace.service`**: Component Services Interfaces.
* **`$namespace.internal.type`**: Internal Component Types.
* **`$namespace.internal.builder`**: Default builders for Internal Component Types.
* **`$namespace.internal.operatino`**: Internal Component Operations Interfaces.
* **Examples:** `edu.univalle.gadim.docman.core.api.type`, `edu.univalle.gadim.docman.core.api.internal.operation`

### Implementation Module

Provides the concrete implementations and persistence layers for the defined APIs.

* **`$namespace.services`**: Services implementations.
* **`$namespace.internal.ops`**: Internal operations implementations.
* **`$namespace.data.model`**: Data models (JPA entities, Custom POJOS).
* **`$namespace.data.daos`**: Data Access Objects (JDBC, JPA, Spring Data).
* **`$namespace.spring`**: Spring artifacts for the module.
* **`$namespace.spring.cfg`**: Spring configuration classes specific to the module.
* **Examples:** `edu.univalle.gadim.docman.core.services`, `edu.univalle.gadim.docman.core.data.model`, `edu.univalle.gadim.docman.core.spring`

### Web Service API Module

Dictates the models and operational interfaces for web-facing services.

* **`$namespace.model`**: Serializable elements like Commands, Requests, and Responses (JSON/Protobuf).
* **`$namespace.ops`**: Web Services Operations Interfaces (Interface name suffix: "*WsOps").

### Web Service Implementation Module

Contains the controllers and implementations serving the web API operations.

* **`$namespace.operation`**: Web Services Operations Implementations (Name suffix: "*WsOps").
* **`$namespace.web`**: Web API Controllers (Name suffix: "*WsApi").
* **`$namespace.page`**: Page Controllers (Name suffix: "*Pages").
* **`$namespace.grpc`**: gRPC Services (Name suffix: "*Grpc").
* **`$namespace.spring`**: Spring configuration classes for the module.

### Web Service Runtime Module

Provides the execution environment, tying the application together.

* **`$namespace`**: Location for the Entrypoint Class (SpringBoot).
* **`$namespace.spring`**: Spring configuration classes for the module.

### Uncategorized Module

For modules that fall outside the standard categories.

* **`$namespace`**: No predefined structure, but development should consistently apply the patterns established
  elsewhere.
