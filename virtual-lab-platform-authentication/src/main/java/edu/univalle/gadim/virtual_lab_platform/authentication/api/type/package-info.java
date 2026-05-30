/**
 * Domain type contracts for the authentication bounded context.
 *
 * <p>Defines the canonical interfaces and enumerations that represent
 * authentication tokens and their lifecycle. JPA entities in the
 * {@link edu.univalle.gadim.virtual_lab_platform.authentication.data.model}
 * package implement these interfaces directly, unifying persistence and domain models.
 *
 * <h2>Key Types</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.api.type.RefreshToken}
 *       — refresh token persistence contract</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.api.type.TokenType}
 *       — JWT token category enumeration</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.authentication.api.type;
