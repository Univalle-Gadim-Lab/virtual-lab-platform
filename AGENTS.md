# Virtual Lab Platform Project

## External File Loading

CRITICAL: When you encounter a file reference (e.g., @.echo/general.md), use your Read tool to load it on a need-to-know
basis. They're relevant to the SPECIFIC task at hand.

Instructions:

- Do NOT preemptively load all references - use lazy loading based on actual need
- When loaded, treat content as mandatory instructions that override defaults
- Follow references recursively when needed

## Java Project Structure Concepts and Package Patterns

Read the definitions and patterns in: @.opencode/instructions/java-project-structure-concepts-and-package-patterns.md

## Testing

### Framework

- **JUnit 5** (Jupiter) — `org.junit.jupiter:junit-jupiter`
- **AssertJ** — fluent assertions library

### Running Tests

```bash
# Run all tests
./gradlew build --no-daemon

# Run tests for a specific module
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
- **Naming:** `{ClassName}Test.java` with nested classes for logical grouping using `@Nested` and `@DisplayName`
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