/**
 * Spring Data repositories for the instances module.
 *
 * <p>Provides data access interfaces for
 * {@link edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceJpa},
 * {@link edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceMetricsJpa},
 * and {@link edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceUserJpa}
 * entities, extending {@code JpaRepository} with custom query methods.
 *
 * <h2>Key Interfaces</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.data.repository.InstanceRepository} — instance persistence with user-based lookup</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.data.repository.InstanceMetricsRepository} — metrics persistence with instance-based lookup</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.data.repository.InstanceUserRepository} — user-to-instance association persistence</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.instances.data.repository;
