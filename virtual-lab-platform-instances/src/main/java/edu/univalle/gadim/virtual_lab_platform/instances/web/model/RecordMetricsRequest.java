package edu.univalle.gadim.virtual_lab_platform.instances.web.model;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Request DTO for recording new metrics for an instance.
 *
 * <p>This record contains the current resource utilization metrics to be recorded
 * for a virtual lab instance.
 */
@ParametersAreNonnullByDefault
public record RecordMetricsRequest(
    Double cpuUsage,
    Double memoryUsage,
    Double diskUsage,
    Double timeUsage) {

  /**
   * Validates that all metric values are within acceptable ranges.
   *
   * @return {@code true} if all values are valid
   */
  public boolean isValid() {
    return cpuUsage >= 0.0 && cpuUsage <= 1.0
        && memoryUsage >= 0.0 && memoryUsage <= 1.0
        && diskUsage >= 0.0 && diskUsage <= 1.0
        && timeUsage >= 0.0;
  }
}
