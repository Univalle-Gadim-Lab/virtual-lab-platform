/**
 * Domain API of the instances bounded context.
 *
 * <p>Holds the service contracts and domain types that constitute the
 * public surface of the module. Persistence and web transport concerns
 * live in sibling packages, so the API remains transport-agnostic and
 * persistence-agnostic.
 *
 * <h2>Sub-packages</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.api.type} — domain interfaces and enums ({@code Instance}, {@code InstanceMetrics}, {@code InstanceUser}, {@code InstanceStatus}, {@code CatalogEntry}, {@code WorkspaceImage})</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.api.service} — service contracts ({@code InstanceService}, {@code InstanceMetricsService}, {@code InstanceUserService}, {@code CatalogService}, {@code WorkspaceProvisionerService})</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.instances.api;
