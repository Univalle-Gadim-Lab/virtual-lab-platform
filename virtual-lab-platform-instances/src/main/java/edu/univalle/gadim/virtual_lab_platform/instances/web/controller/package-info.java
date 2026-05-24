/**
 * REST controllers for the instances module.
 *
 * <p>Exposes instance lifecycle and metrics management operations over HTTP
 * under the {@code /api/instances} base path. Controllers depend exclusively
 * on service interfaces, decoupled from persistence details.
 *
 * <h2>Key Classes</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.controller.InstanceController} — instance CRUD and lifecycle endpoints</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.controller.InstanceMetricsController} — metrics retrieval endpoints</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.instances.web.controller;
