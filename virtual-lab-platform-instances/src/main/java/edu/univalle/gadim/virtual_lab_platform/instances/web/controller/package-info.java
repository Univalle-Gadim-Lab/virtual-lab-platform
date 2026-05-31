/**
 * REST controllers for the instances module.
 *
 * <p>Exposes instance lifecycle, metrics management, and workspace catalog
 * operations over HTTP. Controllers depend exclusively on service interfaces,
 * decoupled from persistence details.
 *
 * <h2>Key Classes</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.controller.InstanceController} — instance CRUD and lifecycle endpoints</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.controller.InstanceMetricsController} — metrics retrieval endpoints</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.controller.CatalogController} — workspace catalog and image discovery endpoints</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.instances.web.controller;
