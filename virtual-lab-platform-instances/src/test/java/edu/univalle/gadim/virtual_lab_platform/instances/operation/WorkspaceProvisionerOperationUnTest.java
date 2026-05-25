package edu.univalle.gadim.virtual_lab_platform.instances.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@NullMarked
@DisplayName("WorkspaceProvisionerOperation")
class WorkspaceProvisionerOperationUnTest {

  private static final String USER_ID = "user-001";
  private static final String CONTAINER_ID = "container-abc123";

  @Mock private DockerClient dockerClient;

  @Mock private CreateContainerCmd createContainerCmd;

  @Mock private CreateContainerResponse createContainerResponse;

  @Mock private StartContainerCmd startContainerCmd;

  @Mock private StopContainerCmd stopContainerCmd;

  private WorkspaceProvisionerOperation provisioner;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    provisioner = new WorkspaceProvisionerOperation(dockerClient);
  }

  @Nested
  @DisplayName("createWorkspace")
  class CreateWorkspace {

    @Test
    @DisplayName("should create and start container without persistent volume")
    void shouldCreateAndStartContainerWithoutPersistentVolume() {
      // Given
      when(dockerClient.createContainerCmd(anyString())).thenReturn(createContainerCmd);
      when(createContainerCmd.withName(anyString())).thenReturn(createContainerCmd);
      when(createContainerCmd.withHostConfig(any(HostConfig.class))).thenReturn(createContainerCmd);
      when(createContainerCmd.withExposedPorts(any(ExposedPort.class)))
          .thenReturn(createContainerCmd);
      when(createContainerCmd.exec()).thenReturn(createContainerResponse);
      when(createContainerResponse.getId()).thenReturn(CONTAINER_ID);
      when(dockerClient.startContainerCmd(anyString())).thenReturn(startContainerCmd);

      // When
      final var result = provisioner.createWorkspace(USER_ID, false);

      // Then
      assertThat(result).isEqualTo(CONTAINER_ID);
      verify(dockerClient).createContainerCmd("lab-kicad:latest");
      verify(createContainerCmd).withName("workspace-" + USER_ID);
      verify(createContainerCmd).withHostConfig(any(HostConfig.class));
      verify(createContainerCmd).withExposedPorts(any(ExposedPort.class));
      verify(createContainerCmd).exec();
      verify(dockerClient).startContainerCmd(CONTAINER_ID);
      verify(startContainerCmd).exec();
    }

    @Test
    @DisplayName("should create container with persistent volume when isPersistent is true")
    void shouldCreateContainerWithPersistentVolume() {
      // Given
      when(dockerClient.createContainerCmd(anyString())).thenReturn(createContainerCmd);
      when(createContainerCmd.withName(anyString())).thenReturn(createContainerCmd);
      when(createContainerCmd.withHostConfig(any(HostConfig.class))).thenReturn(createContainerCmd);
      when(createContainerCmd.withExposedPorts(any(ExposedPort.class)))
          .thenReturn(createContainerCmd);
      when(createContainerCmd.exec()).thenReturn(createContainerResponse);
      when(createContainerResponse.getId()).thenReturn(CONTAINER_ID);
      when(dockerClient.startContainerCmd(anyString())).thenReturn(startContainerCmd);

      // When
      final var result = provisioner.createWorkspace(USER_ID, true);

      // Then
      assertThat(result).isEqualTo(CONTAINER_ID);
      verify(dockerClient).createContainerCmd("lab-kicad:latest");
      verify(createContainerCmd).withHostConfig(any(HostConfig.class));
    }
  }

  @Nested
  @DisplayName("stopWorkSpace")
  class StopWorkSpace {

    @Test
    @DisplayName("should invoke stop container command")
    void shouldInvokeStopContainerCommand() {
      // Given
      when(dockerClient.stopContainerCmd(anyString())).thenReturn(stopContainerCmd);

      // When
      provisioner.stopWorkSpace(CONTAINER_ID);

      // Then
      verify(dockerClient).stopContainerCmd(CONTAINER_ID);
      verify(stopContainerCmd).exec();
    }
  }
}
