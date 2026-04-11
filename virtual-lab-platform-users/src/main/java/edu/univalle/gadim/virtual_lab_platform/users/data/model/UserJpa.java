package edu.univalle.gadim.virtual_lab_platform.users.data.model;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.User;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
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
@Table(name = "users")
public class UserJpa implements User {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "name")
  private String name;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "external_code")
  private String externalCode;

  @Column(name = "password")
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private UserStatus status;

  @Column(name = "created_date")
  private LocalDateTime createdDate;

  @Override
  public String id() {
    return this.id;
  }

  @Override
  public String name() {
    return this.name;
  }

  @Override
  public String lastName() {
    return this.lastName;
  }

  @Override
  public Optional<String> externalCode() {
    return Optional.ofNullable(this.externalCode);
  }

  @Override
  public String password() {
    return this.password;
  }

  @Override
  public UserStatus status() {
    return this.status;
  }

  @Override
  public LocalDateTime createdDate() {
    return this.createdDate;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserJpa userJpa = (UserJpa) o;
    return getId() != null && Objects.equals(getId(), userJpa.getId());
  }

  @Override
  public final int hashCode() {
    return getClass().hashCode();
  }
}
