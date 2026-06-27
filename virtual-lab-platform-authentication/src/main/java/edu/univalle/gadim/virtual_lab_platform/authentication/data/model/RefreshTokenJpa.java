package edu.univalle.gadim.virtual_lab_platform.authentication.data.model;

import edu.univalle.gadim.virtual_lab_platform.authentication.api.type.RefreshToken;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * JPA entity representing a refresh token.
 *
 * <p>Mapped to the {@code refresh_tokens} table and implements the
 * {@link RefreshToken} domain contract. Stores the raw token string,
 * owning user ID, expiration, revocation status, and creation timestamp.
 *
 * <p><b>Thread Safety:</b> This class is not thread-safe. Instances should not
 * be shared across threads without external synchronization.
 *
 * @see RefreshToken
 * @see edu.univalle.gadim.virtual_lab_platform.authentication.data.repository.RefreshTokenRepository
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "refresh_tokens")
public class RefreshTokenJpa implements RefreshToken {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "token")
  private String token;

  @Column(name = "expires_at")
  private LocalDateTime expiresAt;

  @Column(name = "revoked")
  private boolean revoked;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Override
  public String id() {
    return this.id;
  }

  @Override
  public String userId() {
    return this.userId;
  }

  @Override
  public String token() {
    return this.token;
  }

  @Override
  public LocalDateTime expiresAt() {
    return this.expiresAt;
  }

  @Override
  public boolean revoked() {
    return this.revoked;
  }

  @Override
  public LocalDateTime createdAt() {
    return this.createdAt;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RefreshTokenJpa that = (RefreshTokenJpa) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(getId());
  }
}
