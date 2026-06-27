/**
 * HTTP transport layer of the instances bounded context.
 *
 * <p>Exposes instance lifecycle, metrics, workspace catalog and remote
 * session operations over REST and WebSocket, following the canonical Web
 * layer flow:
 *
 * <pre>Controller &rarr; *WsOps interface &rarr; *SpringWsOps impl &rarr; Service interface &rarr; *Operation impl</pre>
 *
 * <h2>Sub-packages</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.controller} — REST controllers</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.ops} — web-operation contracts</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.operation} — web-operation implementations</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.model} — request/response DTOs</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.instances.web;
