---
name: java-gradle
description: Use when you need to create, update, improve, or refactor Gradle build scripts, dependency declarations, or version catalogs. Covers version catalogs, dependency constraints, BOMs, and dependency resolution. Triggers for keywords like "dependency", "build.gradle.kts", "libs.versions.toml", "implementation", "api", "runtimeOnly".
---

# Gradle Dependency Management

## Version Catalogs (Required)

**Rule:** All dependencies must use version catalogs. Never use string literals like `"com.google.guava:guava:31.0-jre"`.

### `libs.versions.toml` Structure

```toml
[versions]
guava = "31.0-jre"
junit = "5.10.0"

[libraries]
guava = { group = "com.google.guava", name = "guava", version.ref = "guava" }
androidx-core = { group = "androidx.core", name = "core-ktx", version = "1.12.0" }

[bundles]
testing = ["junit-jupiter", "mockito-core"]
```

### Using the `libs` Accessor

```kotlin
implementation(libs.guava)
implementation(libs.androidx.core)

// In settings.gradle.kts for multi-project:
include(":module")
project(":module").projectDir = file("modules/module")
```

## Dependency Block Ordering

1. **Scope Order:** `api` → `implementation` → `runtimeOnly` → `testImplementation` → `testRuntimeOnly`
2. **Visual Separation:** Exactly one blank line between scope blocks
3. **Alphabetical Sorting:** Within each block, sort by library alias (the name in the catalog)

```kotlin
api(libs.guava)

implementation(libs.androidx.core)
implementation(libs.kotlin.stdlib)

runtimeOnly("org.postgresql:postgresql:42.7.1")

testImplementation(libs.bundles.testing)
testImplementation(libs.junit.jupiter)
```

## Dependency Constraints & Platforms

### Constraints Block

Use `constraints` to enforce versions or align transitive dependencies:

```kotlin
dependencies {
    constraints {
        implementation("org.apache.commons:commons-lang3:3.14.0")
    }
}
```

### BOM (Bill of Materials)

Import a BOM to manage aligned versions:

```kotlin
implementation(platform("com.google.cloud:libraries-bom:26.30.0"))
implementation("com.google.cloud:bigtable-spanner-emulator")
```

## Exclusions

Remove transitive dependencies with exclusions:

```kotlin
implementation(libs.guava) {
    exclude(group = "com.google.errorprone", module = "annotations")
}
```

## Validation

After any dependency change, verify resolution:

```bash
./gradlew dependencies --configuration implementation
./gradlew dependencyInsight --dependency guava
```

## Gotchas

- **Transitive conflicts:** If two libraries bring different versions of the same transitive dependency, Gradle resolves by nearest-first. Use `constraints` to force alignment.
- **Catalog alias naming:** Aliases in `libs.versions.toml` (`guava`, `androidx-core`) become camelCase in Kotlin (`libs.guava`, `libs.androidxCore`).
- **Bundles vs single:** A bundle (`testing = ["junit-jupiter", "mockito-core"]`) adds multiple deps at once — useful for test suites.
- **Version reference:** When a library's version is defined via `[versions]`, use `version.ref = "versionName"`. If the version is inline, omit `version.ref`.
- **Gradle module metadata:** Enable with `enableFeaturePreview("GRADLE_METADATA")` in `settings.gradle.kts` for better IDE support.

## Constraints

- **DO NOT** use string literals like `"com.google.guava:guava:31.0-jre"` — always use `libs.guava`
- **DO NOT** add version numbers directly in `build.gradle.kts` — declare versions in `libs.versions.toml`
- **DO NOT** use `compile` or `compileOnly` — use `implementation` or `api` instead
- **DO NOT** skip `./gradlew dependencies` validation after adding or removing dependencies
