package edu.univalle.gadim.virtual_lab_platform.instances.web.model;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceMetrics;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Response DTO for instance metrics information.
 *
 * <p>This record contains the performance and usage metrics for a virtual lab instance, including
 * CPU, memory, disk, and time usage.
 */
@ParametersAreNonnullByDefault
public record InstanceMetricsResponse(
    String id,
    String instanceId,
    Double currentCpuUsage,
    Double currentMemoryUsage,
    Double currentDiskUsage,
    Double currentTimeUsage) {

  /**
   * Creates an InstanceMetricsResponse from an InstanceMetrics domain object.
   *
   * @param metrics the instance metrics domain object
   * @return the response DTO
   */
  @Nonnull
  public static InstanceMetricsResponse from(InstanceMetrics metrics) {
    return new InstanceMetricsResponse(
        metrics.id(),
        metrics.instanceId(),
        metrics.currentCpuUsage(),
        metrics.currentMemoryUsage(),
        metrics.currentDiskUsage(),
        metrics.currentTimeUsage());
  }
}
