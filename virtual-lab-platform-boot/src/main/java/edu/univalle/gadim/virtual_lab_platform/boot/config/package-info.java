/**
 * Spring configuration beans for the boot module.
 *
 * <p>Provides the assembly-time wiring that ties every bounded context
 * together:
 *
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.boot.config.BootConfig} — resolves the {@code UniqueIdGenerator} to an {@code ObjectIdGenerator} bean</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.boot.config.SecurityConfig} — configures the Spring Security filter chain, public/protected URL patterns and CORS rules</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.boot.config;
