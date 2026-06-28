package edu.univalle.gadim.virtual_lab_platform.authentication.web.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

@NullMarked
@DisplayName("JwtAccessDeniedHandler")
class JwtAccessDeniedHandlerUnTest {

  private JwtAccessDeniedHandler handler;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() {
    handler = new JwtAccessDeniedHandler();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
  }

  @Test
  @DisplayName("should respond with 403 and a JSON body signalling insufficient permissions")
  void shouldRespond403WithJsonBody() throws IOException {
    // Given
    final var denied = new AccessDeniedException("insufficient role");

    // When
    handler.handle(request, response, denied);

    // Then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
    assertThat(response.getContentAsString())
        .contains("\"error\":\"Forbidden\"")
        .contains("\"message\":\"You do not have permission to access this resource\"");
  }
}
