package edu.univalle.gadim.virtual_lab_platform.authentication.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@NullMarked
@DisplayName("JwtTokenOperation")
class JwtTokenOperationUnTest {

  private static final String SECRET = "test-secret-key-for-jwt-signing-256-bits-minimum!";
  private static final long ACCESS_EXPIRATION = 900000L;
  private static final long REFRESH_EXPIRATION = 604800000L;
  private static final String USER_ID = "user-001";
  private static final String USERNAME = "ana.martinez";
  private static final List<Role> ROLES = List.of(Role.STUDENT, Role.TEACHER);

  private JwtTokenOperation tokenService;

  @BeforeEach
  void setUp() {
    tokenService = new JwtTokenOperation(SECRET, ACCESS_EXPIRATION, REFRESH_EXPIRATION);
  }

  @Nested
  @DisplayName("generateAccessToken")
  class GenerateAccessToken {

    @Test
    @DisplayName("should generate a valid JWT")
    void shouldGenerateValidJwt() {
      final var token = tokenService.generateAccessToken(USER_ID, USERNAME, ROLES);

      assertThat(token).isNotBlank();
      assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("should contain correct claims")
    void shouldContainCorrectClaims() {
      final var token = tokenService.generateAccessToken(USER_ID, USERNAME, ROLES);

      assertThat(tokenService.extractUserId(token)).isEqualTo(USER_ID);
      assertThat(tokenService.extractUsername(token)).isEqualTo(USERNAME);
      assertThat(tokenService.extractRoles(token)).containsExactlyElementsOf(ROLES);
    }
  }

  @Nested
  @DisplayName("generateRefreshToken")
  class GenerateRefreshToken {

    @Test
    @DisplayName("should generate a valid JWT")
    void shouldGenerateValidJwt() {
      final var token = tokenService.generateRefreshToken(USER_ID);

      assertThat(token).isNotBlank();
      assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("should contain user ID in subject")
    void shouldContainUserIdInSubject() {
      final var token = tokenService.generateRefreshToken(USER_ID);

      assertThat(tokenService.extractUserId(token)).isEqualTo(USER_ID);
    }
  }

  @Nested
  @DisplayName("validateAccessToken")
  class ValidateAccessToken {

    @Test
    @DisplayName("should return true for valid access token")
    void shouldReturnTrueForValidAccessToken() {
      final var token = tokenService.generateAccessToken(USER_ID, USERNAME, ROLES);

      assertThat(tokenService.validateAccessToken(token)).isTrue();
    }

    @Test
    @DisplayName("should return false for invalid token")
    void shouldReturnFalseForInvalidToken() {
      assertThat(tokenService.validateAccessToken("invalid.token.value")).isFalse();
    }

    @Test
    @DisplayName("should return false for refresh token used as access token")
    void shouldReturnFalseForRefreshToken() {
      final var refreshToken = tokenService.generateRefreshToken(USER_ID);

      assertThat(tokenService.validateAccessToken(refreshToken)).isFalse();
    }
  }

  @Nested
  @DisplayName("extractUserId")
  class ExtractUserId {

    @Test
    @DisplayName("should extract user ID from token")
    void shouldExtractUserId() {
      final var token = tokenService.generateAccessToken(USER_ID, USERNAME, ROLES);

      assertThat(tokenService.extractUserId(token)).isEqualTo(USER_ID);
    }
  }

  @Nested
  @DisplayName("extractUsername")
  class ExtractUsername {

    @Test
    @DisplayName("should extract username from token")
    void shouldExtractUsername() {
      final var token = tokenService.generateAccessToken(USER_ID, USERNAME, ROLES);

      assertThat(tokenService.extractUsername(token)).isEqualTo(USERNAME);
    }
  }

  @Nested
  @DisplayName("extractRoles")
  class ExtractRoles {

    @Test
    @DisplayName("should extract roles from token")
    void shouldExtractRoles() {
      final var token = tokenService.generateAccessToken(USER_ID, USERNAME, ROLES);

      assertThat(tokenService.extractRoles(token)).containsExactlyElementsOf(ROLES);
    }

    @Test
    @DisplayName("should return empty list for refresh token")
    void shouldReturnEmptyListForRefreshToken() {
      final var refreshToken = tokenService.generateRefreshToken(USER_ID);

      assertThat(tokenService.extractRoles(refreshToken)).isEmpty();
    }
  }

  @Nested
  @DisplayName("error handling")
  class ErrorHandling {

    @Test
    @DisplayName("should return false for malformed token")
    void shouldReturnFalseForMalformedToken() {
      assertThat(tokenService.validateAccessToken("not-a-jwt")).isFalse();
    }

    @Test
    @DisplayName("should throw for malformed token on extract")
    void shouldThrowForMalformedTokenOnExtract() {
      assertThatThrownBy(() -> tokenService.extractUserId("not-a-jwt"))
          .isInstanceOf(Exception.class);
    }
  }
}
