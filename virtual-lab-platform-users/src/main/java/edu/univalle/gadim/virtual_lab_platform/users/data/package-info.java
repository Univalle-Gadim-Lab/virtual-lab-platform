/**
 * Persistence layer of the users bounded context.
 *
 * <p>Contains JPA entities that implement the domain interfaces declared in
 * {@link edu.univalle.gadim.virtual_lab_platform.users.api.type} together
 * with Spring Data repositories that expose them as query methods.
 *
 * <h2>Sub-packages</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.data.model} — JPA entities ({@code UserJpa}, {@code UserRoleJpa})</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.data.repository} — Spring Data repositories</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.users.data;
