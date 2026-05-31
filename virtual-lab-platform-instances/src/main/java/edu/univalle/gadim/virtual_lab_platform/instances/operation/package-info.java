/**
 * Service implementations for the instances module.
 *
 * <p>Contains operation classes that realize the service contracts defined in
 * {@link edu.univalle.gadim.virtual_lab_platform.instances.api.service}. These classes
 * orchestrate persistence, ID generation, Docker container provisioning, and
 * lifecycle management.
 *
 * <h2>Key Classes</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.operation.InstanceServiceOperation} — implements {@link edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceService}</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.operation.InstanceMetricsServiceOperation} — implements {@link edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceMetricsService}</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.operation.InstanceUserServiceOperation} — implements {@link edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceUserService}</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.operation.WorkspaceProvisionerOperation} — implements {@link edu.univalle.gadim.virtual_lab_platform.instances.api.service.WorkspaceProvisionerService}</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.operation.CatalogServiceOperation} — implements {@link edu.univalle.gadim.virtual_lab_platform.instances.api.service.CatalogService}</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.instances.operation;
