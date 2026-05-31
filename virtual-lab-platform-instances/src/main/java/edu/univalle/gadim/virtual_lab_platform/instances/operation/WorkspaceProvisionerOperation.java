package edu.univalle.gadim.virtual_lab_platform.instances.operation;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import edu.univalle.gadim.virtual_lab_platform.instances.api.service.WorkspaceProvisionerService;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
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
 * @see WorkspaceProvisionerService
 */
@Service
@ParametersAreNonnullByDefault
public class WorkspaceProvisionerOperation implements WorkspaceProvisionerService {

  private static final int CPU_PERIOD = 100_000;
  private static final long BYTES_PER_MB = 1024L * 1024L;
  private static final int MB_PER_GB = 1024;
  private static final int DEFAULT_CPU_CORES = 2;
  private static final int DEFAULT_MEMORY_MB = 4096;
  private static final int DEFAULT_STORAGE_MB = 10240;
  private static final String DEFAULT_IMAGE_NAME = "lab-kicad";
  private static final String DEFAULT_IMAGE_VERSION = "latest";

  private final DockerClient dockerClient;

  public WorkspaceProvisionerOperation(DockerClient dockerClient) {
    this.dockerClient = dockerClient;
  }

  @Override
  @Nonnull
  public @NonNull String createWorkspace(String userId, boolean isPersistent) {
    return createWorkspace(
        userId,
        isPersistent,
        DEFAULT_IMAGE_NAME,
        DEFAULT_IMAGE_VERSION,
        DEFAULT_CPU_CORES,
        DEFAULT_MEMORY_MB,
        DEFAULT_STORAGE_MB,
        false,
        8080);
  }

  @Override
  @Nonnull
  public @NonNull String createWorkspace(
      String userId,
      boolean isPersistent,
      String imageName,
      String imageVersion,
      int cpuCores,
      int memoryMb,
      int storageMb,
      boolean gpuEnabled,
      int exposedPort) {

    final var imageReference = imageName + ":" + imageVersion;
    final var cpuQuota = (long) cpuCores * CPU_PERIOD;
    final var ramLimitBytes = (long) memoryMb * BYTES_PER_MB;
    final var diskSizeGb = Math.max(1, storageMb / MB_PER_GB);

    HostConfig hostConfig =
        HostConfig.newHostConfig()
            .withMemory(ramLimitBytes)
            .withMemorySwap(ramLimitBytes)
            .withCpuQuota(cpuQuota)
            .withCpuPeriod((long) CPU_PERIOD)
            .withSecurityOpts(List.of("no-new-privileges:true"));

    hostConfig.withStorageOpt(Map.of("size", diskSizeGb + "G"));

    if (isPersistent) {
      hostConfig.withBinds(
          com.github.dockerjava.api.model.Bind.parse("vol_" + userId + ":/home/labuser/projects"));
    }

    CreateContainerResponse container;
    try (var createCmd = dockerClient.createContainerCmd(imageReference)) {
      container =
          createCmd
              .withName("workspace-" + userId)
              .withHostConfig(hostConfig)
              .withExposedPorts(ExposedPort.tcp(exposedPort))
              .exec();
    }

    dockerClient.startContainerCmd(container.getId()).exec();

    return container.getId();
  }

  @Override
  public void stopWorkSpace(String containerId) {
    dockerClient.stopContainerCmd(containerId).exec();
  }

  @Override
  public void startWorkspace(String containerId) {
    dockerClient.startContainerCmd(containerId).exec();
  }
}
