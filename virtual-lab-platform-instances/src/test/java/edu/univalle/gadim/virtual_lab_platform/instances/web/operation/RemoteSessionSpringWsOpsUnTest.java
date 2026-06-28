package edu.univalle.gadim.virtual_lab_platform.instances.web.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceService;
import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceJpa;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceStatus;
import java.time.LocalDateTime;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

@NullMarked
@DisplayName("RemoteSessionSpringWsOps")
@ExtendWith(MockitoExtension.class)
class RemoteSessionSpringWsOpsUnTest {

  private static final String USER_ID = "ana.martinez@correounivalle.edu.co";
  private static final String INSTANCE_ID = "inst-001";

  @Mock private InstanceService instanceService;

  private RemoteSessionSpringWsOps wsOps;

  @BeforeEach
  void setUp() {
    final var auth =
        new UsernamePasswordAuthenticationToken(
            USER_ID, "n/a", AuthorityUtils.createAuthorityList("ROLE_USER"));
    SecurityContextHolder.getContext().setAuthentication(auth);
    wsOps = new RemoteSessionSpringWsOps(instanceService);
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  private InstanceJpa buildInstance(InstanceStatus status) {
    final var now = LocalDateTime.now();
    return InstanceJpa.builder()
        .id(INSTANCE_ID)
        .name("lab")
        .externalIp("container-001")
        .imageName("lab-kicad")
        .imageVersion("1.0")
        .imageRegistry("registry.univalle.edu")
        .cpuCores(4)
        .memoryMb(8192)
        .storageMb(20480)
        .gpuEnabled(true)
        .exposedPort(8080)
        .vncPort(6901)
        .vncEnabled(true)
        .vncPassword("VncSecret123")
        .internalIp("172.17.0.2")
        .createdAt(now)
        .expiresAt(now.plusDays(7))
        .startedAt(now)
        .status(status)
        .build();
  }

  @Nested
  @DisplayName("getRemoteSession")
  class GetSession {

    @Test
    @DisplayName("should map a running instance to ACTIVE with vncUrl")
    void shouldMapRunningInstanceToActive() {
      // Given
      when(instanceService.getRemoteSessionInfo(INSTANCE_ID, USER_ID))
          .thenReturn(buildInstance(InstanceStatus.RUNNING));

      // When
      final var response = wsOps.getRemoteSession(INSTANCE_ID);

      // Then
      assertThat(response.instanceId()).isEqualTo(INSTANCE_ID);
      assertThat(response.status()).isEqualTo("ACTIVE");
      assertThat(response.vncEnabled()).isTrue();
      assertThat(response.vncUrl()).isNotNull().contains("password=VncSecret123");
    }

    @Test
    @DisplayName("should map a stopped instance to DISCONNECTED with null vncUrl")
    void shouldMapStoppedInstanceToDisconnected() {
      // Given
      when(instanceService.getRemoteSessionInfo(INSTANCE_ID, USER_ID))
          .thenReturn(buildInstance(InstanceStatus.STOPPED));

      // When
      final var response = wsOps.getRemoteSession(INSTANCE_ID);

      // Then
      assertThat(response.status()).isEqualTo("DISCONNECTED");
      assertThat(response.vncUrl()).isNull();
    }
  }

  @Nested
  @DisplayName("terminateRemoteSession")
  class Terminate {

    @Test
    @DisplayName("should delegate to instanceService.stopInstance")
    void shouldDelegate() {
      // When
      wsOps.terminateRemoteSession(INSTANCE_ID);

      // Then
      verify(instanceService).stopInstance(INSTANCE_ID);
    }
  }

  @Nested
  @DisplayName("getRemoteSessionStatus")
  class Status {

    @Test
    @DisplayName("should return HEALTHY when vnc is reachable")
    void shouldReturnHealthy() {
      // Given
      when(instanceService.checkVncHealth(INSTANCE_ID, USER_ID)).thenReturn(true);

      // When
      final var response = wsOps.getRemoteSessionStatus(INSTANCE_ID);

      // Then
      assertThat(response.status()).isEqualTo("HEALTHY");
      assertThat(response.vncReachable()).isTrue();
    }

    @Test
    @DisplayName("should return UNHEALTHY when vnc is not reachable")
    void shouldReturnUnhealthy() {
      // Given
      when(instanceService.checkVncHealth(INSTANCE_ID, USER_ID)).thenReturn(false);

      // When
      final var response = wsOps.getRemoteSessionStatus(INSTANCE_ID);

      // Then
      assertThat(response.status()).isEqualTo("UNHEALTHY");
      assertThat(response.vncReachable()).isFalse();
    }
  }
}
