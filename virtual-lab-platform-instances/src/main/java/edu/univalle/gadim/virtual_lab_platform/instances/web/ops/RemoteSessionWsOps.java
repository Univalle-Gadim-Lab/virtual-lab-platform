package edu.univalle.gadim.virtual_lab_platform.instances.web.ops;

import edu.univalle.gadim.virtual_lab_platform.instances.web.model.RemoteSessionResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.RemoteSessionStatusResponse;
import javax.annotation.Nonnull;

/**
 * Web service operations contract for remote session management.
 *
 * <p>Defines endpoint-level operations for remote desktop session access,
 * providing metadata retrieval, session termination, and health verification.
 *
 * <h2>Endpoints</h2>
 * <ul>
 *   <li>{@code GET /api/instances/{instanceId}/remote-session} — retrieve session info</li>
 *   <li>{@code DELETE /api/instances/{instanceId}/remote-session} — terminate session</li>
 *   <li>{@code GET /api/instances/{instanceId}/remote-session/status} — health check</li>
 * </ul>
 */
public interface RemoteSessionWsOps {

  /**
   * Retrieves remote session metadata for an instance.
   *
   * @param instanceId the instance ID
   * @return the remote session response with VNC connectivity details
   */
  @Nonnull
  RemoteSessionResponse getRemoteSession(@Nonnull String instanceId);

  /**
   * Terminates the remote desktop session by stopping the instance container.
   *
   * @param instanceId the instance ID
   */
  void terminateRemoteSession(@Nonnull String instanceId);

  /**
   * Verifies whether the VNC server inside the instance container is reachable.
   *
   * @param instanceId the instance ID
   * @return the health check response indicating reachability
   */
  @Nonnull
  RemoteSessionStatusResponse getRemoteSessionStatus(@Nonnull String instanceId);
}
