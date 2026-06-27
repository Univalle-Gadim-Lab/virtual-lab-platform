/**
 * Root package of the authentication bounded context.
 *
 * <p>Issues and validates JSON Web Tokens for user authentication, exposing
 * login, token refresh and logout endpoints. Depends on
 * {@link edu.univalle.gadim.virtual_lab_platform.commons} for type utilities
 * and on {@link edu.univalle.gadim.virtual_lab_platform.users} for credential
 * verification and role loading.
 *
 * <h2>Sub-packages</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.api} — domain types and service contracts</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.data} — JPA entities and repositories</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.operation} — service implementations</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.config} — Spring {@code @Configuration} beans</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.web} — HTTP layer (controllers, WsOps, DTOs, security filters)</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.authentication;
