---
name: api-type-builder
description: Generate Java builder classes for API type interfaces. Use when creating or updating a builder class for an existing API type interface, or when adding a new method to an existing type interface that requires a corresponding builder field and setter.
---

# API Type Builder Generator

Generate Java builder classes for API type interfaces following the established pattern.

> **Prerequisite**: This skill requires the `java-null-safety` skill. All generated builder classes must use JSpecify nullness annotations.

## Interface Analysis

Read the source interface and classify each method:

- **Required field**: method returns a plain type (e.g., `String`, `BigDecimal`, `LocalDateTime`, `Bucket`)
- **Optional field**: method returns `Optional<T>` (e.g., `Optional<String>`, `Optional<LocalDate>`)

## Builder Class Generation

### Package and Class Declaration

- Package: `builders` sibling to `types` package
- Declaration: `@NullMarked public final class {InterfaceName}Builder`

### Fields

All fields declared as `private {Type} {name}`. Optional fields (from `Optional<T>` in interface) must be annotated `@Nullable`.

```java
private String entryId;
private @Nullable String releaseEntryId;  // was Optional<String> in interface
private BigDecimal amount;
```

### Constructor

Private no-arg constructor:

```java
private PaymentBuilder() {}
```

### Static Factory Methods

```java
public static PaymentBuilder create() {
  return new PaymentBuilder();
}

public static PaymentBuilder createFrom(Payment payment) {
  final var builder = create();
  builder.copy(payment);
  return builder;
}
```

### Copy Method

For required fields: direct assignment. For `Optional<T>` fields: unwrap with `.orElse(null)`.

```java
public PaymentBuilder copy(Payment payment) {
  this.paymentId = payment.paymentId();
  this.referenceCode = payment.referenceCode().orElse(null);
  this.amount = payment.amount();
  return this;
}
```

### Setter Methods

All setters return `this` for chaining. Optional field setters accept `@Nullable` parameter.

```java
public PaymentBuilder paymentId(String paymentId) {
  this.paymentId = paymentId;
  return this;
}

public PaymentBuilder referenceCode(@Nullable String referenceCode) {
  this.referenceCode = referenceCode;
  return this;
}
```

### Build Method

`Preconditions.checkNotNull` for every **required** field only. Construct the Value class with all fields in declaration order.

```java
public Payment build() {
  Preconditions.checkNotNull(paymentId, "Required field paymentId must be set");
  Preconditions.checkNotNull(amount, "Required field amount must be set");
  return new PaymentValue(paymentId, referenceCode, amount);
}
```

### Inner Value Class

`@Value private static class {InterfaceName}Value implements {InterfaceName}`

Override every interface method:
- Non-optional fields: return `this.{name}` directly
- `Optional` fields: wrap with `Optional.ofNullable(this.{name})`

Optional fields (from `Optional<T>` in interface) must be annotated `@Nullable`.

```java
@Value
private static class PaymentValue implements Payment {
  String paymentId;
  @Nullable String referenceCode;
  BigDecimal amount;

  @Override
  public String paymentId() {
    return this.paymentId;
  }

  @Override
  public Optional<String> referenceCode() {
    return Optional.ofNullable(this.referenceCode);
  }

  @Override
  public BigDecimal amount() {
    return this.amount;
  }
}
```

## Imports

```java
import com.google.common.base.Preconditions;
import edu.univalle.gadim.{project}.api.types.{InterfaceName};
import edu.univalle.gadim.{project}.api.types.{ReferencedType1};
import edu.univalle.gadim.{project}.api.types.{ReferencedType2};
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
```

Only import types actually used in the builder.

## Interface Update

Add a `static builder()` method to the source interface:

```java
import edu.univalle.gadim.{project}.api.builders.{InterfaceName}Builder;

public interface {InterfaceName} {
  static {InterfaceName}Builder builder() {
    return {InterfaceName}Builder.create();
  }

  // ... existing methods
}
```

## Complete Example

**Input interface:**

```java
package edu.univalle.gadim.example.api.types;

import edu.univalle.gadim.example.api.builders.PaymentBuilder;
import java.math.BigDecimal;
import java.util.Optional;

public interface Payment {
  static PaymentBuilder builder() {
    return PaymentBuilder.create();
  }

  String paymentId();
  Optional<String> referenceCode();
  BigDecimal amount();
}
```

**Generated builder:**

```java
package edu.univalle.gadim.example.api.builders;

import com.google.common.base.Preconditions;
import edu.univalle.gadim.example.api.types.Payment;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class PaymentBuilder {
  private String paymentId;
  private @Nullable String referenceCode;
  private BigDecimal amount;

  private PaymentBuilder() {}

  public static PaymentBuilder create() {
    return new PaymentBuilder();
  }

  public static PaymentBuilder createFrom(Payment payment) {
    final var builder = create();
    builder.copy(payment);
    return builder;
  }

  public PaymentBuilder copy(Payment payment) {
    this.paymentId = payment.paymentId();
    this.referenceCode = payment.referenceCode().orElse(null);
    this.amount = payment.amount();
    return this;
  }

  public PaymentBuilder paymentId(String paymentId) {
    this.paymentId = paymentId;
    return this;
  }

  public PaymentBuilder referenceCode(@Nullable String referenceCode) {
    this.referenceCode = referenceCode;
    return this;
  }

  public PaymentBuilder amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public Payment build() {
    Preconditions.checkNotNull(paymentId, "Required field paymentId must be set");
    Preconditions.checkNotNull(amount, "Required field amount must be set");
    return new PaymentValue(paymentId, referenceCode, amount);
  }

  @Value
  private static class PaymentValue implements Payment {
    String paymentId;
    @Nullable String referenceCode;
    BigDecimal amount;

    @Override
    public String paymentId() {
      return this.paymentId;
    }

    @Override
    public Optional<String> referenceCode() {
      return Optional.ofNullable(this.referenceCode);
    }

    @Override
    public BigDecimal amount() {
      return this.amount;
    }
  }
}
```

## Gotchas

- The builder class must be annotated with `@NullMarked`
- Optional fields (from `Optional<T>` in interface) must be annotated with `@Nullable` on both the field and setter parameter
- Field order in `build()` constructor call must match field declaration order
- `Optional` fields in the builder are stored as bare nullable references — `Optional.ofNullable()` wrapping only happens in the Value class method overrides
- All builder setter methods return `this` for chaining, regardless of field optionality
- Required vs optional classification comes **only** from the interface return type: `Optional<T>` = optional, everything else = required
- Skip `Preconditions.checkNotNull` for `Optional` fields — they are inherently nullable

## Validation

After generating the builder, verify:

1. The builder compiles: `./gradlew compileJava`
2. The builder class is annotated with `@NullMarked`
3. Every interface method has a corresponding setter in the builder
4. Every required field has a `Preconditions.checkNotNull` in `build()`
5. Every `Optional` field in the Value class wraps with `Optional.ofNullable()`
6. Every optional field (from `Optional<T>` in interface) is annotated with `@Nullable`
