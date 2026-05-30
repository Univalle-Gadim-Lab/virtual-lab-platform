/**
 * Service contract interfaces for the authentication bounded context.
 *
 * <p>Defines the operations available for login, token lifecycle management,
 * and access token validation. Implementations reside in the
 * {@link edu.univalle.gadim.virtual_lab_platform.authentication.operation} package.
 *
 * <h2>Key Interfaces</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.api.service.AuthenticationService}
 *       — login, refresh, logout, and token validation</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.api.service.TokenService}
 *       — JWT generation, parsing, and validation</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.authentication.api.service;
