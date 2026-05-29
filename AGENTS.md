# Virtual Lab Platform Project

## Project Overview

Multi-module Gradle project using Java 21 and Spring Boot.

### Modules

| Module | Role | Depends on |
|--------|------|------------|
| `virtual-lab-platform-boot` | Spring Boot runtime / entry point | commons, users, instances |
| `virtual-lab-platform-commons` | Shared utilities | — |
| `virtual-lab-platform-users` | User bounded context | commons |
| `virtual-lab-platform-instances` | Instance bounded context | commons, users |

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
- `architecture/openapi/atm-api.yaml` — OpenAPI specification for the Web API
