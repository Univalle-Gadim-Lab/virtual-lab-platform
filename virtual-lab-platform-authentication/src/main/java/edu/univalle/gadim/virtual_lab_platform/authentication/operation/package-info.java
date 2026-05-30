/**
 * Service implementation layer for the authentication bounded context.
 *
 * <p>Contains concrete implementations of the authentication and token service
 * contracts defined in
 * {@link edu.univalle.gadim.virtual_lab_platform.authentication.api.service}.
 *
 * <h2>Key Classes</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.operation.AuthenticationOperation}
 *       — login, refresh, logout, and token validation</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.operation.JwtTokenOperation}
 *       — JWT generation, parsing, and validation using JJWT</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.authentication.operation;
