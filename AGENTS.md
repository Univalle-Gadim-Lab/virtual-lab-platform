# Virtual Lab Platform Project

## Project Overview

Multi-module Gradle project using Java 21 and Spring Boot that provides remote management and secure virtualization of computational resources through containers.

### Modules

| Module | Role | Depends on |
|--------|------|------------|
| `virtual-lab-platform-boot` | Spring Boot runtime / entry point / composition root | commons, users, instances, authentication |
| `virtual-lab-platform-commons` | Shared utilities (ID generation) | — |
| `virtual-lab-platform-users` | User bounded context (identity, roles, soft-delete) | commons |
| `virtual-lab-platform-instances` | Instance bounded context (container lifecycle, metrics, catalog, VNC proxy) | commons, users |
| `virtual-lab-platform-authentication` | Authentication bounded context (JWT tokens, login, refresh, logout) | commons, users |

### Cross-Module Dependencies

```
commons ←── users ←── instances
            ↑
            └── authentication
                      ↑
                boot ───┘
```

- **instances → users**: Declared as a Gradle dependency for future user-to-instance authorization checks.
- **authentication → users**: Imports `UserRepository`, `UserRoleRepository`, and `PasswordEncoder` for credential verification and role loading.

## External File Loading

CRITICAL: When you encounter a file reference (e.g., `@.echo/general.md`), use your Read tool to load it on a need-to-know
basis. They're relevant to the SPECIFIC task at hand.

Instructions:

- Do NOT preemptively load all references — use lazy loading based on actual need
- When loaded, treat content as mandatory instructions that override defaults
- Follow references recursively when needed

## Java Project Structure Concepts and Package Patterns

Read the definitions and patterns in: `@.opencode/instructions/java-project-structure-concepts-and-package-patterns.md`

### Package Layers

Each module uses the base package `edu.univalle.gadim.virtual_lab_platform.<module>` with the following layers:

| Package | Purpose |
|---------|---------|
| `api.type` | Domain interfaces & enums |
| `api.service` | Service interfaces |
| `data.model` | JPA entities implementing domain interfaces |
| `data.repository` | Spring Data JPA repositories |
| `operation` | Service implementations (suffix `*Operation`) |
| `config` | Spring `@Configuration` classes |
| `web.ops` | Web operation interfaces (suffix `*WsOps`) |
| `web.operation` | Web operation implementations (suffix `*SpringWsOps`) |
| `web.controller` | REST controllers |
| `web.model` | Request/response DTOs (Java `record`s) |
| `vnc` | VNC WebSocket proxy handler (KasmVNC broker) |

**Every package must contain a `package-info.java`** with Javadoc describing the package contents.

### Web Layer Flow

```
Controller -> *WsOps interface -> *SpringWsOps impl -> Service interface -> *Operation impl
```

Controllers are thin and return `ResponseEntity<T>`. `IllegalArgumentException` is commonly caught to produce `404 Not Found`.

### Domain Model Patterns

- JPA entities implement domain interfaces directly (e.g., `InstanceJpa implements Instance`).
- `equals()` / `hashCode()` are based **only on the `id`** field.
- Interface methods return primitive `int`/`boolean` for non-null fields and `Optional<T>` for nullable fields.
- Lombok is used on JPA entities: `@Getter`, `@Setter`, `@Builder`, `@ToString`, `@NoArgsConstructor`, `@AllArgsConstructor`.

## Build & Tooling

- **Java:** 21
- **Gradle:** Kotlin DSL (`.gradle.kts`)
- **Spring Boot BOM:** Managed via `dependencyManagement` plugin
- **Checkstyle:** Google Java Style (`maxWarnings = 0`, 100-character line limit, 2-space indent, no star imports, mandatory Javadoc for public types)

## Testing

### Framework

- **JUnit 5** (Jupiter) — `org.junit.jupiter:junit-jupiter`
- **AssertJ** — fluent assertions library
- **Mockito** — `org.mockito:mockito-core`

### Running Tests

```bash
# Run all tests
./gradlew build --no-daemon

# Run tests for a specific module
./gradlew :virtual-lab-platform-commons:test --no-daemon
./gradlew :virtual-lab-platform-users:test --no-daemon
./gradlew :virtual-lab-platform-instances:test --no-daemon
./gradlew :virtual-lab-platform-authentication:test --no-daemon
```

### Dependency Setup

When adding tests to a module, include these in `build.gradle.kts`:

```kotlin
testImplementation(libs.junit)
testImplementation(libs.assertj)
testRuntimeOnly("org.junit.platform:junit-platform-launcher")
```

### Test Conventions

- **Location:** `src/test/java/` — mirror the source package structure (e.g., `...data.model` for `...data.model` classes)
- **No comments:** Do not add comments in test code (same as production code)
- **Naming:** Use the following suffixes:
  - `{ClassName}Test.java` — general tests
  - `{ClassName}UnTest.java` — unit tests
  - `{ClassName}BuilderTest.java` — builder tests
- **Grouping:** Use nested classes for logical grouping with `@Nested` and `@DisplayName`:

```java
@DisplayName("InstanceJpa")
class InstanceJpaTest {

    @Nested
    @DisplayName("No-args constructor")
    class NoArgsConstructor {
        // ...
    }
}
```

- **Assertion style:** Use **chained `returns()`** for same-object field verification instead of multiple standalone `assertThat()` calls:

```java
// Preferred
assertThat(instance)
    .returns("id", InstanceJpa::getId)
    .returns("name", InstanceJpa::getName)
    .returns(InstanceStatus.RUNNING, InstanceJpa::getStatus);

// Also acceptable for multi-field snapshots
assertThat(instance)
    .extracting(InstanceJpa::getId, InstanceJpa::getName)
    .containsExactly("id", "name");
```

### Null Annotations in Tests

- Production code uses `javax.annotation.Nonnull`, `@Nullable`, and `javax.annotation.ParametersAreNonnullByDefault`.
- Test code uses `org.jspecify.annotations.NullMarked`.

## Project Structure / Architecture Documentation

Architecture and design documentation lives under `architecture/`:

- `architecture/ARCHITECTURE.md` — Class-level system architecture, module diagrams, component relationships
- `architecture/DATABASE.md` — Physical database schema, ERD diagram, DDL-to-JPA mapping reference
- `architecture/openapi/openapi.yaml` — OpenAPI specification for the Web API
- `architecture/virtual-lab-authentication.puml` — PlantUML class diagram for the authentication module
- `architecture/virtual-lab-instances-module.puml` — PlantUML class diagram for the instances module
- `architecture/virtual-lab-users-module.puml` — PlantUML class diagram for the users module
- `architecture/vnc-integration-analysis.md` — VNC/KasmVNC integration analysis and design rationale
- `architecture/virtual-lab-module-integration.puml` — PlantUML diagram showing cross-module integration

### Virtual Lab UI (External Project)

The frontend project (`virtual-lab-ui`) lives in a separate repository. Its architecture documentation covers both frontend (React/TypeScript) and backend (Java/Spring Boot) system design:

- `/Users/guedaf/Proyectos/virtual-lab-ui/architecture/ARCHITECTURE.md` — Full-stack system architecture: frontend feature organization, backend module design, component relationships

## Architecture and API References

The following table lists all architecture and API documentation resources. Consult these documents before making architectural or implementation decisions.

| Document | Purpose | When to Use |
|----------|---------|-------------|
| `architecture/ARCHITECTURE.md` | System architecture, module breakdown, class diagrams, web layer patterns | Before implementing new features, refactoring, or adding new modules; to understand the WsOps pattern or module boundaries |
| `architecture/DATABASE.md` | Physical database schema, column types, constraints, foreign keys, enum values | Before modifying JPA entities, adding columns, changing relationships, or writing repository queries; to check nullable/unique constraints |
| `architecture/openapi/openapi.yaml` | HTTP API contracts, request/response schemas, endpoint definitions | Before implementing or consuming REST endpoints; to verify endpoint paths, HTTP methods, request/response shapes, and status codes |
| `architecture/virtual-lab-authentication.puml` | Authentication module class diagram (API types, services, operations, web layer, security) | When working on authentication features, JWT token handling, or security filter configuration |
| `architecture/virtual-lab-users-module.puml` | Users module class diagram (User, UserRole, services, controllers) | When modifying user management, role assignment, or user-related API endpoints |
| `architecture/virtual-lab-instances-module.puml` | Instances module class diagram (Instance, metrics, catalog, Docker integration) | When modifying instance lifecycle, metrics collection, workspace catalog, or container provisioning |
| `architecture/virtual-lab-module-integration.puml` | Cross-module dependency and integration diagram | Before adding cross-module dependencies; to understand how boot assembles the application from all modules |
| `architecture/vnc-integration-analysis.md` | VNC/KasmVNC integration analysis: architecture, proxy flow, session lifecycle | When modifying VNC proxy, remote desktop access, or container VNC configuration |

### Key Architectural Decisions to Keep in Mind

1. **Web layer pattern**: Controllers → `WsOps` interface → `SpringWsOps` implementation → Service interface → `Operation` implementation. Never call service interfaces directly from controllers.
2. **Domain interfaces**: All domain types (`User`, `Instance`, etc.) are Java interfaces; JPA entities implement them directly. The persistence model *is* the domain model.
3. **Primitive types in domain interfaces**: Non-nullable numeric and boolean fields use primitives (`int`, `boolean`) in domain interfaces; JPA entities and response DTOs use boxed types (`Integer`, `Boolean`) where nullable.
4. **Email as user ID**: `users.id` stores the institutional email address, not a generated UUID.
5. **Soft deletes**: Users and instances use lifecycle status transitions (`ACTIVE → INACTIVE → DELETED`) rather than physical row deletion.
6. **ID generation**: All IDs except `users.id` are generated by `UniqueIdGenerator` (resolved to `ObjectIdGenerator` at assembly time).
7. **Shared types vs implementation**: The "Shared API Types" diagram in ARCHITECTURE.md is aspirational. Always refer to module-specific diagrams for the current implementation.
8. **Security**: `/api/auth/login`, `/api/auth/refresh`, and `/api/health` are public; `/api/users/**` and `/api/user-roles/**` require ADMIN role; all others require authenticated JWT.
9. **VNC / KasmVNC**: Browser-based remote desktop is proxied through the backend (`VncProxyController` and `VncWebSocketProxyHandler`) to KasmVNC servers inside Docker containers on port 6901. JWT tokens are passed via `?token=` query parameter for iframe and WebSocket authentication. The frontend embeds the KasmVNC web client in an iframe at `/workspace/:id/desktop`.
