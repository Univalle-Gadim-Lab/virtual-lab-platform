/**
 * Domain type contracts for the users bounded context.
 *
 * <p>Defines the canonical interfaces and enumerations that represent user
 * identities and their authorization roles. JPA entities in the
 * {@link edu.univalle.gadim.virtual_lab_platform.users.data.model} package
 * implement these interfaces directly, unifying persistence and domain models.
 *
 * <h2>Key Types</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.api.type.User} — core user identity contract</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.api.type.UserRole} — user-to-role association contract</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.api.type.Role} — system authorization roles</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus} — user lifecycle states</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.users.api.type;
