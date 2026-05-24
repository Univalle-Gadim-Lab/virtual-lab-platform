---
name: spring-jdbc-dao
description: Use when creating or updating a Spring JDBC DAO module. Covers DAO interface and implementation generation, Spring configuration class, and application YAML reference. Produces <DaoName>Dao interface, <DaoName>JdbcDao implementation, <ProjectName><ModuleName>JdbcDaoConfig configuration, and application-reference.yml entries.
---

# Spring JDBC DAO Generator

Guidelines for creating Spring JDBC Data Access Objects following the established patterns.

> **Prerequisites**: This skill requires the `java-core`, `java-null-safety`, and `api-type-builder` skills. All generated code must follow Google Java Style and use JSpecify nullness annotations.

## Module Components

A JDBC DAO module consists of:

1. **DAO Interface** — `<DaoName>Dao.java`
2. **DAO Implementation** — `<DaoName>JdbcDao.java`
3. **Spring Configuration** — `<ProjectName><ModuleName>JdbcDaoConfig.java`
4. **Application YAML Reference** — `<module>/application-reference.yml`

Both interface and implementation must reside in the **same package**.

## DAO Interface

### Naming Convention

- Interface name: `<DaoName>Dao` (e.g., `LedgerEntryDao`, `PaymentDao`)

### Structure

```java
package edu.univalle.gadim.{project}.{module}.persistence.daos;

import edu.univalle.gadim.{project}.{module}.api.types.{Type1};
import edu.univalle.gadim.{project}.{module}.api.types.{Type2};
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface LedgerEntryDao {

  @NonNull
  LedgerEntry create(LedgerEntry ledgerEntry);

  @NonNull
  Optional<LedgerEntry> findById(String entryId);

  @NonNull
  List<LedgerEntry> findByFlowId(String flowId);

  void deleteById(String entryId);
}
```

### Interface Rules

- **DO NOT** add Javadocs
- Annotate the interface with `@NullMarked`
- Return types that are never null use `@NonNull` (or omit in `@NullMarked` scope)
- Return `Optional<T>` for queries that may not find results
- Use `@Nullable` only when the method can explicitly return null (prefer `Optional` instead)

## DAO Implementation

### Naming Convention

- Implementation name: `<DaoName>JdbcDao` (e.g., `LedgerEntryJdbcDao`, `PaymentJdbcDao`)

### Structure

```java
package edu.univalle.gadim.{project}.{module}.persistence.daos;

import static edu.univalle.gadim.{project}.{module}.spring.cfg.{ProjectName}{ModuleName}JdbcDaoConfig.JDBC_OPS_BEAN_NAME;

import edu.univalle.gadim.{project}.{module}.api.builders.LedgerEntryBuilder;
import edu.univalle.gadim.{project}.{module}.api.types.LedgerEntry;
import edu.univalle.gadim.{project}.{module}.api.types.LedgerEntryType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

@NullMarked
@Repository
public class LedgerEntryJdbcDao implements LedgerEntryDao {

  private static final String ENTRY_ID_COL = "entry_id";
  private static final String FLOW_ID_COL = "flow_id";
  private static final String ENTRY_TYPE_COL = "entry_type";
  private static final String AMOUNT_COL = "amount";
  private static final String ENTRY_DATE_COL = "entry_date";

  private static final String INSERT_SQL =
      """
      INSERT INTO ledger_entries (
          entry_id,
          flow_id,
          entry_type,
          amount,
          entry_date
      ) VALUES (
          :entry_id,
          :flow_id,
          :entry_type,
          :amount,
          :entry_date
      )
      """;

  private static final String SELECT_BY_ID_SQL =
      "SELECT * FROM ledger_entries WHERE entry_id = :entry_id";

  private static final String SELECT_BY_FLOW_ID_SQL =
      "SELECT * FROM ledger_entries WHERE flow_id = :flow_id";

  private static final String DELETE_BY_ID_SQL =
      "DELETE FROM ledger_entries WHERE entry_id = :entry_id";

  private final NamedParameterJdbcOperations jdbcOperations;

  private final RowMapper<LedgerEntry> rowMapper = new LedgerEntryRowMapper();

  public LedgerEntryJdbcDao(
      @Qualifier(JDBC_OPS_BEAN_NAME) NamedParameterJdbcOperations jdbcOperations) {
    this.jdbcOperations = jdbcOperations;
  }

  @Override
  public @NonNull LedgerEntry create(LedgerEntry ledgerEntry) {
    final var parameters =
        new MapSqlParameterSource()
            .addValue(ENTRY_ID_COL, ledgerEntry.entryId())
            .addValue(FLOW_ID_COL, ledgerEntry.flowId())
            .addValue(ENTRY_TYPE_COL, ledgerEntry.entryType().name())
            .addValue(AMOUNT_COL, ledgerEntry.amount())
            .addValue(ENTRY_DATE_COL, ledgerEntry.entryDate());

    this.jdbcOperations.update(INSERT_SQL, parameters);

    return ledgerEntry;
  }

  @Override
  public @NonNull Optional<LedgerEntry> findById(String entryId) {
    final var parameters = new MapSqlParameterSource().addValue(ENTRY_ID_COL, entryId);

    return this.jdbcOperations.query(SELECT_BY_ID_SQL, parameters, rowMapper).stream()
        .findFirst();
  }

  @Override
  public @NonNull List<LedgerEntry> findByFlowId(String flowId) {
    final var parameters = new MapSqlParameterSource().addValue(FLOW_ID_COL, flowId);

    return this.jdbcOperations.query(SELECT_BY_FLOW_ID_SQL, parameters, rowMapper);
  }

  @Override
  public void deleteById(String entryId) {
    final var parameters = new MapSqlParameterSource().addValue(ENTRY_ID_COL, entryId);

    this.jdbcOperations.update(DELETE_BY_ID_SQL, parameters);
  }

  private static final class LedgerEntryRowMapper implements RowMapper<LedgerEntry> {

    @Override
    public LedgerEntry mapRow(ResultSet resultSet, int i) throws SQLException {
      return LedgerEntryBuilder.create()
          .entryId(resultSet.getString(ENTRY_ID_COL))
          .flowId(resultSet.getString(FLOW_ID_COL))
          .entryType(LedgerEntryType.valueOf(resultSet.getString(ENTRY_TYPE_COL)))
          .amount(resultSet.getBigDecimal(AMOUNT_COL))
          .entryDate(resultSet.getTimestamp(ENTRY_DATE_COL).toLocalDateTime())
          .build();
    }
  }
}
```

### Implementation Rules

- **DO NOT** add Javadocs
- Annotate the class with `@NullMarked` and `@Repository`
- Inject `NamedParameterJdbcOperations` via constructor with `@Qualifier(JDBC_OPS_BEAN_NAME)`
- **Create string constants** for all literals:
  - Column names: `private static final String ENTRY_ID_COL = "entry_id";`
  - SQL statements: `private static final String INSERT_SQL = """...""";`
- Use `MapSqlParameterSource` for query parameters
- Implement `RowMapper` as a private static inner class
- Use builders from `api-type-builder` skill to construct return types

## Spring Configuration Class

### Naming Convention

- Config class name: `<ProjectName><ModuleName>JdbcDaoConfig`
- Example: `ReserveFlowLedgerJdbcDaoConfig`, `PaymentProcessingDaoConfig`

### Configuration Key Pattern

The DataSource name is resolved from external configuration:

```
<project-name>.<module-name>.jdbc.datasource-name
```

Example: `reserve-flow.ledger.jdbc.datasource-name`

### Structure

```java
package edu.univalle.gadim.{project}.{module}.spring.cfg;

import javax.sql.DataSource;
import org.jspecify.annotations.NullMarked;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@NullMarked
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackageClasses = {LedgerEntryJdbcDao.class})
public class ReserveFlowLedgerJdbcDaoConfig {

  public static final String JDBC_OPS_BEAN_NAME = "reserveFlowLedgerJdbcOps";

  private final ApplicationContext applicationContext;

  public ReserveFlowLedgerJdbcDaoConfig(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Bean(JDBC_OPS_BEAN_NAME)
  public NamedParameterJdbcOperations namedParameterJdbcOperations(
      @Value("${reserve-flow.ledger.jdbc.datasource-name}") String dataSourceName) {
    final var dataSource = applicationContext.getBean(dataSourceName, DataSource.class);
    return new NamedParameterJdbcTemplate(dataSource);
  }
}
```

### Configuration Rules

- Annotate with `@NullMarked`, `@Configuration`, `@EnableTransactionManagement`
- Use `@ComponentScan(basePackageClasses = {...})` to register DAO beans
- Define a `public static final String JDBC_OPS_BEAN_NAME` constant
- Create `NamedParameterJdbcOperations` bean that:
  - Accepts `@Value("${...}") String dataSourceName` parameter
  - Resolves `DataSource` from `ApplicationContext` by bean name
  - Returns `new NamedParameterJdbcTemplate(dataSource)`

## Application YAML Reference

Create or update `<module>/application-reference.yml` with the configuration key stub:

```yaml
{project-name}:
  {module-name}:
    jdbc:
      datasource-name: <datasource-bean-name>
```

**Example** (`reserve-flow/application-reference.yml`):

```yaml
reserve-flow:
  ledger:
    jdbc:
      datasource-name: defaultDataSource
```

The actual `datasource-name` value will be supplied at runtime based on the deployment environment.

## Reuse Existing Types

Before creating a DAO:

1. Search for existing API types in `api/types/` and their builders in `api/builders/`
2. Reuse existing interfaces and builders whenever possible
3. **Ask the user before creating any new type or DTO**

If a new type is needed, use the `api-type-builder` skill to generate both the interface and its builder.

## Complete Example

### DAO Interface

```java
package edu.univalle.gadim.payment.processing.persistence.daos;

import edu.univalle.gadim.payment.processing.api.types.Payment;
import edu.univalle.gadim.payment.processing.api.types.PaymentId;
import edu.univalle.gadim.payment.processing.api.types.PaymentStatus;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NonNull;

@NullMarked
public interface PaymentDao {

  @NonNull
  Payment create(Payment payment);

  @NonNull
  Optional<Payment> findById(PaymentId paymentId);

  @NonNull
  List<Payment> findByStatus(PaymentStatus status);

  @NonNull
  List<Payment> findByStatuses(List<PaymentStatus> statuses);

  void updateStatus(PaymentId paymentId, PaymentStatus status);

  void deleteById(PaymentId paymentId);
}
```

### DAO Implementation

```java
package edu.univalle.gadim.payment.processing.persistence.daos;

import static edu.univalle.gadim.payment.processing.spring.cfg.PaymentProcessingJdbcDaoConfig.JDBC_OPS_BEAN_NAME;

import edu.univalle.gadim.payment.processing.api.builders.PaymentBuilder;
import edu.univalle.gadim.payment.processing.api.types.Payment;
import edu.univalle.gadim.payment.processing.api.types.PaymentId;
import edu.univalle.gadim.payment.processing.api.types.PaymentStatus;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

@NullMarked
@Repository
public class PaymentJdbcDao implements PaymentDao {

  private static final String PAYMENT_ID_COL = "payment_id";
  private static final String STATUS_COL = "status";
  private static final String AMOUNT_COL = "amount";
  private static final String CREATED_AT_COL = "created_at";

  private static final String INSERT_SQL =
      """
      INSERT INTO payments (payment_id, status, amount, created_at)
      VALUES (:payment_id, :status, :amount, :created_at)
      """;

  private static final String SELECT_BY_ID_SQL =
      "SELECT * FROM payments WHERE payment_id = :payment_id";

  private static final String SELECT_BY_STATUS_SQL =
      "SELECT * FROM payments WHERE status = :status ORDER BY created_at DESC";

  private static final String SELECT_BY_STATUSES_SQL =
      "SELECT * FROM payments WHERE status IN (:statuses) ORDER BY created_at DESC";

  private static final String UPDATE_STATUS_SQL =
      "UPDATE payments SET status = :status WHERE payment_id = :payment_id";

  private static final String DELETE_BY_ID_SQL =
      "DELETE FROM payments WHERE payment_id = :payment_id";

  private final NamedParameterJdbcOperations jdbcOperations;
  private final RowMapper<Payment> rowMapper = new PaymentRowMapper();

  public PaymentJdbcDao(
      @Qualifier(JDBC_OPS_BEAN_NAME) NamedParameterJdbcOperations jdbcOperations) {
    this.jdbcOperations = jdbcOperations;
  }

  @Override
  public @NonNull Payment create(Payment payment) {
    final var parameters =
        new MapSqlParameterSource()
            .addValue(PAYMENT_ID_COL, payment.paymentId().value())
            .addValue(STATUS_COL, payment.status().name())
            .addValue(AMOUNT_COL, payment.amount())
            .addValue(CREATED_AT_COL, payment.createdAt());

    jdbcOperations.update(INSERT_SQL, parameters);
    return payment;
  }

  @Override
  public @NonNull Optional<Payment> findById(PaymentId paymentId) {
    final var parameters = new MapSqlParameterSource()
        .addValue(PAYMENT_ID_COL, paymentId.value());

    return jdbcOperations.query(SELECT_BY_ID_SQL, parameters, rowMapper).stream()
        .findFirst();
  }

  @Override
  public @NonNull List<Payment> findByStatus(PaymentStatus status) {
    final var parameters = new MapSqlParameterSource()
        .addValue(STATUS_COL, status.name());

    return jdbcOperations.query(SELECT_BY_STATUS_SQL, parameters, rowMapper);
  }

  @Override
  public @NonNull List<Payment> findByStatuses(List<PaymentStatus> statuses) {
    final var statusNames = statuses.stream().map(PaymentStatus::name).toList();
    final var parameters = new MapSqlParameterSource()
        .addValue("statuses", statusNames);

    return jdbcOperations.query(SELECT_BY_STATUSES_SQL, parameters, rowMapper);
  }

  @Override
  public void updateStatus(PaymentId paymentId, PaymentStatus status) {
    final var parameters = new MapSqlParameterSource()
        .addValue(PAYMENT_ID_COL, paymentId.value())
        .addValue(STATUS_COL, status.name());

    jdbcOperations.update(UPDATE_STATUS_SQL, parameters);
  }

  @Override
  public void deleteById(PaymentId paymentId) {
    final var parameters = new MapSqlParameterSource()
        .addValue(PAYMENT_ID_COL, paymentId.value());

    jdbcOperations.update(DELETE_BY_ID_SQL, parameters);
  }

  private static final class PaymentRowMapper implements RowMapper<Payment> {

    @Override
    public Payment mapRow(ResultSet resultSet, int i) throws SQLException {
      return PaymentBuilder.create()
          .paymentId(PaymentId.valueOf(resultSet.getString(PAYMENT_ID_COL)))
          .status(PaymentStatus.valueOf(resultSet.getString(STATUS_COL)))
          .amount(resultSet.getBigDecimal(AMOUNT_COL))
          .createdAt(resultSet.getTimestamp(CREATED_AT_COL).toLocalDateTime())
          .build();
    }
  }
}
```

### Spring Configuration

```java
package edu.univalle.gadim.payment.processing.spring.cfg;

import javax.sql.DataSource;
import edu.univalle.gadim.payment.processing.persistence.daos.PaymentJdbcDao;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@NullMarked
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackageClasses = {PaymentJdbcDao.class})
public class PaymentProcessingJdbcDaoConfig {

  public static final String JDBC_OPS_BEAN_NAME = "paymentProcessingJdbcOps";

  private final ApplicationContext applicationContext;

  public PaymentProcessingJdbcDaoConfig(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Bean(JDBC_OPS_BEAN_NAME)
  public NamedParameterJdbcOperations namedParameterJdbcOperations(
      @Value("${payment.processing.jdbc.datasource-name}") String dataSourceName) {
    final var dataSource = applicationContext.getBean(dataSourceName, DataSource.class);
    return new NamedParameterJdbcTemplate(dataSource);
  }
}
```

### Application YAML Reference

**File**: `payment-processing/application-reference.yml`

```yaml
payment:
  processing:
    jdbc:
      datasource-name: defaultDataSource
```

## Gotchas

- **JDBC_OPS_BEAN_NAME** must be unique across all DAO config classes — use a descriptive prefix like `{project}{module}JdbcOps`
- **IN clause parameters** use `List<T>` values in `MapSqlParameterSource` — the parameter name in SQL uses `:paramName` but the value is a list
- **LIKE clause patterns** must append the wildcard in Java code (`prefix + "%"`) — never embed wildcards in SQL strings
- **Optional fields** from builders use `.orElse(null)` when binding parameters
- **`@Qualifier` is mandatory** — multiple `NamedParameterJdbcOperations` beans may exist in a multi-module project
- **RowMapper instances** can be stored as instance fields since they're stateless
- **`@EnableTransactionManagement`** enables annotation-driven transaction management for the module's DAOs

## Validation

After creating or modifying a JDBC DAO module, verify:

1. Code compiles: `./gradlew :<module>:compileJava`
2. Interface is annotated with `@NullMarked`
3. Implementation is annotated with `@NullMarked` and `@Repository`
4. All SQL strings and column names are declared as `private static final String` constants
5. Implementation constructor uses `@Qualifier(JDBC_OPS_BEAN_NAME)`
6. Configuration class is annotated with `@Configuration`, `@EnableTransactionManagement`, and `@ComponentScan`
7. Configuration defines `public static final String JDBC_OPS_BEAN_NAME`
8. `application-reference.yml` contains the `datasource-name` configuration key
