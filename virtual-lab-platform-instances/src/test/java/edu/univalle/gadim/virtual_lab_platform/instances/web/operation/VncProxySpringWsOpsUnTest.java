package edu.univalle.gadim.virtual_lab_platform.instances.web.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceService;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceStatus;
import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceJpa;
import java.net.URI;
import java.time.LocalDateTime;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@NullMarked
@DisplayName("VncProxySpringWsOps")
@ExtendWith(MockitoExtension.class)
class VncProxySpringWsOpsUnTest {

  private static final String USER_ID = "ana.martinez@correounivalle.edu.co";
  private static final String INSTANCE_ID = "inst-001";

  @Mock private InstanceService instanceService;
  @Mock private RestTemplate restTemplate;

  private VncProxySpringWsOps wsOps;

  @BeforeEach
  void setUp() {
    final var auth =
        new UsernamePasswordAuthenticationToken(
            USER_ID, "n/a", AuthorityUtils.createAuthorityList("ROLE_USER"));
    SecurityContextHolder.getContext().setAuthentication(auth);
    wsOps = new VncProxySpringWsOps(instanceService, restTemplate);
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  private InstanceJpa buildRunningInstance() {
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
        .status(InstanceStatus.RUNNING)
        .build();
  }

  @Nested
  @DisplayName("proxyVncRequest")
  class Proxy {

    @Test
    @DisplayName("should return proxied status, content type, and body for a running instance")
    void shouldReturnProxiedResponse() {
      // Given
      when(instanceService.checkOwnership(INSTANCE_ID, USER_ID)).thenReturn(true);
      when(instanceService.getInstanceById(INSTANCE_ID))
          .thenReturn(Optional.of(buildRunningInstance()));
      final var body = "<html>kasmvnc</html>".getBytes();
      final var responseEntity = ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(body);
      when(restTemplate.exchange(
              any(URI.class),
              eq(HttpMethod.GET),
              any(HttpEntity.class),
              eq(byte[].class)))
          .thenReturn(responseEntity);

      // When
      final var response = wsOps.proxyVncRequest(INSTANCE_ID, "/index.html");

      // Then
      assertThat(response.statusCode()).isEqualTo(200);
      assertThat(response.contentType()).isEqualTo(MediaType.TEXT_HTML);
      assertThat(response.body()).isEqualTo(body);
    }

    @Test
    @DisplayName("should return 403 when caller is not the owner")
    void shouldReturn403WhenForbidden() {
      // Given
      when(instanceService.checkOwnership(INSTANCE_ID, USER_ID)).thenReturn(false);

      // When
      final var response = wsOps.proxyVncRequest(INSTANCE_ID, "/");

      // Then
      assertThat(response.statusCode()).isEqualTo(403);
    }

    @Test
    @DisplayName("should return 409 when the instance is not in RUNNING status")
    void shouldReturn409WhenNotRunning() {
      // Given
      final var instance = buildRunningInstance();
      instance.setStatus(InstanceStatus.STOPPED);
      when(instanceService.checkOwnership(INSTANCE_ID, USER_ID)).thenReturn(true);
      when(instanceService.getInstanceById(INSTANCE_ID)).thenReturn(Optional.of(instance));

      // When
      final var response = wsOps.proxyVncRequest(INSTANCE_ID, "/");

      // Then
      assertThat(response.statusCode()).isEqualTo(409);
    }

    @Test
    @DisplayName("should return 502 when restTemplate.exchange throws")
    void shouldReturn502OnUpstreamError() {
      // Given
      when(instanceService.checkOwnership(INSTANCE_ID, USER_ID)).thenReturn(true);
      when(instanceService.getInstanceById(INSTANCE_ID))
          .thenReturn(Optional.of(buildRunningInstance()));
      when(restTemplate.exchange(
              any(URI.class),
              eq(HttpMethod.GET),
              any(HttpEntity.class),
              eq(byte[].class)))
          .thenThrow(new ResourceAccessException("connection refused"));

      // When
      final var response = wsOps.proxyVncRequest(INSTANCE_ID, "/");

      // Then
      assertThat(response.statusCode()).isEqualTo(502);
    }
  }
}
