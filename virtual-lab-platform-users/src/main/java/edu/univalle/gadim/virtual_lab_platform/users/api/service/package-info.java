/**
 * Service contracts for the users bounded context.
 *
 * <p>Defines interfaces for user lifecycle and role assignment operations.
 * Implementations reside in the
 * {@link edu.univalle.gadim.virtual_lab_platform.users.operation} package and
 * depend on Spring Data repositories and the commons {@code UniqueIdGenerator}.
 *
 * <h2>Key Interfaces</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.api.service.UserService} — user creation and retrieval</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.api.service.UserRoleService} — role assignment and lookup</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.users.api.service;
