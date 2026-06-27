package edu.univalle.gadim.virtual_lab_platform.instances.web.model;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.Instance;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceStatus;
import java.time.LocalDateTime;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Response DTO for instance remote session information.
 *
 * <p>Contains metadata about the remote desktop session including VNC connectivity
 * details and expiration time.
 */
public record RemoteSessionResponse(
    String instanceId,
    String status,
    boolean vncEnabled,
    @Nullable String vncUrl,
    LocalDateTime expiresAt) {

  /**
   * Creates a RemoteSessionResponse from an Instance domain object.
   *
   * @param instance the instance domain object
   * @return the remote session response DTO
   */
  @Nonnull
  public static RemoteSessionResponse from(Instance instance) {
    return new RemoteSessionResponse(
        instance.id(),
        mapSessionStatus(instance.status()),
        instance.vncEnabled(),
        buildVncUrl(instance),
        instance.expiresAt());
  }

  private static String mapSessionStatus(InstanceStatus instanceStatus) {
    return switch (instanceStatus) {
      case RUNNING -> "ACTIVE";
      case STARTING -> "CONNECTING";
      case STOPPED, EXPIRED -> "DISCONNECTED";
      case CREATED -> "PENDING";
      case DELETED -> "TERMINATED";
    };
  }

  @Nullable
  private static String buildVncUrl(Instance instance) {
    if (!instance.vncEnabled() || instance.status() != InstanceStatus.RUNNING) {
      return null;
    }
    return "http://"
        + "localhost"
        + ":"
        + instance.vncPort()
        + "?password="
        + instance.vncPassword();
  }
}
