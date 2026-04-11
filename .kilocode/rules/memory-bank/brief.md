# Virtual Lab Platform Libraries — Project Brief

Authoritative, concise overview for humans and AI agents working on the Virtual Lab platform’s Java component libraries.

## Purpose and Scope

- Purpose: Provide a unified set of reusable component libraries for the Virtual Lab Platform.
- Scope: Multi-module Gradle build where each sub-project is an independent component library that can be built,
  versioned, and released consistently.

## Tech Baseline

- Build: Gradle 9.3.x
- Language: Java 21
- Frameworks:
    - Spring Boot 4.0.x
    - Spring Framework 7.0
- Jakarta EE 11
- Jackson 3.x
- Notes: Prefer conventional Spring Boot module layout and modern Java language features as appropriate.

## Dependency Management

- Dependency tool: Gradle
- Version catalog: gradle/libs.versions.toml
    - Use catalog aliases in build.gradle.kts instead of hard-coded coordinates.
    - Centralizes versions, simplifies upgrades, and ensures consistency.

## Component Library Model

- Multi-module Gradle build”
- Each module is:
    - Independently buildable and testable
    - Aligned with shared conventions (dependencies via version catalog, code style via Checkstyle)
    - Minimal in surface area; avoid leaking transitive dependencies without purpose
