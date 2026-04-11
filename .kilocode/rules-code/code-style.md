## Code Style and Quality Gates

- Java style rules: build-tools/checkstyle/checkstyle.xml
- Enforcement: Checkstyle is required for all modules. Do not suppress rules without justification.
- Guidance:
    - Ensure new modules apply the Checkstyle plugin and reference the shared configuration.
    - Address warnings as part of the change, not after.
