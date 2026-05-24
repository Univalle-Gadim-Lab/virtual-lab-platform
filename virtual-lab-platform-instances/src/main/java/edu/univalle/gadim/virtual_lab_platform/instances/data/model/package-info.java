/**
 * JPA entities for the instances module.
 *
 * <p>Contains the persistence model for virtual lab instances, resource metrics,
 * and user-to-instance associations. Each entity implements the corresponding
 * domain interface from the
 * {@link edu.univalle.gadim.virtual_lab_platform.instances.api.type} package.
 *
 * <h2>Key Classes</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceJpa} — maps to the {@code instances} table</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceMetricsJpa} — maps to the {@code instance_metrics} table</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceUserJpa} — maps to the {@code instance_users} table</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.instances.data.model;
