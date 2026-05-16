package edu.univalle.gadim.virtual_lab_platform.instances.api.service;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceMetrics;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * Service interface for managing instance metrics.
 *
 * <p>This interface defines operations for retrieving and recording performance
 * and usage metrics for virtual lab instances.
 */
public interface InstanceMetricsService {

    /**
     * Retrieves metrics for a specific instance.
     *
     * @param instanceId The instance ID
     * @return A list of metrics records for the instance
     */
    @Nonnull
    List<InstanceMetrics> getMetricsByInstanceId(@Nonnull String instanceId);

    /**
     * Records new metrics for an instance.
     *
     * @param instanceId The instance ID
     * @param cpuUsage Current CPU usage (0.0 to 1.0)
     * @param memoryUsage Current memory usage (0.0 to 1.0)
     * @param diskUsage Current disk usage (0.0 to 1.0)
     * @param timeUsage Current time usage in seconds
     * @return The recorded metrics
     */
    @Nonnull
    InstanceMetrics recordMetrics(
            @Nonnull String instanceId,
            double cpuUsage,
            double memoryUsage,
            double diskUsage,
            double timeUsage);
}