/**
 * Domain API of the users bounded context.
 *
 * <p>Holds the service contracts and domain types that constitute the
 * public surface of the module. Persistence and web transport concerns
 * live in sibling packages, so the API remains transport-agnostic and
 * persistence-agnostic.
 *
 * <h2>Sub-packages</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.api.type} — domain interfaces and enums ({@code User}, {@code UserRole}, {@code Role}, {@code UserStatus})</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.api.service} — service contracts ({@code UserService}, {@code UserRoleService})</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.users.api;
