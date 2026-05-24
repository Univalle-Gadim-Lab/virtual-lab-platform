package edu.univalle.gadim.virtual_lab_platform.instances.data.model;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * JPA entity representing the many-to-many association between users and instances.
 *
 * <p>Mapped to the {@code instance_users} table and implements the {@link InstanceUser}
 * domain contract. Establishes ownership and access rights for users to specific instances.
 *
 * @see InstanceUser
 * @see InstanceUserRepository
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "instance_users")
public class InstanceUserJpa implements InstanceUser {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "instance_id")
  private String instanceId;

  @Column(name = "user_id")
  private String userId;

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InstanceUserJpa instanceUserJpa = (InstanceUserJpa) o;
    return getId() != null && Objects.equals(getId(), instanceUserJpa.getId());
  }

  @Override
  public String id() {
    return this.id;
  }

  @Override
  public String instanceId() {
    return this.instanceId;
  }

  @Override
  public String userId() {
    return this.userId;
  }

  @Override
  public final int hashCode() {
    return Objects.hash(getId());
  }
}