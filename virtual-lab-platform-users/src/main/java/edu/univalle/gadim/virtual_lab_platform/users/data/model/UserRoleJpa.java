package edu.univalle.gadim.virtual_lab_platform.users.data.model;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserRole;
import edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRoleRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import javax.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * JPA entity representing a user-to-role association.
 *
 * <p>Mapped to the {@code user_roles} table and implements the {@link UserRole} domain contract. A
 * unique constraint on {@code (user_id, role)} prevents duplicate role assignments.
 *
 * @see UserRole
 * @see Role
 * @see UserRoleRepository
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_roles")
public class UserRoleJpa implements UserRole {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "user_id")
  private String userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private Role role;

  @Override
  @Nonnull
  public String id() {
    return this.id;
  }

  @Override
  @Nonnull
  public String userId() {
    return userId;
  }

  @Override
  @Nonnull
  public Role role() {
    return this.role;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserRoleJpa that = (UserRoleJpa) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(getId());
  }
}
