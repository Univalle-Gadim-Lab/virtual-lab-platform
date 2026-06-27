/**
 * HTTP transport layer of the users bounded context.
 *
 * <p>Exposes user and role management operations over REST under the
 * {@code /api/users} and {@code /api/user-roles} base paths, following the
 * canonical Web layer flow:
 *
 * <pre>Controller &rarr; *WsOps interface &rarr; *SpringWsOps impl &rarr; Service interface &rarr; *Operation impl</pre>
 *
 * <h2>Sub-packages</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.web.controller} — REST controllers</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.web.ops} — web-operation contracts</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.web.operation} — web-operation implementations</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.web.model} — request/response DTOs</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.users.web;
