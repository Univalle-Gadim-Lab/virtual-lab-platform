/**
 * Web service operations contracts for the instances module.
 *
 * <p>Defines interfaces that specify one method per REST endpoint exposed
 * by the instances module. These contracts decouple the HTTP controller layer
 * from the underlying domain services, enabling independent testing and
 * clear separation of concerns.
 *
 * <h2>Key Classes</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.ops.InstancesWsOps} —
 *       contract for instance lifecycle management web operations</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.ops.InstanceMetricsWsOps} —
 *       contract for instance metrics web operations</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.ops.InstanceUsersWsOps} —
 *       contract for instance-user association web operations</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.ops.CatalogWsOps} —
 *       contract for workspace catalog web operations</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.ops.RemoteSessionWsOps} —
 *       contract for remote session web operations</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.ops.VncProxyWsOps} —
 *       contract for VNC HTTP proxy web operations</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.instances.web.ops;
