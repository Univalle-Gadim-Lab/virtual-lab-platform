package edu.univalle.gadim.virtual_lab_platform.instances.operation;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Ports;
import edu.univalle.gadim.virtual_lab_platform.instances.api.service.WorkspaceProvisionerService;
import java.io.IOException;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger logger = LoggerFactory.getLogger(WorkspaceProvisionerOperation.class);
  private static final int CPU_PERIOD = 100_000;
  private static final long BYTES_PER_MB = 1024L * 1024L;
  private static final int DEFAULT_CPU_CORES = 2;
  private static final int DEFAULT_MEMORY_MB = 4096;
  private static final int DEFAULT_STORAGE_MB = 10240;
  private static final int DEFAULT_EXPOSED_PORT = 8080;
  private static final int DEFAULT_VNC_PORT = 6901;
  private static final long SHM_SIZE_BYTES = 2L * 1024 * 1024 * 1024;
  private static final String DEFAULT_IMAGE_NAME = "lab-kicad";
  private static final String DEFAULT_IMAGE_VERSION = "latest";
  private static final String VNC_PASSWORD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final int VNC_PASSWORD_LENGTH = 12;
  private static final String DOCKER_BUILD_CONTEXT = "virtual-lab-platform-instances/docker/kicad";

  private final DockerClient dockerClient;
  private final SecureRandom secureRandom = new SecureRandom();

  public WorkspaceProvisionerOperation(DockerClient dockerClient) {
    this.dockerClient = dockerClient;
  }

  @Override
  @Nonnull
  public String createWorkspace(String userId, boolean isPersistent) {
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
        generateVncPassword());
  }

  @Override
  @Nonnull
  public String createWorkspace(
      String userId,
      boolean isPersistent,
      String imageName,
      String imageVersion,
      int cpuCores,
      int memoryMb,
      int storageMb,
      boolean gpuEnabled,
      int exposedPort,
      String vncPassword) {
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
        DEFAULT_VNC_PORT,
        vncPassword);
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
      int vncPort,
      String vncPassword) {

    final var imageReference = imageName + ":" + imageVersion;
    final var cpuQuota = (long) cpuCores * CPU_PERIOD;
    final var ramLimitBytes = (long) memoryMb * BYTES_PER_MB;

    var vncExposedPort = ExposedPort.tcp(vncPort);
    var portBindings = new Ports();
    portBindings.bind(vncExposedPort, Ports.Binding.bindPort(0));

    HostConfig hostConfig =
        HostConfig.newHostConfig()
            .withMemory(ramLimitBytes)
            .withMemorySwap(ramLimitBytes)
            .withCpuQuota(cpuQuota)
            .withCpuPeriod((long) CPU_PERIOD)
            .withShmSize(SHM_SIZE_BYTES)
            .withSecurityOpts(List.of("no-new-privileges:true"))
            .withPortBindings(portBindings);

    if (isPersistent) {
      var volumeName = "vol_" + sanitizeUserId(userId);
      hostConfig.withBinds(
          com.github.dockerjava.api.model.Bind.parse(volumeName + ":/home/labuser/projects"));
    }

    boolean imageExistsLocally = false;
    try {
      dockerClient.inspectImageCmd(imageReference).exec();
      imageExistsLocally = true;
    } catch (NotFoundException ignored) {
    }

    if (!imageExistsLocally) {
      buildImageLocally(imageName, imageVersion);
    }

    CreateContainerResponse container;
    try (var createCmd = dockerClient.createContainerCmd(imageReference)) {
      container =
          createCmd
              .withHostConfig(hostConfig)
              .withExposedPorts(ExposedPort.tcp(exposedPort), ExposedPort.tcp(vncPort))
              .withEnv("KASMVNC_PASSWORD=" + vncPassword)
              .exec();
    }

    dockerClient.startContainerCmd(container.getId()).exec();

    return container.getId();
  }

  private String generateVncPassword() {
    var sb = new StringBuilder(VNC_PASSWORD_LENGTH);
    for (int i = 0; i < VNC_PASSWORD_LENGTH; i++) {
      sb.append(VNC_PASSWORD_CHARS.charAt(secureRandom.nextInt(VNC_PASSWORD_CHARS.length())));
    }
    return sb.toString();
  }

  private String sanitizeUserId(String userId) {
    var localPart = userId.contains("@") ? userId.substring(0, userId.indexOf("@")) : userId;
    return localPart.contains(".") ? localPart.substring(0, localPart.indexOf(".")) : localPart;
  }

  private void buildImageLocally(String imageName, String imageVersion) {
    var buildContext = Path.of(DOCKER_BUILD_CONTEXT).toAbsolutePath();
    var imageTag = imageName + ":" + imageVersion;

    logger.info("Image {} not found locally. Building from {}...", imageTag, buildContext);

    try {
      var processBuilder =
          new ProcessBuilder("docker", "build", "-t", imageTag, ".")
              .directory(buildContext.toFile())
              .redirectErrorStream(true);

      var process = processBuilder.start();
      var output = new String(process.getInputStream().readAllBytes());
      var exitCode = process.waitFor();

      if (exitCode != 0) {
        throw new RuntimeException(
            "Docker build failed for " + imageTag + " (exit code " + exitCode + "):\n" + output);
      }

      logger.info("Image {} built successfully.", imageTag);
    } catch (IOException e) {
      throw new RuntimeException("Failed to execute docker build for " + imageTag, e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Docker build was interrupted for " + imageTag, e);
    }
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
  public String getContainerIp(String containerId) {
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

  @Override
  public int getHostVncPort(String containerId) {
    for (int attempt = 0; attempt < 10; attempt++) {
      InspectContainerResponse inspection = dockerClient.inspectContainerCmd(containerId).exec();
      var networkSettings = inspection.getNetworkSettings();
      if (networkSettings != null && networkSettings.getPorts() != null) {
        var bindings = networkSettings.getPorts().getBindings();
        if (bindings != null) {
          var vncBindings = bindings.get(ExposedPort.tcp(DEFAULT_VNC_PORT));
          if (vncBindings != null && vncBindings.length > 0) {
            var hostPort = vncBindings[0].getHostPortSpec();
            if (hostPort != null && !hostPort.equals("0")) {
              return Integer.parseInt(hostPort);
            }
          }
        }
      }
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
    logger.warn("Could not determine host VNC port for container {}, using default {}", containerId, DEFAULT_VNC_PORT);
    return DEFAULT_VNC_PORT;
  }
}
