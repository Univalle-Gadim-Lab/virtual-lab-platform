# Virtual Lab Platform Libraries — Tech

## Technologies

- Language: Java 21
- Build: Gradle 9.3.x
- Frameworks:
    - Spring Boot 4.0.x
    - Spring Framework 7.0.x
- Logging: Log4j 2 API (with Spring Boot default logging excluded in Boot apps)
- Testing:
    - JUnit Jupiter (5)
    - Mockito
    - Testcontainers (Junit Jupiter, PostgreSQL)
    - H2 (for tests)

## Dependency Management

- Version Catalog: gradle/libs.versions.toml
    - Central authority for dependency and plugin versions
    - Use catalog aliases instead of raw coordinates in build files
- Platform/BOMs:
    - Spring Boot Dependencies BOM

## Repositories and Credentials

- Maven repositories:
    - mavenCentral

## Quality Gates

- Checkstyle:
    - Rules: build-tools/checkstyle/checkstyle.xml
    - Enforced with maxWarnings=0

## Security and Compliance

- Prefer BOM-managed versions and version catalog aliases to maintain consistency

## Build and Run Commands

- Build all:
    - ./gradlew clean build

- Baselines validated:
    - Java: 21 (toolchain)
    - Gradle: 9.3.1
    - Spring Boot: 4.0.3