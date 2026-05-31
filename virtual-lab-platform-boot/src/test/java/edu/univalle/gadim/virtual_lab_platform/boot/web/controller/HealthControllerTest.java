package edu.univalle.gadim.virtual_lab_platform.boot.web.controller;

import static org.assertj.core.api.Assertions.assertThat;

import edu.univalle.gadim.virtual_lab_platform.boot.web.model.HealthResponse;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@NullMarked
@DisplayName("HealthController")
class HealthControllerTest {

  private HealthController controller;

  @BeforeEach
  void setUp() {
    controller = new HealthController();
  }

  @Test
  @DisplayName("should return 200 with status UP")
  void shouldReturn200WithStatusUp() {
    final var result = controller.health();

    assertThat(result.getStatusCode().value()).isEqualTo(200);
    assertThat(result.getBody()).isEqualTo(new HealthResponse("UP"));
  }

  @Test
  @DisplayName("should return non-null body")
  void shouldReturnNonNullBody() {
    final var result = controller.health();

    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().status()).isEqualTo("UP");
  }
}
