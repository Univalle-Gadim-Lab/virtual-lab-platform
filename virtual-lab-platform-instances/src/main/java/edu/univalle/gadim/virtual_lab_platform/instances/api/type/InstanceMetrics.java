package edu.univalle.gadim.virtual_lab_platform.instances.api.type;

import javax.annotation.Nonnull;

/**
 * Represents the metrics of an instance in the virtual lab platform.
 *
 * <p>This interface defines the current usage metrics for CPU, memory, disk, and time
 * for a specific instance.
 */
public interface InstanceMetrics {

    /**
     * Returns the unique identifier of the metrics record.
     *
     * @return The metrics ID
     */
    @Nonnull
    String id();

    /**
     * Returns the ID of the instance these metrics belong to.
     *
     * @return The instance ID
     */
    @Nonnull
    String instanceId();

    /**
     * Returns the current CPU usage percentage.
     *
     * @return The CPU usage as a double (0.0 to 1.0)
     */
    double currentCpuUsage();

    /**
     * Returns the current memory usage percentage.
     *
     * @return The memory usage as a double (0.0 to 1.0)
     */
    double currentMemoryUsage();

    /**
     * Returns the current disk usage percentage.
     *
     * @return The disk usage as a double (0.0 to 1.0)
     */
    double currentDiskUsage();

    /**
     * Returns the current time usage in seconds.
     *
     * @return The time usage in seconds
     */
    double currentTimeUsage();
}