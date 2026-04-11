
# Virtual Lab Platform Libraries — Architecture

## Build and project structure
- Multi-module Gradle build; root centralizes Sonar and OSS Index configuration and enforces a shared semantic version across all modules.
- Dependency versions managed via Gradle Version Catalog (gradle/libs.versions.toml); modules use catalog aliases.
- Root applies dependency constraints for known CVEs and enforces dependency substitutions for local modules.

## Conventions (buildSrc)
- java-conventions:
  - Java 21 toolchain and source/target compatibility
  - Repositories: mavenCentral + internal Maven via system properties
  - Dependency Management import of Spring Boot BOM
  - Quality: Checkstyle (maxWarnings=0), SpotBugs (HTML/XML reports), JaCoCo (XML reports), Javadoc tolerant mode
- library-conventions:
  - java-library + maven-publish configured to internal Maven
  - Sources + Javadoc jars enabled
- spring-boot-conventions:
  - application + Spring Boot plugin
  - Excludes default logging (spring-boot-starter-logging, commons-logging)
  - Layered BootJar and generation of VERSION, BUILD, COMMIT files at build time

## Cross-cutting concerns
- Java 21, Gradle 9