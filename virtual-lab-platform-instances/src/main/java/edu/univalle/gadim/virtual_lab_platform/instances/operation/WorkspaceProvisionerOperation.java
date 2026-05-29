package edu.univalle.gadim.virtual_lab_platform.instances.operation;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import edu.univalle.gadim.virtual_lab_platform.instances.api.service.WorkspaceProvisionerService;
import java.util.List;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

/**
 * Docker adapter that implements the {@link WorkspaceProvisionerService} contract.
 *
 * <p>Bridges the domain service layer to the Docker daemon using the {@code docker-java} client
 * library. Creates containers with configurable resource limits (CPU, memory, disk) and manages
 * their lifecycle.
 *
 * <p><b>Resource Configuration:</b>
 *
 * <ul>
 *   <li>CPU: 2 cores (quota 200000/period 100000)
 *   <li>Memory: 4 GB RAM (swap disabled)
 *   <li>Disk: 10 GB storage limit
 * </ul>
 *
 * @see WorkspaceProvisionerService
 */
@Service
@ParametersAreNonnullByDefault
public class WorkspaceProvisionerOperation implements WorkspaceProvisionerService {

  private final DockerClient dockerClient;

  public WorkspaceProvisionerOperation(DockerClient dockerClient) {
    this.dockerClient = dockerClient;
  }

  /**
   * Creates and starts a new Docker container workspace with preconfigured resource limits.
   *
   * <p>The container is allocated 2 CPU cores, 4 GB of RAM (swap disabled), and 10 GB of disk
   * storage. If {@code isPersistent} is true, a named Docker volume is mounted at {@code
   * /home/labuser/projects} to retain user data across restarts.
   *
   * @param userId the user ID used to name the container and volume
   * @param isPersistent whether to mount a persistent volume for user projects
   * @return the Docker container ID of the newly created workspace
   */
  @Override
  public @NonNull String createWorkspace(String userId, boolean isPersistent) {
    // 1 Core = 100000 quota / 100000 period
    long cpuQuota = 200000L; // 2 Cores
    long ramLimitBytes = 4L * 1024 * 1024 * 1024; // 4GB RAM

    HostConfig hostConfig =
        HostConfig.newHostConfig()
            .withMemory(ramLimitBytes)
            .withMemorySwap(ramLimitBytes) // Disable swap to prevent disk IO contention
            .withCpuQuota(cpuQuota)
            .withCpuPeriod(100000L)
            .withSecurityOpts(List.of("no-new-privileges:true"));

    // Enforce disk limits (requires overlay2 + xfs/ext4 project quotas on the host)
    hostConfig.withStorageOpt(Map.of("size", "10G"));

    if (isPersistent) {
      // Mount a named volume for the user to retain their projects
      hostConfig.withBinds(
          com.github.dockerjava.api.model.Bind.parse("vol_" + userId + ":/home/labuser/projects"));
    }

    CreateContainerResponse container;
    try (var createCmd = dockerClient.createContainerCmd("lab-kicad:latest")) {
      container =
          createCmd
              .withName("workspace-" + userId)
              .withHostConfig(hostConfig)
              .withExposedPorts(ExposedPort.tcp(8080))
              .exec();
    }

    dockerClient.startContainerCmd(container.getId()).exec();

    return container.getId();
  }

  /**
   * Stops the Docker container workspace identified by the given container ID.
   *
   * @param containerId the Docker container ID to stop
   */
  @Override
  public void stopWorkSpace(String containerId) {
    dockerClient.stopContainerCmd(containerId).exec();
  }

  @Override
  public void startWorkspace(String containerId) {
    dockerClient.startContainerCmd(containerId).exec();
  }
}
