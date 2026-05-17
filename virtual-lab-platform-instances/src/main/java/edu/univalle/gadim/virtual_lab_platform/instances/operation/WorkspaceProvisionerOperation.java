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

@Service
@ParametersAreNonnullByDefault
public class WorkspaceProvisionerOperation implements WorkspaceProvisionerService {

  private final DockerClient dockerClient;

  public WorkspaceProvisionerOperation(DockerClient dockerClient) {
    this.dockerClient = dockerClient;
  }

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

    CreateContainerResponse container =
        dockerClient
            .createContainerCmd("lab-kicad:latest")
            .withName("workspace-" + userId)
            .withHostConfig(hostConfig)
            .withExposedPorts(ExposedPort.tcp(8080))
            .exec();

    dockerClient.startContainerCmd(container.getId()).exec();

    return container.getId();
  }

  @Override
  public void stopWorkSpace(String containerId) {
    dockerClient.stopContainerCmd(containerId).exec();
  }
}
