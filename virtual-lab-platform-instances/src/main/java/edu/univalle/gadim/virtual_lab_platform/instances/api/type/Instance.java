package edu.univalle.gadim.virtual_lab_platform.instances.api.type;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents an instance in the virtual lab platform.
 *
 * <p>This interface defines the core properties and behaviors of a virtual lab instance,
 * including its configuration, lifecycle timestamps, and current status.
 */
public interface Instance {

    /**
     * Returns the unique identifier of the instance.
     *
     * @return The instance ID
     */
    @Nonnull
    String id();

    /**
     * Returns the name of the instance.
     *
     * @return The instance name
     */
    @Nonnull
    String name();

    /**
     * Returns the optional description of the instance.
     *
     * @return An Optional containing the description, or empty if none
     */
    @Nonnull
    Optional<String> description();

    /**
     * Returns the external IP address of the instance (real container ID).
     *
     * @return The external IP address
     */
    @Nonnull
    String externalIp();

    /**
     * Returns the image name used for the instance.
     *
     * @return The image name
     */
    @Nonnull
    String imageName();

    /**
     * Returns the image version used for the instance.
     *
     * @return The image version
     */
    @Nonnull
    String imageVersion();

    /**
     * Returns the image registry used for the instance.
     *
     * @return The image registry
     */
    @Nonnull
    String imageRegistry();

    /**
     * Returns the number of CPU cores allocated to the instance.
     *
     * @return The CPU core count
     */
    int cpuCores();

    /**
     * Returns the memory allocation in MB for the instance.
     *
     * @return The memory in MB
     */
    int memoryMb();

    /**
     * Returns the storage allocation in MB for the instance.
     *
     * @return The storage in MB
     */
    int storageMb();

    /**
     * Returns whether GPU is enabled for the instance.
     *
     * @return true if GPU is enabled, false otherwise
     */
    boolean gpuEnabled();

    /**
     * Returns the exposed port number for the instance.
     *
     * @return The exposed port
     */
    int exposedPort();

    /**
     * Returns the internal IP address of the instance.
     *
     * @return The internal IP address
     */
    @Nonnull
    String internalIp();

    /**
     * Returns the creation timestamp of the instance.
     *
     * @return The creation date and time
     */
    @Nonnull
    LocalDateTime createdAt();

    /**
     * Returns the expiration timestamp of the instance.
     *
     * @return The expiration date and time
     */
    @Nonnull
    LocalDateTime expiresAt();

    /**
     * Returns the start timestamp of the instance.
     *
     * @return The start date and time
     */
    @Nonnull
    LocalDateTime startedAt();

    /**
     * Returns the optional stop timestamp of the instance.
     *
     * @return An Optional containing the stop date and time, or empty if not stopped
     */
    @Nonnull
    Optional<LocalDateTime> stoppedAt();

    /**
     * Returns the optional deletion timestamp of the instance.
     *
     * @return An Optional containing the deletion date and time, or empty if not deleted
     */
    @Nonnull
    Optional<LocalDateTime> deletedAt();

    /**
     * Returns the optional last accessed timestamp of the instance.
     *
     * @return An Optional containing the last accessed date and time, or empty if not accessed
     */
    @Nonnull
    Optional<LocalDateTime> lastAccessedAt();

    /**
     * Returns the VNC port for remote desktop access.
     *
     * @return The VNC port
     */
    int vncPort();

    /**
     * Returns the current status of the instance.
     *
     * @return The instance status
     */
    @Nonnull
    InstanceStatus status();
}