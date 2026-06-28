package edu.univalle.gadim.virtual_lab_platform.instances.web.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceService;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceStatus;
import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceJpa;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.CreateInstanceRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
@DisplayName("InstancesSpringWsOps")
@ExtendWith(MockitoExtension.class)
class InstancesSpringWsOpsUnTest {

  private static final String USER_ID = "ana.martinez@correounivalle.edu.co";
  private static final String INSTANCE_ID = "inst-001";

  @Mock private InstanceService instanceService;

  private InstancesSpringWsOps wsOps;

  @BeforeEach
  void setUp() {
    authenticateAs(USER_ID);
    wsOps = new InstancesSpringWsOps(instanceService);
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  private static void authenticateAs(String userId) {
    final var auth =
        new UsernamePasswordAuthenticationToken(
            userId, "n/a", AuthorityUtils.createAuthorityList("ROLE_USER"));
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  private InstanceJpa buildInstance() {
    final var now = LocalDateTime.now();
    return InstanceJpa.builder()
        .id(INSTANCE_ID)
        .name("lab-instance")
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
        .status(InstanceStatus.RUNNING)
        .build();
  }

  @Nested
  @DisplayName("createInstance")
  class CreateInstance {

    @Test
    @DisplayName("should delegate to service using authenticated user id")
    void shouldDelegate() {
      // Given
      final var request =
          new CreateInstanceRequest(
              "lab",
              null,
              "lab-kicad",
              "1.0",
              "registry.univalle.edu",
              4,
              8192,
              20480,
              true,
              8080);
      final var saved = buildInstance();
      when(instanceService.createInstance(
              USER_ID,
              "lab",
              Optional.empty(),
              "lab-kicad",
              "1.0",
              "registry.univalle.edu",
              4,
              8192,
              20480,
              true,
              8080))
          .thenReturn(saved);

      // When
      final var response = wsOps.createInstance(request);

      // Then
      assertThat(response.id()).isEqualTo(INSTANCE_ID);
      assertThat(response.status()).isEqualTo(InstanceStatus.RUNNING);
    }
  }

  @Nested
  @DisplayName("getInstanceById")
  class GetById {

    @Test
    @DisplayName("should return mapped response when instance exists")
    void shouldReturnMappedResponse() {
      // Given
      when(instanceService.getInstanceById(INSTANCE_ID)).thenReturn(Optional.of(buildInstance()));

      // When
      final var response = wsOps.getInstanceById(INSTANCE_ID);

      // Then
      assertThat(response.id()).isEqualTo(INSTANCE_ID);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when instance missing")
    void shouldThrowWhenMissing() {
      // Given
      when(instanceService.getInstanceById(INSTANCE_ID)).thenReturn(Optional.empty());

      // When / Then
      assertThatThrownBy(() -> wsOps.getInstanceById(INSTANCE_ID))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining(INSTANCE_ID);
    }
  }

  @Nested
  @DisplayName("getInstancesByUser")
  class ListByUser {

    @Test
    @DisplayName("should return mapped list for the authenticated user")
    void shouldReturnList() {
      // Given
      final var instance = buildInstance();
      when(instanceService.getInstancesByUserId(USER_ID)).thenReturn(List.of(instance));

      // When
      final var response = wsOps.getInstancesByUser();

      // Then
      assertThat(response)
          .hasSize(1)
          .first()
          .returns(INSTANCE_ID, edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceResponse::id);
    }
  }

  @Nested
  @DisplayName("startInstance and stopInstance")
  class Lifecycle {

    @Test
    @DisplayName("should map start result")
    void shouldStart() {
      // Given
      when(instanceService.startInstance(INSTANCE_ID)).thenReturn(buildInstance());

      // When
      final var response = wsOps.startInstance(INSTANCE_ID);

      // Then
      assertThat(response.id()).isEqualTo(INSTANCE_ID);
    }

    @Test
    @DisplayName("should map stop result")
    void shouldStop() {
      // Given
      when(instanceService.stopInstance(INSTANCE_ID)).thenReturn(buildInstance());

      // When
      final var response = wsOps.stopInstance(INSTANCE_ID);

      // Then
      assertThat(response.id()).isEqualTo(INSTANCE_ID);
    }
  }

  @Nested
  @DisplayName("deleteInstance")
  class Delete {

    @Test
    @DisplayName("should delegate to service")
    void shouldDelegate() {
      // When
      wsOps.deleteInstance(INSTANCE_ID);

      // Then — the method is void; assert the call is forwarded to the service
      org.mockito.Mockito.verify(instanceService).deleteInstance(INSTANCE_ID);
    }
  }
}
