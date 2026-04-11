# Init Component Library Workflow

A reusable workflow to scaffold a new, empty Nucleus component library that follows repository conventions.

## Steps

1. Ask for the input: component_name (kebab-case), example: `acme-chat`
2. Ask for the input: component_alias (snake_case) default to empty, example: `acchat`
3. Ask for the input: description (one sentence), example: `Acme Chat Component Library.`
4. Evaluate derived values:
    - snake_case_name = kebab_to_snake(component_name) (e.g., `acme-chat` → `acme_chat`)
    - base_package = if component_alias is empty: io.cmt.nucleus.<snake_case_name> (e.g., `io.cmt.nucleus.acme_chat`); 
        else: io.cmt.nucleus.<component_alias> (e.g., `io.cmt.nucleus.acchat`)
    - package_path = `src/main/java/${base_package.replace('.', '/')}`
5. Create module directory structure:
    - <component_name>/${package_path}/
6. Create <component_name>/build.gradle.kts (Use template defined in the templates section)
7. Create <component_name>/${package_path}/package-info.java with the resolved package (Use template defined in the
   templates section)
8. Register the module in the project's root settings.gradle.kts:
   - Ensure an include entry exists in the correct, alphabetically sorted position.
   - Use the repository’s include format, for example:
     ```kotlin
     include(":{{component_name}}")
     ```
9. Run Gradle tasks and ensure the module is recognized by the build:
  ```bash
  ./gradlew :{{component_name}}:tasks --quiet
  ```

## Templates

### <component_name>/build.gradle.kts

```kotlin
plugins {
    id(libs.plugins.cmtbuild.libraryConventions.get().pluginId)
}

val sharedProjectVersion: String by extra

version = sharedProjectVersion

dependencies {
    implementation(libs.guava)
    implementation(libs.log4j.api)
}
```

### <component_name>/${package_path}/package-info.java

```java
  /**
   * {{description}}
   */

  package {{base_package}};
```