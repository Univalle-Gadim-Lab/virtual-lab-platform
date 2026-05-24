---
name: gradle-java-tasks
description: Use this skill when working with Gradle-based Java projects, even if the user doesn't explicitly mention Gradle. Triggers for tasks involving compilation, running tests, or checking test results. Run Gradle tasks for Java projects — compile code, run tests, and view test reports.
---

# Gradle CLI Tasks for Java

**Use the Gradle wrapper (`./gradlew`) for all commands.** It ensures consistent Gradle versions across environments.

## Workflow

Progress:
- [ ] **Step 1: Discover modules** — List available modules to target the right one
- [ ] **Step 2: Run targeted task** — Compile or test only what's needed
- [ ] **Step 3: Verify results** — Check output and reports

### Step 1: Discover Modules

Before running tasks, identify the module structure:

```bash
./gradlew projects
```

This lists all subprojects. Use the full module path with colons (e.g., `:app:core`).

### Step 2: Run Targeted Tasks

**Default: Target the specific module you're working on.** This saves time and context.

#### Compilation

```bash
# Specific module (recommended)
./gradlew :<module>:compileJava
./gradlew :<module>:compileTestJava

# All modules (only when needed)
./gradlew compileJava
```

#### Running Tests

```bash
# Specific test class (recommended)
./gradlew test --tests "com.example.MyTestClass"

# Specific test method
./gradlew test --tests "com.example.MyTestClass.testMethod"

# Pattern matching
./gradlew test --tests "*Integration*"

# Specific module's tests
./gradlew :<module>:test

# Continue running other tests after failures
./gradlew test --continue
```

### Step 3: Verify Results

**Test reports location:**
- Single module: `<module>/build/reports/tests/test/index.html`
- Root project: `build/reports/tests/test/index.html`

## Gotchas

- **Never use `build` for routine compilation/testing.** It runs full build lifecycle including packaging — slow and unnecessary.
- **`check` and `spotlessApply` are restricted.** Only use when explicitly requested.
- **Module names use colons.** Nested modules like `app/core` become `:app:core` in Gradle commands.
- **Test patterns are glob patterns.** Use `*` for wildcards, not regex.
- **Failures stop execution by default.** Use `--continue` to run all tests despite failures.

## Excluding Tests

To exclude specific tests:

```bash
./gradlew test -x test --tests "!*Slow*"
```