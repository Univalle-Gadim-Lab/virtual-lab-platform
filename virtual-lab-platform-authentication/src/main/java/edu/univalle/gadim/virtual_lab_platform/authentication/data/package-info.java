/**
 * Persistence layer of the authentication bounded context.
 *
 * <p>Contains JPA entities that implement the domain interfaces declared in
 * {@link edu.univalle.gadim.virtual_lab_platform.authentication.api.type}
 * together with Spring Data repositories that expose them as query methods.
 *
 * <h2>Sub-packages</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.data.model} — JPA entities ({@code RefreshTokenJpa})</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.data.repository} — Spring Data repositories</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.authentication.data;
