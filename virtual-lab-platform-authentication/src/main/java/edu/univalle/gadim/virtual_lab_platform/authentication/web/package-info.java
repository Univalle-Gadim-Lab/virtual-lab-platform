/**
 * HTTP transport layer of the authentication bounded context.
 *
 * <p>Exposes authentication operations (login, refresh, logout) over REST
 * under the {@code /api/auth} base path and provides the Spring Security
 * filter chain components that enforce JWT-based authentication on
 * downstream requests, following the canonical Web layer flow:
 *
 * <pre>Controller &rarr; *WsOps interface &rarr; *SpringWsOps impl &rarr; Service interface &rarr; *Operation impl</pre>
 *
 * <h2>Sub-packages</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.web.controller} — REST controllers</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.web.ops} — web-operation contracts</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.web.operation} — web-operation implementations</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.web.model} — request/response DTOs</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.authentication.web.security} — Spring Security filter, entry point and access-denied handler</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.authentication.web;
