package edu.univalle.gadim.virtual_lab_platform.users.data.model;

import static org.assertj.core.api.Assertions.assertThat;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@NullMarked
@DisplayName("UserRoleJpa")
class UserRoleJpaUnTest {

  private static final String ID = "user-role-001";
  private static final String USER_ID = "user-001";
  private static final Role ROLE = Role.STUDENT;

  @Nested
  @DisplayName("no-args constructor")
  class NoArgsConstructor {

    @Test
    @DisplayName("should create instance with null fields")
    void shouldCreateInstanceWithNullFields() {
      final var userRole = new UserRoleJpa();

      assertThat(userRole.getId()).isNull();
      assertThat(userRole.getUserId()).isNull();
      assertThat(userRole.getRole()).isNull();
    }
  }

  @Nested
  @DisplayName("all-args constructor")
  class AllArgsConstructor {

    @Test
    @DisplayName("should populate all fields")
    void shouldPopulateAllFields() {
      final var userRole = new UserRoleJpa(ID, USER_ID, ROLE);

      assertThat(userRole.getId()).isEqualTo(ID);
      assertThat(userRole.getUserId()).isEqualTo(USER_ID);
      assertThat(userRole.getRole()).isEqualTo(ROLE);
    }
  }

  @Nested
  @DisplayName("setters")
  class Setters {

    @Test
    @DisplayName("should update id")
    void shouldUpdateId() {
      final var userRole = new UserRoleJpa();
      userRole.setId(ID);

      assertThat(userRole.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("should update userId")
    void shouldUpdateUserId() {
      final var userRole = new UserRoleJpa();
      userRole.setUserId(USER_ID);

      assertThat(userRole.getUserId()).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName("should update role")
    void shouldUpdateRole() {
      final var userRole = new UserRoleJpa();
      userRole.setRole(ROLE);

      assertThat(userRole.getRole()).isEqualTo(ROLE);
    }
  }

  @Nested
  @DisplayName("toString")
  class ToString {

    @Test
    @DisplayName("should contain class name and field values")
    void shouldContainClassNameAndFieldValues() {
      final var userRole = UserRoleJpa.builder().id(ID).userId(USER_ID).role(ROLE).build();

      final var result = userRole.toString();

      assertThat(result)
          .contains("UserRoleJpa")
          .contains(ID)
          .contains(USER_ID)
          .contains(ROLE.name());
    }
  }
}
