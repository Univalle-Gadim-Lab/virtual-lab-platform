/**
 * Root package of the users bounded context.
 *
 * <p>Provides identity, role assignment and lifecycle operations for platform
 * users. Depends only on the {@code commons} module for unique-ID generation
 * and is consumed by both the {@code instances} and {@code authentication}
 * modules for user and role lookups.
 *
 * <h2>Sub-packages</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.api} — domain types and service contracts</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.data} — JPA entities and repositories</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.operation} — service implementations</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.config} — Spring {@code @Configuration} beans</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.web} — HTTP layer (controllers, WsOps, DTOs)</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.users;
