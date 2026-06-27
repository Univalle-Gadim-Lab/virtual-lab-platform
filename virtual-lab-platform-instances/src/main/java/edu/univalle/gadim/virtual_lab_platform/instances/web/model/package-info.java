/**
 * Request and response DTOs for the instances module REST API.
 *
 * <p>Records used to serialize and deserialize HTTP request and response bodies
 * for instance, metrics, and catalog management endpoints.
 *
 * <h2>Key Classes</h2>
 * <ul>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.model.CreateInstanceRequest} — instance creation request</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceResponse} — instance data response</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceMetricsResponse} — metrics data response</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.model.WorkspaceImageResponse} — workspace image catalog entry</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.model.CatalogEntryResponse} — catalog entry with instance count</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.model.RemoteSessionResponse} — remote session metadata response</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.model.RemoteSessionStatusResponse} — VNC health check response</li>
 *   <li>{@link edu.univalle.gadim.virtual_lab_platform.instances.web.model.VncProxyResponse} — VNC HTTP proxy response</li>
 * </ul>
 */
package edu.univalle.gadim.virtual_lab_platform.instances.web.model;
