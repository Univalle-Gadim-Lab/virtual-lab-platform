package edu.univalle.gadim.virtual_lab_platform.instances.api.service;

import javax.annotation.Nonnull;

/**
 * Service contract for Docker container workspace provisioning.
 *
 * <p>Provides methods for creating and stopping Docker containers that serve
 * as virtual lab workspaces. Implementations bridge the domain layer to the
 * Docker daemon.
 *
 * @see edu.univalle.gadim.virtual_lab_platform.instances.operation.WorkspaceProvisionerOperation
 */
public interface WorkspaceProvisionerService {

  /**
   * Creates and starts a new Docker container workspace for the specified user.
   *
   * <p>Uses hardcoded default resource limits and generates a random VNC password. Prefer
   * {@link #createWorkspace(String, boolean, String, String, int, int, int, boolean, int, String)}
   * for dynamic resource configuration.
   *
   * @param userId the user ID to associate with the workspace
   * @param isPersistent whether to mount a persistent volume for user data
   * @return the Docker container ID
   */
  @Nonnull
  String createWorkspace(String userId, boolean isPersistent);

  /**
   * Creates and starts a new Docker container workspace with explicit resource limits.
   *
   * @param userId the user ID to associate with the workspace
   * @param isPersistent whether to mount a persistent volume for user data
   * @param imageName the Docker image name (without registry/tag)
   * @param imageVersion the Docker image version tag
   * @param cpuCores the number of CPU cores to allocate
   * @param memoryMb the amount of memory in megabytes to allocate
   * @param storageMb the amount of disk storage in megabytes to allocate
   * @param gpuEnabled whether GPU acceleration is enabled
   * @param exposedPort the port to expose on the container
   * @param vncPassword the VNC password for remote desktop access
   * @return the Docker container ID
   */
  @Nonnull
  String createWorkspace(
      String userId,
      boolean isPersistent,
      String imageName,
      String imageVersion,
      int cpuCores,
      int memoryMb,
      int storageMb,
      boolean gpuEnabled,
      int exposedPort,
      String vncPassword);

  /**
   * Stops the Docker container workspace identified by the given container ID.
   *
   * @param containerId the Docker container ID to stop
   */
  void stopWorkSpace(String containerId);

  /**
   * Starts an existing Docker container workspace identified by the given container ID.
   *
   * @param containerId the Docker container ID to start
   */
  void startWorkspace(String containerId);

  /**
   * Resolves the internal bridge network IP address of a running container.
   *
   * @param containerId the Docker container ID
   * @return the container's internal IP address
   */
  @Nonnull
  String getContainerIp(String containerId);

  /**
   * Resolves the host port assigned to the VNC port (6901) of a running container.
   *
   * <p>When running on Docker Desktop (macOS/Windows), the container's internal bridge
   * IP is not routable from the host. This method returns the host-side port that was
   * dynamically bound to the container's VNC port at creation time.
   *
   * @param containerId the Docker container ID
   * @return the host port number assigned to the container's VNC port
   */
  int getHostVncPort(String containerId);
}
