---
name: java-null-safety
description: Use when creating, updating, improving, or refactoring Java code to add JSpecify nullness annotations. Handles migration from JSR-305 or Checker Framework annotations to JSpecify.
---

# JSpecify Null Safety Guidelines

When editing Java code, add JSpecify nullness annotations to express whether types can be `null`.

## Core Annotations

| Annotation | Purpose |
|------------|---------|
| `@NullMarked` | Scope annotation: unannotated types are treated as `@NonNull` |
| `@Nullable` | Type may include `null` |
| `@NonNull` | Type will not include `null` (often implicit in `@NullMarked`) |
| `@NullUnmarked` | Opt-out of `@NullMarked` within a marked scope |

## Class-Level Rule

**Always add `@NullMarked` to new classes.** This makes unannotated reference types non-null by default.

```java
@NullMarked
public class UserService {
  // Unannotated String is treated as @NonNull String
  public String getName(User user) { ... }
  
  // Only annotate what can actually be null
  public @Nullable String getNickname(User user) { ... }
}
```

## Annotation Placement

### Simple Types
```java
@Nullable String name;    // name can be null
String email;             // email cannot be null (in @NullMarked)
```

### Arrays
```java
@Nullable String[] names;     // array elements may be null
String @Nullable [] emails;   // array itself may be null
@Nullable String @Nullable [] mixed; // both array and elements may be null
```

### Nested Types
```java
// To mark nested type nullable, put annotation before the nested name
Map.@Nullable Entry<K, V>  // Entry can be null, not Map
```

### Generics
```java
List<@Nullable String> nullableElements;    // elements may be null
List<String> nonNullElements;               // elements cannot be null

// Type parameter accepting nullable arguments
class ImmutableList<E extends @Nullable Object> { ... }

// Type parameter rejecting nullable arguments
class ImmutableList<E> { ... }  // or E extends Object
```

### Wildcards
```java
List<? extends @Nullable Number>  // elements may be null
List<? extends Number>            // elements cannot be null
```

## Migration from JSR-305

If the file uses JSR-305 annotations (`javax.annotation.*`), refactor to JSpecify:

### 1. Update Imports
```java
// Before
import javax.annotation.Nullable;
import javax.annotation.Nonnull;

// After
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;
```

### 2. Fix Array Syntax
```java
// Before (JSR-305): nullable array of objects
@Nullable Object[]

// After (JSpecify): array of nullable objects (most common intent)
// Just Object[] or @Nullable Object[] (elements nullable)

// To express nullable array itself:
Object @Nullable []
```

### 3. Fix Nested Type Syntax
```java
// Before
@Nullable Map.Entry<K, V>

// After
Map.@Nullable Entry<K, V>
```

### 4. Replace Defaulting Annotations
```java
// Before
@ParametersAreNonnullByDefault
class MyClass { ... }

// After
@NullMarked
class MyClass { ... }
```

### 5. Generic Type Parameter Bounds
```java
// Before: no bound means nullable in JSR-305 world
class Box<T> { ... }

// After: must explicitly declare nullable bound if nullable type args allowed
@NullMarked
class Box<T extends @Nullable Object> { ... }
```

## Local Variables

**Do not annotate local variable root types.** Nullness is inferred from assignments.

```java
@NullMarked
void method(@Nullable String nullable, String nonNull) {
  String a = nullable;     // inferred as nullable
  String b = nonNull;      // inferred as non-null
  String c = random() ? nullable : nonNull;  // inferred as nullable
}
```

## Method Return Types

```java
@NullMarked
class Repository {
  // Return type: nullable
  public @Nullable User findById(Long id) { ... }
  
  // Return type: non-null (no annotation in @NullMarked)
  public User findByIdOrThrow(Long id) { ... }
  
  // Type variable return: nullable only if T could be nullable
  public @Nullable E getFirst(List<E> list) { ... }
}
```

## Gotchas

- `@NullMarked` does NOT apply to local variable root types
- `@NullMarked` does NOT make unannotated type variable uses `@NonNull`; they inherit from bound
- Array annotation position is critical: `@Nullable String[]` ≠ `String @Nullable []`
- Wildcard bounds `<? extends T>` inherit nullness from T's bound
- If migrating from Checker Framework, switching imports is usually sufficient
