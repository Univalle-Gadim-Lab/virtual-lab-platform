package edu.univalle.gadim.virtual_lab_platform.instances.web.ops;

import edu.univalle.gadim.virtual_lab_platform.instances.web.model.VncProxyResponse;
import javax.annotation.Nonnull;

/**
 * Web service operations contract for VNC HTTP proxy endpoints.
 *
 * <p>Defines endpoint-level operations for proxying HTTP requests
 * to the KasmVNC web server inside a running instance container.
 *
 * <h2>Endpoints</h2>
 * <ul>
 *   <li>{@code GET /api/instances/{instanceId}/vnc/**} — proxy HTTP requests to KasmVNC</li>
 * </ul>
 *
 * @see edu.univalle.gadim.virtual_lab_platform.instances.web.operation.VncProxySpringWsOps
 */
public interface VncProxyWsOps {

  /**
   * Proxies an HTTP request to the KasmVNC web server inside the target container.
   *
   * @param instanceId the instance ID to proxy requests for
   * @param requestSuffix the path suffix after the VNC base path
   * @return the VNC proxy response containing the proxied resource and metadata,
   *     or an empty response with the appropriate error status
   */
  @Nonnull
  VncProxyResponse proxyVncRequest(
      @Nonnull String instanceId, @Nonnull String requestSuffix);
}
