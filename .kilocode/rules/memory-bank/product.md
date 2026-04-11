# Virtual Lab Platform Libraries — Product Overview

## Purpose

- Deliver a unified set of reusable Java libraries to accelerate backend service development.
- Enforce consistent conventions for build, quality, dependency management, and runtime
  configuration.

## Problems this solves

- Fragmented library versions and drift across services.
- Inconsistent quality gates and tooling configuration.
- Boilerplate for common concerns: authentication/authorization, data access, serialization, shared
  domain primitives.
- Slow onboarding due to ad-hoc setups.

## How it works

- Multi-module Gradle build; each module is an independently buildable, testable, and could be
  publishable component library.
- Shared conventions via custom Gradle convention plugins from buildSrc (java, library,
  spring-boot).
- Centralized versions through Gradle Version Catalog and a shared semantic version exposed to all
  modules.
- Quality and security gates applied consistently in root and per-module builds.

## Developer experience goals

- Start new services quickly using starters that bundle common dependencies and sensible defaults.
- Predictable builds (Java 21 toolchain, Gradle 9.3.x, Spring Boot 4.0.x).
- Clear documentation of build/runtime properties and CI expectations.
- Example application for reference and smoke testing.

## Scope

- Component libraries focused on minimal, reusable APIs and minimal transitive dependency exposure.
- Starters to integrate libraries into Spring Boot services with minimal configuration.
- Reference demo application to validate integration of starters.

## Non-goals

- Building fully featured end-user applications in this repository.
- Maintaining multiple conflicting versions of the same third-party stack.

## Success criteria

- Libraries and starters can be added to a new service and pass quality gates without custom setup.
- Upgrades to core stack performed centrally with a low change surface in downstream services.
- CI passes with OSS Index, Checkstyle, SpotBugs, JaCoCo, and Sonar.