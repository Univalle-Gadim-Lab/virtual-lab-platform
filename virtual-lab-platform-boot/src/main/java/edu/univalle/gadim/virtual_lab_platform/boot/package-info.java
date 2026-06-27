/**
 * Root package of the Spring Boot composition root.
 *
 * <p>Acts as the application entry point and assembly layer: it wires
 * every other module ({@code commons}, {@code users}, {@code instances},
 * {@code authentication}) into a single Spring Boot application, declares
 * the main {@code @SpringBootApplication} class and supplies cross-cutting
 * beans such as the {@link edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator}
 * resolution.
 *
 * <h2>Sub-packages</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.boot.config} — Spring {@code @Configuration} beans ({@code BootConfig}, {@code SecurityConfig})</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.boot.web} — HTTP layer (controllers, DTOs)</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.boot;
