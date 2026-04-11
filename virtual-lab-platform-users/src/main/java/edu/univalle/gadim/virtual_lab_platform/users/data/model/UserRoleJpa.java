package edu.univalle.gadim.virtual_lab_platform.users.data.model;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
  public String id() {
    return this.id;
  }

  @Override
  public String userId() {
    return userId;
  }

  @Override
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
    return getClass().hashCode();
  }
}
