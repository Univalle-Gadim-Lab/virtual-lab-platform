/**
 * Web service operations implementations for the instances module.
 *
 * <p>Contains concrete implementations of the web service operations interfaces
 * defined in {@link edu.univalle.gadim.virtual_lab_platform.instances.web.ops}.
 * Each implementation bridges the HTTP contract to the underlying
 * domain services, handling request-to-domain translation and domain-to-response
 * mapping.
 *
 * <h2>Key Classes</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.operation.InstancesSpringWsOps} —
 *       delegates to {@code InstanceService}</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.operation.InstanceMetricsSpringWsOps} —
 *       delegates to {@code InstanceMetricsService}</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.operation.InstanceUsersSpringWsOps} —
 *       delegates to {@code InstanceUserService}</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.instances.web.operation;
