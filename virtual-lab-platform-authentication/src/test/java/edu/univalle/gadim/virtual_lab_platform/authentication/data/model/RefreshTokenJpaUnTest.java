package edu.univalle.gadim.virtual_lab_platform.authentication.data.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@NullMarked
@DisplayName("RefreshTokenJpa")
class RefreshTokenJpaUnTest {

  private static final String ID = "rt-001";
  private static final String USER_ID = "user-001";
  private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.token";
  private static final LocalDateTime EXPIRES_AT = LocalDateTime.of(2025, 2, 15, 10, 30, 0);
  private static final boolean REVOKED = false;
  private static final LocalDateTime CREATED_AT = LocalDateTime.of(2025, 1, 15, 10, 30, 0);

  @Nested
  @DisplayName("no-args constructor")
  class NoArgsConstructor {

    @Test
    @DisplayName("should create instance with null fields")
    void shouldCreateInstanceWithNullFields() {
      final var token = new RefreshTokenJpa();

      assertThat(token.getId()).isNull();
      assertThat(token.getUserId()).isNull();
      assertThat(token.getToken()).isNull();
      assertThat(token.getExpiresAt()).isNull();
      assertThat(token.isRevoked()).isFalse();
      assertThat(token.getCreatedAt()).isNull();
    }
  }

  @Nested
  @DisplayName("all-args constructor")
  class AllArgsConstructor {

    @Test
    @DisplayName("should populate all fields")
    void shouldPopulateAllFields() {
      final var token =
          new RefreshTokenJpa(ID, USER_ID, TOKEN, EXPIRES_AT, REVOKED, CREATED_AT);

      assertThat(token)
          .returns(ID, RefreshTokenJpa::getId)
          .returns(USER_ID, RefreshTokenJpa::getUserId)
          .returns(TOKEN, RefreshTokenJpa::getToken)
          .returns(EXPIRES_AT, RefreshTokenJpa::getExpiresAt)
          .returns(REVOKED, RefreshTokenJpa::isRevoked)
          .returns(CREATED_AT, RefreshTokenJpa::getCreatedAt);
    }
  }

  @Nested
  @DisplayName("setters")
  class Setters {

    @Test
    @DisplayName("should update id")
    void shouldUpdateId() {
      final var token = new RefreshTokenJpa();
      token.setId(ID);

      assertThat(token.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("should update userId")
    void shouldUpdateUserId() {
      final var token = new RefreshTokenJpa();
      token.setUserId(USER_ID);

      assertThat(token.getUserId()).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName("should update token")
    void shouldUpdateToken() {
      final var token = new RefreshTokenJpa();
      token.setToken(TOKEN);

      assertThat(token.getToken()).isEqualTo(TOKEN);
    }

    @Test
    @DisplayName("should update expiresAt")
    void shouldUpdateExpiresAt() {
      final var token = new RefreshTokenJpa();
      token.setExpiresAt(EXPIRES_AT);

      assertThat(token.getExpiresAt()).isEqualTo(EXPIRES_AT);
    }

    @Test
    @DisplayName("should update revoked")
    void shouldUpdateRevoked() {
      final var token = new RefreshTokenJpa();
      token.setRevoked(true);

      assertThat(token.isRevoked()).isTrue();
    }

    @Test
    @DisplayName("should update createdAt")
    void shouldUpdateCreatedAt() {
      final var token = new RefreshTokenJpa();
      token.setCreatedAt(CREATED_AT);

      assertThat(token.getCreatedAt()).isEqualTo(CREATED_AT);
    }
  }

  @Nested
  @DisplayName("interface accessors")
  class InterfaceAccessors {

    @Test
    @DisplayName("should delegate to fields")
    void shouldDelegateToFields() {
      final var token =
          new RefreshTokenJpa(ID, USER_ID, TOKEN, EXPIRES_AT, REVOKED, CREATED_AT);

      assertThat(token)
          .returns(ID, RefreshTokenJpa::id)
          .returns(USER_ID, RefreshTokenJpa::userId)
          .returns(TOKEN, RefreshTokenJpa::token)
          .returns(EXPIRES_AT, RefreshTokenJpa::expiresAt)
          .returns(REVOKED, RefreshTokenJpa::revoked)
          .returns(CREATED_AT, RefreshTokenJpa::createdAt);
    }
  }

  @Nested
  @DisplayName("toString")
  class ToString {

    @Test
    @DisplayName("should contain class name and field values")
    void shouldContainClassNameAndFieldValues() {
      final var token =
          new RefreshTokenJpa(ID, USER_ID, TOKEN, EXPIRES_AT, REVOKED, CREATED_AT);

      final var result = token.toString();

      assertThat(result)
          .contains("RefreshTokenJpa")
          .contains(ID)
          .contains(USER_ID)
          .contains(TOKEN)
          .contains(EXPIRES_AT.toString())
          .contains(CREATED_AT.toString());
    }
  }

  @Nested
  @DisplayName("equals and hashCode")
  class EqualsHashCode {

    @Test
    @DisplayName("should be equal when id matches")
    void shouldBeEqualWhenIdMatches() {
      final var a =
          new RefreshTokenJpa(ID, USER_ID, TOKEN, EXPIRES_AT, REVOKED, CREATED_AT);
      final var b =
          new RefreshTokenJpa(ID, "other-user", "other-token", EXPIRES_AT, true, CREATED_AT);

      assertThat(a).isEqualTo(b);
      assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should not be equal when id differs")
    void shouldNotBeEqualWhenIdDiffers() {
      final var a =
          new RefreshTokenJpa(ID, USER_ID, TOKEN, EXPIRES_AT, REVOKED, CREATED_AT);
      final var b =
          new RefreshTokenJpa("rt-002", USER_ID, TOKEN, EXPIRES_AT, REVOKED, CREATED_AT);

      assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("should not be equal to null")
    void shouldNotBeEqualToNull() {
      final var token =
          new RefreshTokenJpa(ID, USER_ID, TOKEN, EXPIRES_AT, REVOKED, CREATED_AT);

      assertThat(token).isNotEqualTo(null);
    }
  }
}
