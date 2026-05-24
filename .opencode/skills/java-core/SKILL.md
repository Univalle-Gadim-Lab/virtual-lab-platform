---
name: java-core
description: Use when you need to create, update, improve, or refactor Java code. Covers Google Java Style, import policies, variable declarations, and compilation workflow.
---

# Java Code Core Guidelines

Project follows Google Java Style Guide.

## Validation Workflow

**Before any change:**
```bash
./gradlew compileJava
```
If compilation fails, stop immediately — do not proceed until resolved.

**After any change:**
```bash
./gradlew compileJava
```
If compilation fails, fix immediately before continuing.

## Import Policy: No Wildcard Imports

**Rule:** Explicitly list every class and interface. Wildcard imports (`*`) are forbidden.

```java
// Correct
import java.util.List;
import java.util.ArrayList;

// Incorrect
import java.util.*;
```

If you see a `*` in imports, replace it with specific class names.

## Constructor & Factory Method Argument Ordering

Order parameters by conceptual weight:

1. **Identity/Primary Keys** — `id`, `uuid`, `slug`
2. **Required Dependencies** — Core objects for class function (`Service`, `Repository`)
3. **Mandatory Configuration** — Primitives/Strings defining behavior (`maxRetryCount`)
4. **Optional/Nullable Fields** — Values with defaults or may be empty

**Never** put optional flags before primary identifiers.

## Local Variable Declaration: `final var`

Use `final var` for local variables where the reference is not reassigned:

```java
// Correct
final var userService = new UserService();
final var results = userService.findAll();

// Incorrect
UserService userService = new UserService();  // missing final
var results = userService.findAll();          // missing final
```

Exception: If reassignment is required (loops, conditionals), omit `final` but keep `var`.

Applies to both production code and tests.

## Gotchas

- **Checked exceptions:** Wrap in runtime exceptions or declare explicitly — avoid empty catch blocks
- **Resource management:** Always use try-with-resources for `Closeable` objects
- **String comparison:** Use `.equals()` on the literal first (`"constant".equals(variable)`) to avoid NPE

## Constraints

- **DO NOT** add JavaDocs. If modifying a method with existing JavaDocs, append `(STALE!)` to summary
- **DO NOT** run formatting tasks (`./gradlew spotlessApply`) unless explicitly requested
- **DO NOT** run code analysis tasks (`./gradlew checkstyleMain`) unless explicitly requested
- **DO NOT** run tests unless explicitly requested
- **DO NOT** add or update test cases unless explicitly requested
