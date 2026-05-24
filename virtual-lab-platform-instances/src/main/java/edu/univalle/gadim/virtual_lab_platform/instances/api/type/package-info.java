/**
 * Domain type contracts for the instances bounded context.
 *
 * <p>Defines the canonical interfaces and enumerations that represent virtual lab
 * instances, their resource metrics, and user associations. JPA entities in the
 * {@link edu.univalle.gadim.virtual_lab_platform.instances.data.model} package
 * implement these interfaces directly, unifying persistence and domain models.
 *
 * <h2>Key Types</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.api.type.Instance} — core virtual workspace contract</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceStatus} — instance lifecycle states</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceMetrics} — resource utilization snapshots</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceUser} — user-to-instance association contract</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.instances.api.type;
