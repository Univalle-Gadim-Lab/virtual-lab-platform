package edu.univalle.gadim.virtual_lab_platform.boot.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@NullMarked
@DisplayName("SecurityConfig")
class SecurityConfigUnTest {

  @Test
  @DisplayName("corsConfigurationSource should register a CORS policy for the dev UI origins")
  void shouldExposeCorsForDevOrigins() {
    // Given
    final var config = new SecurityConfig(null, null, null);

    // When
    final var source = (UrlBasedCorsConfigurationSource) config.corsConfigurationSource();

    // Then
    final var corsConfig = source.getCorsConfigurations();
    assertThat(corsConfig)
        .isNotNull()
        .containsKey("/**");

    final var policy = corsConfig.get("/**");
    assertThat(policy)
        .isNotNull()
        .satisfies(this::assertDevOrigins);
  }

  private void assertDevOrigins(CorsConfiguration policy) {
    assertThat(policy.getAllowedOrigins())
        .containsExactly(
            "http://localhost:4200",
            "http://localhost:3000",
            "http://localhost:3001",
            "http://localhost:3002",
            "http://localhost:5173",
            "http://localhost:6901");
  }

  @Test
  @DisplayName("corsConfigurationSource should allow REST methods plus OPTIONS preflight")
  void shouldAllowRestMethods() {
    // Given
    final var config = new SecurityConfig(null, null, null);

    // When
    final var source = (UrlBasedCorsConfigurationSource) config.corsConfigurationSource();
    final var policy = source.getCorsConfigurations().get("/**");

    // Then
    assertThat(policy).isNotNull();
    final var expectedMethods =
        List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
    assertThat(policy.getAllowedMethods()).containsExactlyElementsOf(expectedMethods);
  }

  @Test
  @DisplayName("corsConfigurationSource should permit Authorization header (Bearer tokens)")
  void shouldAllowAuthorizationHeader() {
    // Given
    final var config = new SecurityConfig(null, null, null);

    // When
    final var source = (UrlBasedCorsConfigurationSource) config.corsConfigurationSource();
    final var policy = source.getCorsConfigurations().get("/**");

    // Then
    assertThat(policy).isNotNull();
    assertThat(policy.getAllowedHeaders())
        .contains(
            "Authorization",
            "Content-Type",
            "Accept",
            "X-Requested-With",
            "Upgrade",
            "Connection",
            "Sec-WebSocket-Key",
            "Sec-WebSocket-Version",
            "Sec-WebSocket-Extensions",
            "Sec-WebSocket-Protocol");
  }

  @Test
  @DisplayName("corsConfigurationSource should allow credentials and a 1-hour preflight cache")
  void shouldAllowCredentialsAndCachePreflight() {
    // Given
    final var config = new SecurityConfig(null, null, null);

    // When
    final var source = (UrlBasedCorsConfigurationSource) config.corsConfigurationSource();
    final var policy = source.getCorsConfigurations().get("/**");

    // Then
    assertThat(policy).isNotNull();
    assertThat(policy.getAllowCredentials()).isTrue();
    assertThat(policy.getMaxAge()).isEqualTo(3600L);
  }
}
