package edu.univalle.gadim.virtual_lab_platform.instances.data.model;

import static org.assertj.core.api.Assertions.assertThat;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("InstanceUserJpa Builder")
class InstanceUserJpaBuilderTest {

  private static final String ID = "iu-001";
  private static final String INSTANCE_ID = "inst-001";
  private static final String USER_ID = "user-001";

  private InstanceUserJpa.InstanceUserJpaBuilder fullBuilder() {
    return InstanceUserJpa.builder().id(ID).instanceId(INSTANCE_ID).userId(USER_ID);
  }

  @Nested
  @DisplayName("when building with all fields")
  class AllFields {

    @Test
    @DisplayName("should populate every field via builder getters")
    void shouldPopulateEveryFieldViaGetters() {
      InstanceUserJpa instanceUser = fullBuilder().build();

      assertThat(instanceUser)
          .returns(ID, InstanceUserJpa::getId)
          .returns(INSTANCE_ID, InstanceUserJpa::getInstanceId)
          .returns(USER_ID, InstanceUserJpa::getUserId);
    }

    @Test
    @DisplayName("should return correct values from InstanceUser interface methods")
    void shouldReturnCorrectValuesFromInterfaceMethods() {
      InstanceUserJpa instanceUser = fullBuilder().build();

      assertThat(instanceUser)
          .returns(ID, InstanceUser::id)
          .returns(INSTANCE_ID, InstanceUser::instanceId)
          .returns(USER_ID, InstanceUser::userId);
    }

    @Test
    @DisplayName("should implement InstanceUser interface")
    void shouldImplementInstanceUserInterface() {
      InstanceUserJpa instanceUser = fullBuilder().build();

      assertThat(instanceUser).isInstanceOf(InstanceUser.class);
    }
  }

  @Nested
  @DisplayName("equals and hashCode contract")
  class Equality {

    @Test
    @DisplayName("should be equal when ids are the same")
    void shouldBeEqualWhenIdsAreSame() {
      InstanceUserJpa user1 =
          InstanceUserJpa.builder().id(ID).instanceId(INSTANCE_ID).userId(USER_ID).build();
      InstanceUserJpa user2 =
          InstanceUserJpa.builder().id(ID).instanceId("other-inst").userId("other-user").build();

      assertThat(user1).isEqualTo(user2).hasSameHashCodeAs(user2);
    }

    @Test
    @DisplayName("should not be equal when ids differ")
    void shouldNotBeEqualWhenIdsDiffer() {
      InstanceUserJpa user1 =
          InstanceUserJpa.builder().id(ID).instanceId(INSTANCE_ID).userId(USER_ID).build();
      InstanceUserJpa user2 =
          InstanceUserJpa.builder().id("iu-002").instanceId(INSTANCE_ID).userId(USER_ID).build();

      assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    @DisplayName("should not be equal to null")
    void shouldNotBeEqualToNull() {
      InstanceUserJpa instanceUser = fullBuilder().build();

      assertThat(instanceUser).isNotEqualTo(null);
    }

    @Test
    @DisplayName("should be equal to itself")
    void shouldBeEqualToItself() {
      InstanceUserJpa instanceUser = fullBuilder().build();

      assertThat(instanceUser).isEqualTo(instanceUser);
    }

    @Test
    @DisplayName("should not be equal when other has null id")
    void shouldNotBeEqualWhenOtherHasNullId() {
      InstanceUserJpa userWithId =
          InstanceUserJpa.builder().id(ID).instanceId(INSTANCE_ID).userId(USER_ID).build();
      InstanceUserJpa userWithoutId =
          InstanceUserJpa.builder().instanceId(INSTANCE_ID).userId(USER_ID).build();

      assertThat(userWithId).isNotEqualTo(userWithoutId);
    }
  }

  @Nested
  @DisplayName("builder instances")
  class BuilderInstances {

    @Test
    @DisplayName("should produce distinct objects on successive builds")
    void shouldProduceDistinctObjectsOnSuccessiveBuilds() {
      InstanceUserJpa.InstanceUserJpaBuilder builder = fullBuilder();

      InstanceUserJpa first = builder.build();
      InstanceUserJpa second = builder.build();

      assertThat(first).isNotSameAs(second);
    }

    @Test
    @DisplayName("should allow field override on builder")
    void shouldAllowFieldOverrideOnBuilder() {
      InstanceUserJpa instanceUser =
          InstanceUserJpa.builder()
              .id(ID)
              .instanceId(INSTANCE_ID)
              .userId(USER_ID)
              .userId("overridden-user")
              .build();

      assertThat(instanceUser)
          .returns("overridden-user", InstanceUserJpa::getUserId)
          .returns(ID, InstanceUserJpa::getId);
    }
  }
}
