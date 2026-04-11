# Gradle Commands

The Gradle wrapper (`./gradlew`) is located at the project root and should be used for all commands to ensure a
consistent Gradle version.

## Compilation

Java and Kotlin source compilation tasks are commonly available (depending on what the module applies).

- Compile Java for a specific module:
  ```bash
  ./gradlew :<module-name>:compileJava
  ```
- Compile Java for all modules:
  ```bash
  ./gradlew compileJava
  ```
- Compile Java tests for a specific module:
  ```bash
  ./gradlew :<module-name>:compileTestJava
  ```
- Compile Java tests for all modules:
  ```bash
  ./gradlew compileTestJava
  ```

If Kotlin is used, these tasks may also be present:

- Compile Kotlin for a specific module:
  ```bash
  ./gradlew :<module-name>:compileKotlin
  ```
- Compile Kotlin tests for a specific module:
  ```bash
  ./gradlew :<module-name>:compileTestKotlin
  ```

## Running Tests

- Run all tests:
  ```bash
  ./gradlew test
  ```
- Run tests for a specific module:
  ```bash
  ./gradlew :<module-name>:test
  ```
- Continue even if some tests fail:
  ```bash
  ./gradlew test --continue
  ```
- Run tests for a specific class:
  ```bash
  ./gradlew test --tests "com.example.MyTestClass"
  ```
- Run a specific test method:
  ```bash
  ./gradlew test --tests "com.example.MyTestClass.myTestMethod"
  ```
- Run tests matching a pattern:
  ```bash
  ./gradlew test --tests "*Integration*"
  ```
- Exclude specific tests (pattern):
  ```bash
  ./gradlew test -x test --tests "!*Slow*"
  ```
  Note: You can also exclude via `test { exclude '**/Slow*' }` in Gradle config.

### JUnit 5 tags (if configured)

- Include tests with tags:
  ```bash
  ./gradlew test -Djunit.jupiter.tags=fast,unit
  ```
- Exclude tests with tags:
  ```bash
  ./gradlew test -Djunit.jupiter.exclude.tags=slow
  ```

### Test Reports

After running tests, HTML reports are generated at:

- Single module:
  ```
  <module-name>/build/reports/tests/test/index.html
  ```
- Root (aggregated only if configured; otherwise check each module path as above)

### Code formatting with spotless

- Format code for a specific class (absolute path)
  ```bash
  ./gradlew spotlessApply -PspotlessIdeHook=/absolute/path/to/TheClass.java
  ```

Tips:

- Replace <module-name> with your actual module path (for nested modules, use colons, e.g., :app:core).
