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
   * @param userId the user ID to associate with the workspace
   * @param isPersistent whether to mount a persistent volume for user data
   * @return the Docker container ID
   */
  @Nonnull
  String createWorkspace(String userId, boolean isPersistent);

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
}
