# Java nullability

## General guidelines

- Favor non-null by default for parameters, fields, and return values.
- Do not return `null` from methods. If absence is a valid outcome, return `Optional<T>`.
- Do not use `Optional` for fields or parameters, reserve it only for return types.
- For primitive return types and parameters, do not add nullability annotations.

## Annotations

- Use the annotations from the package `javax.annotation`.
- Annotate every class with `@ParametersAreNonnullByDefault` to assume parameters are non-null by default unless
  explicitly annotated otherwise.
- For methods and return types, annotate with `@Nonnull` or `@Nullable` as appropriate:
    - If a method never returns null, use `@Nonnull`.
    - If a method may return null, use `@Nullable`.
    - If a method return type is a Java primitive, don't add annotation.
- For constructor parameters and methods that explicitly accept or allow null values, annotate those parameters with
  `@Nullable`.

## Example

```java
package com.example.myservice;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SampleService {

  private final String id;

  @Nullable
  private final String description;

  public SampleService(String id, @Nullable String description) {
    this.id = id;
    this.description = description;
  }

  @Nonnull
  public String getId() {
    return id;
  }

  @Nonnull
  public Optional<String> getDescription() {
    return Optional.ofNullable(description);
  }

  public void setDescription(@Nullable String newDescription) {
    description = newDescription;
  }

  @Nonnull
  public String statusMessage() {
    return description != null ? "Description set" : "No description";
  }
}
```