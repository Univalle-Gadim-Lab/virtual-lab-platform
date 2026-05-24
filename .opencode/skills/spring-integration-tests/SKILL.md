---
name: spring-integration-tests
description: Use when writing Spring Boot integration tests. Covers @SpringBootTest configuration, Testcontainers for databases, @Sql scripts, test class naming (<ClassName>InTest), database state management, and integration test patterns. Inherits base testing principles from java-unit-testing.
---

# Spring Boot Integration Testing Guidelines

Guidelines for writing Spring Boot integration tests that verify components working together with real dependencies.

> **Prerequisites**: This skill requires the `java-core`, `java-null-safety`, and `java-unit-testing` skills. All test code must follow Google Java Style and use JSpecify nullness annotations. Base principles (AssertJ, Given-When-Then, `@DisplayName`, `final var`, `@NullMarked`, package-private, single responsibility) carry forward from `java-unit-testing`.

## Test Class Naming

Integration test classes must be named using the convention `<ClassName>InTest`:

```java
// Correct
class LedgerEntryJdbcDaoInTest { ... }
class PaymentServiceInTest { ... }
class UserRepositoryInTest { ... }

// Incorrect
class LedgerEntryJdbcDaoTest { ... }
class PaymentServiceIntegrationTest { ... }
class TestPaymentService { ... }
```

This distinguishes integration tests from unit tests (`UnTest` suffix).

## Test Scope and Visibility

Test classes and methods must have package-private visibility:

```java
// Correct
@NullMarked
class LedgerEntryJdbcDaoInTest {
  @Test
  void shouldCreateAndFindLedgerEntryById() { ... }
}

// Incorrect
public class LedgerEntryJdbcDaoInTest {
  @Test
  public void shouldCreateAndFindLedgerEntryById() { ... }
}
```

## Core Principles

Integration tests inherit these principles from `java-unit-testing`:

1. **Clarity and Readability** — Descriptive names, Given-When-Then structure, focused assertions
2. **Isolation and Independence** — Each test is self-contained with clean database state
3. **Comprehensive Validation** — Cover positive paths, edge cases, boundary conditions, and error scenarios
4. **AssertJ Assertions** — Use AssertJ fluent API consistently (not JUnit assertions)
5. **Single Responsibility** — Each test focuses on one specific behavior

## Spring Boot Test Annotations

Use the standard annotation stack for integration tests:

```java
@SpringBootTest(classes = {LedgerEntryJdbcDao.class, ReserveFlowDataAccessConfig.class})
@Import({DataAccessTestConfig.class})
@Testcontainers
@Sql(
    scripts = {"classpath:ddl/reserve-flow-ddl.sql"},
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@DisplayName("LedgerEntryJdbcDao Integration Tests")
class LedgerEntryJdbcDaoInTest {
  // ...
}
```

### Annotation Stack Explanation

| Annotation | Purpose |
|------------|---------|
| `@SpringBootTest` | Boots Spring context with specified classes |
| `@Import` | Imports test configuration (Testcontainers, test beans) |
| `@Testcontainers` | Enables Testcontainers lifecycle management |
| `@Sql` | Executes DDL scripts before test class |
| `@DisplayName` | Natural language description for test class |

### `@SpringBootTest` Classes Attribute

List only the classes needed for the test:

```java
// For DAO tests — include the DAO and its config
@SpringBootTest(classes = {LedgerEntryJdbcDao.class, ReserveFlowDataAccessConfig.class})

// For service tests — include the service and its dependencies
@SpringBootTest(classes = {PaymentService.class, PaymentConfig.class})
```

## Test Configuration Class

Create a shared `@TestConfiguration` class for Testcontainers setup:

```java
package edu.univalle.gadim.{project}.{module}.tests.spring.cfg;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class DataAccessTestConfig {

  @Bean
  public PostgreSQLContainer<?> postgreSqlContainer() {
    final var container = new PostgreSQLContainer<>("postgres:14-alpine");
    container.start();
    return container;
  }

  @Bean
  public DataSource defaultDataSource(PostgreSQLContainer<?> container) {
    final var dataSourceBuilder = DataSourceBuilder.create();
    dataSourceBuilder.driverClassName(container.getDriverClassName());
    dataSourceBuilder.url(container.getJdbcUrl());
    dataSourceBuilder.username(container.getUsername());
    dataSourceBuilder.password(container.getPassword());
    return dataSourceBuilder.build();
  }
}
```

Import this configuration in tests:

```java
@Import({DataAccessTestConfig.class})
```

## Database State Management

### Option 1: `@BeforeEach` Cleanup

Use `@BeforeEach` to clean database tables before each test:

```java
@Autowired private NamedParameterJdbcOperations jdbcOperations;

@BeforeEach
void setUp() {
  jdbcOperations.getJdbcOperations().execute("DELETE FROM release_schedules");
  jdbcOperations.getJdbcOperations().execute("DELETE FROM ledger_entries");
}
```

Delete in dependency order (child tables before parent tables).

### Option 2: `@Sql` Scripts

Use `@Sql` for schema setup:

```java
@Sql(
    scripts = {"classpath:ddl/reserve-flow-ddl.sql"},
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
```

For per-test data setup:

```java
@Test
@Sql(scripts = {"classpath:test-data/ledger-entries.sql"})
void shouldFindLedgerEntriesByFlowId() { ... }
```

### Option 3: Truncate Tables

For faster cleanup, use TRUNCATE:

```java
@BeforeEach
void setUp() {
  jdbcOperations.getJdbcOperations().execute("TRUNCATE TABLE ledger_entries CASCADE");
}
```

## No @Nested Classes

Integration tests must NOT use `@Nested` classes. Keep all test methods at the top level:

```java
// Correct — flat structure
@NullMarked
class LedgerEntryJdbcDaoInTest {
  @Test
  void shouldCreateAndFindLedgerEntryById() { ... }

  @Test
  void shouldFindEntriesByFlowId() { ... }

  @Test
  void shouldDeleteEntryById() { ... }
}

// Incorrect — nested classes
@NullMarked
class LedgerEntryJdbcDaoInTest {
  @Nested
  class CreateOperations {
    @Test
    void shouldCreateEntry() { ... }
  }

  @Nested
  class FindOperations {
    @Test
    void shouldFindById() { ... }
  }
}
```

Rationale: Integration tests are inherently slower and more complex. Flat structure keeps tests independent and easier to debug.

## Dependency Injection

Use `@Autowired` for Spring-managed dependencies:

```java
@Autowired private LedgerEntryJdbcDao ledgerEntryDao;
@Autowired private NamedParameterJdbcOperations jdbcOperations;
@Autowired private UidGenerator uidGenerator;
```

Do NOT use Mockito `@Mock` or `@MockBean` in integration tests. Use real Spring beans.

## Complete Example

```java
package edu.univalle.gadim.reserve_flow.persistence.daos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.univalle.gadim.nucleus.commons.uid.types.UidGenerator;
import edu.univalle.gadim.reserve_flow.api.builders.LedgerEntryBuilder;
import edu.univalle.gadim.reserve_flow.api.types.Bucket;
import edu.univalle.gadim.reserve_flow.api.types.LedgerDefinitionId;
import edu.univalle.gadim.reserve_flow.api.types.LedgerEntryType;
import edu.univalle.gadim.reserve_flow.spring.cfg.ReserveFlowDataAccessConfig;
import edu.univalle.gadim.reserve_flow.tests.spring.cfg.DataAccessTestConfig;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = {LedgerEntryJdbcDao.class, ReserveFlowDataAccessConfig.class})
@Import({DataAccessTestConfig.class})
@Testcontainers
@Sql(
    scripts = {"classpath:ddl/reserve-flow-ddl.sql"},
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@DisplayName("LedgerEntryJdbcDao Integration Tests")
class LedgerEntryJdbcDaoInTest {

  @Autowired private LedgerEntryJdbcDao ledgerEntryDao;
  @Autowired private NamedParameterJdbcOperations jdbcOperations;
  @Autowired private UidGenerator uidGenerator;

  private Bucket testBucket;
  private String testEntryId;
  private String testFlowId;

  @BeforeEach
  void setUp() {
    jdbcOperations.getJdbcOperations().execute("DELETE FROM ledger_entries");

    testBucket =
        BucketBuilder.create()
            .appId("app1")
            .buyerId("buyer1")
            .sellerId("seller1")
            .obligorId("obligor1")
            .assetId("asset1")
            .build();
    testEntryId = uidGenerator.uid();
    testFlowId = uidGenerator.uid();
  }

  @Test
  @DisplayName("should create ledger entry and retrieve by ID")
  void shouldCreateAndFindLedgerEntryById() {
    // Given
    final var now = LocalDateTime.now();
    final var ledgerEntry =
        LedgerEntryBuilder.create()
            .entryId(testEntryId)
            .flowId(testFlowId)
            .ledgerDefinitionId(LedgerDefinitionId.COLLECTED_RESERVE)
            .bucket(testBucket)
            .entryType(LedgerEntryType.DEPOSIT)
            .absoluteAmount(BigDecimal.valueOf(100))
            .signedAmount(BigDecimal.valueOf(100))
            .entryDate(now)
            .build();

    // When
    final var created = ledgerEntryDao.create(ledgerEntry);
    final var foundOpt = ledgerEntryDao.findById(testEntryId);

    // Then
    assertTrue(foundOpt.isPresent());
    final var found = foundOpt.get();
    assertEquals(testEntryId, found.entryId());
    assertEquals(testFlowId, found.flowId());
    assertEquals(
        now.truncatedTo(ChronoUnit.MICROS),
        found.entryDate().truncatedTo(ChronoUnit.MICROS));
  }

  @Test
  @DisplayName("should find no ledger entry for non-existent ID")
  void shouldNotFindLedgerEntryByNonExistentId() {
    assertTrue(ledgerEntryDao.findById("non-existent").isEmpty());
  }

  @Test
  @DisplayName("should delete ledger entry by ID")
  void shouldDeleteLedgerEntryById() {
    // Given
    final var now = LocalDateTime.now();
    final var ledgerEntry =
        LedgerEntryBuilder.create()
            .entryId(testEntryId)
            .flowId(testFlowId)
            .ledgerDefinitionId(LedgerDefinitionId.COLLECTED_RESERVE)
            .bucket(testBucket)
            .entryType(LedgerEntryType.DEPOSIT)
            .absoluteAmount(BigDecimal.valueOf(100))
            .signedAmount(BigDecimal.valueOf(100))
            .entryDate(now)
            .build();
    ledgerEntryDao.create(ledgerEntry);

    // When
    ledgerEntryDao.deleteById(testEntryId);

    // Then
    assertTrue(ledgerEntryDao.findById(testEntryId).isEmpty());
  }
}
```

## Gotchas

- **Testcontainers requires Docker** — Ensure Docker is running before executing tests
- **PostgreSQL precision** — Database timestamp precision is microseconds; use `truncatedTo(ChronoUnit.MICROS)` for comparisons
- **`@BeforeEach` runs before each test** — Clean all tables that the test might modify
- **Delete order matters** — Delete child tables before parent tables to avoid foreign key violations
- **`@Sql` scripts run once** — Use `BEFORE_TEST_CLASS` for schema, `BEFORE_TEST_METHOD` for data
- **Test configuration bean names** — `DataAccessTestConfig` DataSource bean name must match what the tested class expects
- **`@Testcontainers` annotation** — Must be present for Testcontainers lifecycle hooks
- **`@SpringBootTest(classes = {...})`** — Only list classes needed; avoid loading entire application context

## Constraints

- **DO NOT** use `@Nested` classes in integration tests
- **DO NOT** use Mockito `@Mock` or `@MockBean` — use real Spring beans
- **DO NOT** name tests with `Test` or `Tests` suffix — use `InTest` suffix
- **DO NOT** skip database cleanup in `@BeforeEach` — tests must be independent
- **DO NOT** use JUnit 4 annotations (`@Before`, `@After`, `@Ignore`)
- **DO NOT** use wildcard imports — explicitly list every import
- **DO NOT** make test classes or methods `public`
- **DO NOT** mix JUnit assertions with AssertJ — use AssertJ consistently

## Validation

After creating or modifying integration tests, verify:

1. Tests compile: `./gradlew :<module>:compileTestJava`
2. Tests pass: `./gradlew :<module>:test --tests "*InTest"`
3. Test class follows `<ClassName>InTest` naming convention
4. Test class has `@NullMarked` annotation
5. Test class and methods are package-private
6. All tests use AssertJ assertions (`assertThat`, `assertThatThrownBy`)
7. Each test follows Given-When-Then structure
8. Each test has `@DisplayName` with natural language description
9. `@SpringBootTest` specifies only required classes
10. `@Import` includes test configuration class
11. `@Testcontainers` annotation is present
12. `@BeforeEach` cleans database tables
13. No `@Nested` classes are used
