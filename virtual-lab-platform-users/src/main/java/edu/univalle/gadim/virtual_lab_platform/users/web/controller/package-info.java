/**
 * REST controllers for the users module.
 *
 * <p>Exposes user and role management operations over HTTP under the
 * {@code /api/users} and {@code /api/user-roles} base paths. Controllers
 * depend exclusively on service interfaces, decoupled from persistence details.
 *
 * <h2>Key Classes</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.web.controller.UserController} — user CRUD endpoints</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.web.controller.UserRoleController} — role assignment endpoints</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.users.web.controller;
