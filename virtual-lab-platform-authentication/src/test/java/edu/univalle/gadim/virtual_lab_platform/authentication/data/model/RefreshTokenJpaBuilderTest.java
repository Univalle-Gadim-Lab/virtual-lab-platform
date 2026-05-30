package edu.univalle.gadim.virtual_lab_platform.authentication.data.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@NullMarked
@DisplayName("RefreshTokenJpa builder")
class RefreshTokenJpaBuilderTest {

  private static final String ID = "rt-001";
  private static final String USER_ID = "user-001";
  private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.token";
  private static final LocalDateTime EXPIRES_AT = LocalDateTime.of(2025, 2, 15, 10, 30, 0);
  private static final boolean REVOKED = false;
  private static final LocalDateTime CREATED_AT = LocalDateTime.of(2025, 1, 15, 10, 30, 0);

  @Test
  @DisplayName("should build with all fields")
  void shouldBuildWithAllFields() {
    final var token = RefreshTokenJpa.builder()
        .id(ID)
        .userId(USER_ID)
        .token(TOKEN)
        .expiresAt(EXPIRES_AT)
        .revoked(REVOKED)
        .createdAt(CREATED_AT)
        .build();

    assertThat(token)
        .returns(ID, RefreshTokenJpa::getId)
        .returns(USER_ID, RefreshTokenJpa::getUserId)
        .returns(TOKEN, RefreshTokenJpa::getToken)
        .returns(EXPIRES_AT, RefreshTokenJpa::getExpiresAt)
        .returns(REVOKED, RefreshTokenJpa::isRevoked)
        .returns(CREATED_AT, RefreshTokenJpa::getCreatedAt);
  }
}
