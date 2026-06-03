package edu.univalle.gadim.virtual_lab_platform.instances.web.model;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.Instance;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceStatus;
import java.time.LocalDateTime;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Response DTO for instance information.
 *
 * <p>This record contains the complete information about a virtual lab instance, including its
 * configuration, status, and lifecycle timestamps.
 */
@ParametersAreNonnullByDefault
public record InstanceResponse(
    String id,
    String name,
    @Nullable String description,
    String imageName,
    String imageVersion,
    Integer cpuCores,
    Integer memoryMb,
    Integer storageMb,
    Boolean gpuEnabled,
    Integer vncPort,
    Boolean vncEnabled,
    InstanceStatus status,
    LocalDateTime createdAt,
    LocalDateTime expiresAt,
    @Nullable LocalDateTime startedAt,
    @Nullable LocalDateTime stoppedAt,
    @Nullable LocalDateTime deletedAt,
    @Nullable LocalDateTime lastAccessedAt) {

  /**
   * Creates an InstanceResponse from an Instance domain object.
   *
   * @param instance the instance domain object
   * @return the response DTO
   */
  @Nonnull
  public static InstanceResponse from(Instance instance) {
    return new InstanceResponse(
        instance.id(),
        instance.name(),
        instance.description().orElse(null),
        instance.imageName(),
        instance.imageVersion(),
        instance.cpuCores(),
        instance.memoryMb(),
        instance.storageMb(),
        instance.gpuEnabled(),
        instance.vncPort(),
        instance.vncEnabled(),
        instance.status(),
        instance.createdAt(),
        instance.expiresAt(),
        instance.startedAt(),
        instance.stoppedAt().orElse(null),
        instance.deletedAt().orElse(null),
        instance.lastAccessedAt().orElse(null));
  }
}
