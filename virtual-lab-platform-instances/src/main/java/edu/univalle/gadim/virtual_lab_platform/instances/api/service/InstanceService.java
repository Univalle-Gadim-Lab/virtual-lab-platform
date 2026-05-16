package edu.univalle.gadim.virtual_lab_platform.instances.api.service;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.Instance;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;

/**
 * Service interface for managing virtual lab instances.
 *
 * <p>This interface defines the operations available for creating, managing, and
 * controlling the lifecycle of virtual lab instances.
 */
public interface InstanceService {

    /**
     * Creates a new instance with the specified parameters.
     *
     * @param userId The ID of the user creating the instance
     * @param name The name of the instance
     * @param description Optional description of the instance
     * @param imageName The container image name
     * @param imageVersion The container image version
     * @param imageRegistry The container image registry
     * @param cpuCores Number of CPU cores to allocate
     * @param memoryMb Memory allocation in MB
     * @param storageMb Storage allocation in MB
     * @param gpuEnabled Whether GPU acceleration is enabled
     * @param exposedPort The port to expose
     * @return The created instance
     */
    @Nonnull
    Instance createInstance(
            @Nonnull String userId,
            @Nonnull String name,
            @Nonnull Optional<String> description,
            @Nonnull String imageName,
            @Nonnull String imageVersion,
            @Nonnull String imageRegistry,
            int cpuCores,
            int memoryMb,
            int storageMb,
            boolean gpuEnabled,
            int exposedPort);

    /**
     * Retrieves an instance by its ID.
     *
     * @param instanceId The instance ID
     * @return An Optional containing the instance if found
     */
    @Nonnull
    Optional<Instance> getInstanceById(@Nonnull String instanceId);

    /**
     * Retrieves all instances for a specific user.
     *
     * @param userId The user ID
     * @return A list of instances belonging to the user
     */
    @Nonnull
    List<Instance> getInstancesByUserId(@Nonnull String userId);

    /**
     * Starts an instance.
     *
     * @param instanceId The instance ID to start
     * @return The updated instance
     */
    @Nonnull
    Instance startInstance(@Nonnull String instanceId);

    /**
     * Stops an instance.
     *
     * @param instanceId The instance ID to stop
     * @return The updated instance
     */
    @Nonnull
    Instance stopInstance(@Nonnull String instanceId);

    /**
     * Deletes an instance.
     *
     * @param instanceId The instance ID to delete
     */
    void deleteInstance(@Nonnull String instanceId);
}