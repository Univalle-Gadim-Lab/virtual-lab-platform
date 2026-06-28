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
import org.springframework.security.core.AuthenticationException;

@NullMarked
@DisplayName("JwtAuthenticationEntryPoint")
class JwtAuthenticationEntryPointUnTest {

  private JwtAuthenticationEntryPoint entryPoint;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() {
    entryPoint = new JwtAuthenticationEntryPoint();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
  }

  @Test
  @DisplayName("should respond with 401 and a JSON body signalling missing authentication")
  void shouldRespond401WithJsonBody() throws IOException {
    // Given
    final var authException = new AuthenticationException("missing token") {};

    // When
    entryPoint.commence(request, response, authException);

    // Then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
    assertThat(response.getContentAsString())
        .contains("\"error\":\"Unauthorized\"")
        .contains("\"message\":\"Authentication is required to access this resource\"");
  }
}
