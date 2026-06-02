package edu.univalle.gadim.virtual_lab_platform.instances.data.model;

import static org.assertj.core.api.Assertions.assertThat;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceUser;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@NullMarked
@DisplayName("InstanceUserJpa")
class InstanceUserJpaUnTest {

  private static final String ID = "iu-001";
  private static final String INSTANCE_ID = "inst-001";
  private static final String USER_ID = "ana.martinez@correounivalle.edu.co";

  @Nested
  @DisplayName("no-args constructor")
  class NoArgsConstructor {

    @Test
    @DisplayName("should create instance with null fields")
    void shouldCreateInstanceWithNullFields() {
      final var instanceUser = new InstanceUserJpa();

      assertThat(instanceUser.getId()).isNull();
      assertThat(instanceUser.getInstanceId()).isNull();
      assertThat(instanceUser.getUserId()).isNull();
    }
  }

  @Nested
  @DisplayName("all-args constructor")
  class AllArgsConstructor {

    @Test
    @DisplayName("should populate all fields")
    void shouldPopulateAllFields() {
      final var instanceUser = new InstanceUserJpa(ID, INSTANCE_ID, USER_ID);

      assertThat(instanceUser)
          .returns(ID, InstanceUserJpa::getId)
          .returns(INSTANCE_ID, InstanceUserJpa::getInstanceId)
          .returns(USER_ID, InstanceUserJpa::getUserId);
    }
  }

  @Nested
  @DisplayName("setters")
  class Setters {

    @Test
    @DisplayName("should update id")
    void shouldUpdateId() {
      final var instanceUser = new InstanceUserJpa();
      instanceUser.setId(ID);

      assertThat(instanceUser.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("should update instanceId")
    void shouldUpdateInstanceId() {
      final var instanceUser = new InstanceUserJpa();
      instanceUser.setInstanceId(INSTANCE_ID);

      assertThat(instanceUser.getInstanceId()).isEqualTo(INSTANCE_ID);
    }

    @Test
    @DisplayName("should update userId")
    void shouldUpdateUserId() {
      final var instanceUser = new InstanceUserJpa();
      instanceUser.setUserId(USER_ID);

      assertThat(instanceUser.getUserId()).isEqualTo(USER_ID);
    }
  }

  @Nested
  @DisplayName("toString")
  class ToString {

    @Test
    @DisplayName("should contain class name and field values")
    void shouldContainClassNameAndFieldValues() {
      final var instanceUser =
          InstanceUserJpa.builder().id(ID).instanceId(INSTANCE_ID).userId(USER_ID).build();

      final var result = instanceUser.toString();

      assertThat(result)
          .contains("InstanceUserJpa")
          .contains(ID)
          .contains(INSTANCE_ID)
          .contains(USER_ID);
    }
  }

  @Nested
  @DisplayName("interface methods")
  class InterfaceMethods {

    @Test
    @DisplayName("should implement InstanceUser interface")
    void shouldImplementInstanceUserInterface() {
      final var instanceUser =
          InstanceUserJpa.builder().id(ID).instanceId(INSTANCE_ID).userId(USER_ID).build();

      assertThat(instanceUser).isInstanceOf(InstanceUser.class);
    }

    @Test
    @DisplayName("should return correct values from interface methods")
    void shouldReturnCorrectValuesFromInterfaceMethods() {
      final var instanceUser =
          InstanceUserJpa.builder().id(ID).instanceId(INSTANCE_ID).userId(USER_ID).build();

      assertThat(instanceUser)
          .returns(ID, InstanceUser::id)
          .returns(INSTANCE_ID, InstanceUser::instanceId)
          .returns(USER_ID, InstanceUser::userId);
    }
  }

  @Nested
  @DisplayName("equals and hashCode")
  class EqualsAndHashCode {

    @Test
    @DisplayName("should be equal when ids are the same")
    void shouldBeEqualWhenIdsAreSame() {
      final var user1 =
          InstanceUserJpa.builder().id(ID).instanceId(INSTANCE_ID).userId(USER_ID).build();
      final var user2 =
          InstanceUserJpa.builder().id(ID).instanceId("other-inst").userId("other-user").build();

      assertThat(user1).isEqualTo(user2).hasSameHashCodeAs(user2);
    }

    @Test
    @DisplayName("should not be equal when ids differ")
    void shouldNotBeEqualWhenIdsDiffer() {
      final var user1 =
          InstanceUserJpa.builder().id(ID).instanceId(INSTANCE_ID).userId(USER_ID).build();
      final var user2 =
          InstanceUserJpa.builder().id("iu-002").instanceId(INSTANCE_ID).userId(USER_ID).build();

      assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    @DisplayName("should not be equal to null")
    void shouldNotBeEqualToNull() {
      final var instanceUser =
          InstanceUserJpa.builder().id(ID).instanceId(INSTANCE_ID).userId(USER_ID).build();

      assertThat(instanceUser).isNotEqualTo(null);
    }

    @Test
    @DisplayName("should be equal to itself")
    void shouldBeEqualToItself() {
      final var instanceUser =
          InstanceUserJpa.builder().id(ID).instanceId(INSTANCE_ID).userId(USER_ID).build();

      assertThat(instanceUser).isEqualTo(instanceUser);
    }

    @Test
    @DisplayName("should not be equal when other has null id")
    void shouldNotBeEqualWhenOtherHasNullId() {
      final var userWithId =
          InstanceUserJpa.builder().id(ID).instanceId(INSTANCE_ID).userId(USER_ID).build();
      final var userWithoutId =
          InstanceUserJpa.builder().instanceId(INSTANCE_ID).userId(USER_ID).build();

      assertThat(userWithId).isNotEqualTo(userWithoutId);
    }
  }
}
