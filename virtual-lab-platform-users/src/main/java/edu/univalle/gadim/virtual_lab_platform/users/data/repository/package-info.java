/**
 * Spring Data repositories for the users module.
 *
 * <p>Provides data access interfaces for {@link edu.univalle.gadim.virtual_lab_platform.users.data.model.UserJpa}
 * and {@link edu.univalle.gadim.virtual_lab_platform.users.data.model.UserRoleJpa} entities,
 * extending {@code JpaRepository} with custom query methods.
 *
 * <h2>Key Interfaces</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRepository} — user account persistence</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRoleRepository} — role assignment persistence</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.users.data.repository;
