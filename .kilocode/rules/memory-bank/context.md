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

## virtual-platform Application

### Modules:
- virtual-platform-boot
    - Clase main
    - Configuración general
    - Wiring final
    - Este módulo depende de todos los demás.

- Seguridad como módulo propio
      - Users como dominio puro
      - Instances como dominio puro
      - Boot como ensamblador
      - JWT en security
      - BCrypt en security
      - Control de ownership en instances

- virtual-platform-security
  - SecurityConfig
  - JWT Provider
  - JWT Filter
  - AuthenticationEntryPoint
  - PasswordEncoder bean
  - CustomUserDetailsService (o interfaz que consuma users)
  - Este módulo NO debe tener lógica de negocio. Solo infraestructura de seguridad.

- virtual-platform-users
  - Entidad User
  - Entidad Role
  - Repositorios
  - Servicios CRUD
  - Lógica de negocio de usuarios
  - No debe conocer nada de JWT.

- virtual-platform-instances 
  - Entidad Instance 
  - Asociación con User 
  - Lógica de ownership 
  - Lógica de virtualización

- virtual-platform-core (opcional pero recomendable)
  - Excepciones comunes 
  - Response wrappers 
  - Auditoría base 
  - Interfaces compartidas

