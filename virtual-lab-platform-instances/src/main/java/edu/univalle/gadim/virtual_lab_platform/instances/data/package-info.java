/**
 * Persistence layer of the instances bounded context.
 *
 * <p>Contains JPA entities that implement the domain interfaces declared
 * in {@link edu.univalle.gadim.virtual_lab_platform.instances.api.type},
 * together with Spring Data repositories that expose them as query methods.
 *
 * <h2>Sub-packages</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.data.model} — JPA entities ({@code InstanceJpa}, {@code InstanceMetricsJpa}, {@code InstanceUserJpa})</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.data.repository} — Spring Data repositories</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.instances.data;
