package edu.univalle.gadim.virtual_lab_platform.instances.operation;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
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
 * their lifecycle. Each container exposes an application port and a KasmVNC port (6901) for
 * browser-based remote desktop access.
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
  private static final int DEFAULT_EXPOSED_PORT = 8080;
  private static final int DEFAULT_VNC_PORT = 6901;
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
        DEFAULT_EXPOSED_PORT,
        DEFAULT_VNC_PORT);
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
    return createWorkspace(
        userId,
        isPersistent,
        imageName,
        imageVersion,
        cpuCores,
        memoryMb,
        storageMb,
        gpuEnabled,
        exposedPort,
        DEFAULT_VNC_PORT);
  }

  private String createWorkspace(
      String userId,
      boolean isPersistent,
      String imageName,
      String imageVersion,
      int cpuCores,
      int memoryMb,
      int storageMb,
      boolean gpuEnabled,
      int exposedPort,
      int vncPort) {

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
              .withHostConfig(hostConfig)
              .withExposedPorts(ExposedPort.tcp(exposedPort), ExposedPort.tcp(vncPort))
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

  @Override
  @Nonnull
  public @NonNull String getContainerIp(String containerId) {
    InspectContainerResponse inspection = dockerClient.inspectContainerCmd(containerId).exec();
    var networkSettings = inspection.getNetworkSettings();
    if (networkSettings != null && networkSettings.getNetworks() != null) {
      var bridgeNetwork = networkSettings.getNetworks().values().stream()
          .findFirst()
          .orElse(null);
      if (bridgeNetwork != null && bridgeNetwork.getIpAddress() != null) {
        return bridgeNetwork.getIpAddress();
      }
    }
    return "127.0.0.1";
  }
}
