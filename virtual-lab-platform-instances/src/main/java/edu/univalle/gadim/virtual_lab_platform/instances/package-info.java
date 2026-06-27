/**
 * Root package of the instances bounded context.
 *
 * <p>Provides container lifecycle management, CPU/RAM metrics collection,
 * workspace catalog, and VNC/WebSocket proxying for remote virtualization
 * of computational workspaces. Depends on
 * {@link edu.univalle.gadim.virtual_lab_platform.commons} for unique-ID
 * generation and on {@link edu.univalle.gadim.virtual_lab_platform.users}
 * as an indirect dependency for future user-to-instance authorization checks.
 *
 * <h2>Sub-packages</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.api} — domain types and service contracts</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.data} — JPA entities and repositories</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.operation} — service implementations</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.config} — Spring {@code @Configuration} beans</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.vnc} — VNC WebSocket proxy handler (KasmVNC broker)</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web} — HTTP layer (controllers, WsOps, DTOs)</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.instances;
