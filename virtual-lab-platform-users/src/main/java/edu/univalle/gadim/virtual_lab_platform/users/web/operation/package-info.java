/**
 * Web service operations implementations for the users module.
 *
 * <p>Contains concrete implementations of the {@link edu.univalle.gadim.virtual_lab_platform.users.web.ops.UsersWsOps}
 * interface. Each implementation bridges the HTTP contract to the underlying
 * domain services, handling request-to-domain translation and domain-to-response
 * mapping.
 *
 * <h2>Key Classes</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.web.operation.UsersSpringWsOps} —
 *       delegates to {@code UserService} and {@code UserRoleService}</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.users.web.operation;
