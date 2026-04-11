# Virtual Lab Platform Libraries — Context

## Current work focus

- Initialize the Memory Bank for the Virtual Lab Platform libraries, documenting purpose,
  architecture, technology
  baseline, quality gates, and repeatable tasks.

## Recent findings and state

- Multi-module Gradle build with shared conventions via buildSrc:
- Root build:
    - Root-level dependency constraints present to mitigate known issues (commons-lang3, gson,
      commons-fileupload,
      commons-io)
    - Dependency substitutions prefer local modules for commons and object-serializers-core
- Version catalog:
    - gradle/libs.versions.toml is authoritative for dependency and plugin versions; modules use
      catalog aliases
- Quality gates:
    - Checkstyle rules at build-tools/checkstyle/checkstyle.xml (maxWarnings=0)
    - SpotBugs plugin with exclusions at build-tools/spotbugs/spotbugs-exclude.xml
    - JaCoCo XML reports enabled; JUnit Platform configured

