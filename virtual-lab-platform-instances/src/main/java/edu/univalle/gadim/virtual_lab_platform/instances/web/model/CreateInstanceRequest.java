package edu.univalle.gadim.virtual_lab_platform.instances.web.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Request DTO for creating a new instance.
 *
 * <p>This record contains the necessary information to create a virtual lab instance,
 * including configuration parameters for CPU, memory, storage, and GPU.
 */
@ParametersAreNonnullByDefault
public record CreateInstanceRequest(
    String name,
    @Nullable String description,
    String imageName,
    String imageVersion,
    String imageRegistry,
    Integer cpuCores,
    Integer memoryMb,
    Integer storageMb,
    Boolean gpuEnabled,
    Integer exposedPort) {
}