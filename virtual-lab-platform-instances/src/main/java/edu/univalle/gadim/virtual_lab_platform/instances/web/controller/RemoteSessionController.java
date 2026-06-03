package edu.univalle.gadim.virtual_lab_platform.instances.web.controller;

import edu.univalle.gadim.virtual_lab_platform.instances.web.model.RemoteSessionResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.RemoteSessionStatusResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.ops.RemoteSessionWsOps;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for remote desktop session management.
 *
 * <p>Provides endpoints for retrieving session metadata, terminating sessions,
 * and checking VNC health. All operations require authenticated access and
 * instance ownership verification, which is enforced at the service layer.
 *
 * <h2>Endpoints</h2>
 * <ul>
 *   <li>{@code GET /api/instances/{instanceId}/remote-session} — retrieve session info</li>
 *   <li>{@code DELETE /api/instances/{instanceId}/remote-session} — terminate session</li>
 *   <li>{@code GET /api/instances/{instanceId}/remote-session/status} — health check</li>
 * </ul>
 *
 * @see RemoteSessionWsOps
 */
@RestController
@RequestMapping("/api/instances/{instanceId}/remote-session")
@ParametersAreNonnullByDefault
public class RemoteSessionController {

  private static final Logger logger = LoggerFactory.getLogger(RemoteSessionController.class);

  private final RemoteSessionWsOps remoteSessionWsOps;

  public RemoteSessionController(RemoteSessionWsOps remoteSessionWsOps) {
    this.remoteSessionWsOps = remoteSessionWsOps;
  }

  @GetMapping
  @Nonnull
  public ResponseEntity<RemoteSessionResponse> getRemoteSession(
      @PathVariable String instanceId) {
    logger.debug("Retrieving remote session for instance: {}", instanceId);
    try {
      return ResponseEntity.ok(remoteSessionWsOps.getRemoteSession(instanceId));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    } catch (SecurityException e) {
      return ResponseEntity.status(403).build();
    }
  }

  @DeleteMapping
  @Nonnull
  public ResponseEntity<Void> terminateRemoteSession(
      @PathVariable String instanceId) {
    logger.info("Terminating remote session for instance: {}", instanceId);
    try {
      remoteSessionWsOps.terminateRemoteSession(instanceId);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/status")
  @Nonnull
  public ResponseEntity<RemoteSessionStatusResponse> getRemoteSessionStatus(
      @PathVariable String instanceId) {
    logger.debug("Checking VNC health for instance: {}", instanceId);
    try {
      return ResponseEntity.ok(remoteSessionWsOps.getRemoteSessionStatus(instanceId));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    } catch (SecurityException e) {
      return ResponseEntity.status(403).build();
    }
  }
}
