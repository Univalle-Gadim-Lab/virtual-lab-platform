/**
 * Security filter components for the authentication module.
 *
 * <p>Provides the JWT authentication filter that validates Bearer tokens
 * and populates the Spring Security context for authenticated requests.
 *
 * <h2>Key Classes</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.web.security.JwtAuthenticationFilter}
 *       — per-request filter for JWT validation</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.web.security.JwtAuthenticationEntryPoint}
 *       — returns JSON 401 when no valid JWT is present</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.web.security.JwtAccessDeniedHandler}
 *       — returns JSON 403 when a valid JWT lacks sufficient privileges</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.authentication.web.security;
