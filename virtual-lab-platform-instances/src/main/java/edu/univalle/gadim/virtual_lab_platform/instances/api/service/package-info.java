/**
 * Service contracts for the instances bounded context.
 *
 * <p>Defines interfaces for instance lifecycle management, resource metrics
 * recording, user-to-instance associations, Docker workspace provisioning,
 * and workspace catalog access. Implementations reside in the
 * {@link edu.univalle.gadim.virtual_lab_platform.instances.operation} package.
 *
 * <h2>Key Interfaces</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceService} — instance CRUD and lifecycle</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceMetricsService} — metrics recording and retrieval</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceUserService} — user-to-instance associations</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.api.service.WorkspaceProvisionerService} — Docker container provisioning</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.api.service.CatalogService} — workspace catalog and image discovery</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.instances.api.service;
