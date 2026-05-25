package edu.univalle.gadim.virtual_lab_platform.users.data.model;

import static org.assertj.core.api.Assertions.assertThat;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.User;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("UserJpa Builder")
class UserJpaBuilderTest {

  private static final String ID = "user-001";
  private static final String NAME = "Ana";
  private static final String LAST_NAME = "Martinez";
  private static final String EXTERNAL_CODE = "2024101001";
  private static final String PASSWORD = "s3cur3p4ss";
  private static final UserStatus STATUS = UserStatus.ACTIVE;
  private static final LocalDateTime CREATED_DATE = LocalDateTime.of(2025, 1, 15, 10, 30, 0);

  @Nested
  @DisplayName("when building with all fields")
  class AllFields {

    @Test
    @DisplayName("should populate every field via builder")
    void shouldPopulateEveryField() {
      UserJpa user =
          UserJpa.builder()
              .id(ID)
              .name(NAME)
              .lastName(LAST_NAME)
              .externalCode(EXTERNAL_CODE)
              .password(PASSWORD)
              .status(STATUS)
              .createdDate(CREATED_DATE)
              .build();

      assertThat(user.getId()).isEqualTo(ID);
      assertThat(user.getLastName()).isEqualTo(LAST_NAME);
      assertThat(user.getPassword()).isEqualTo(PASSWORD);
      assertThat(user.getStatus()).isEqualTo(STATUS);
      assertThat(user.getCreatedDate()).isEqualTo(CREATED_DATE);
    }

    @Test
    @DisplayName("should return correct values from User interface methods")
    void shouldReturnCorrectValuesFromInterfaceMethods() {
      UserJpa user =
          UserJpa.builder()
              .id(ID)
              .name(NAME)
              .lastName(LAST_NAME)
              .externalCode(EXTERNAL_CODE)
              .password(PASSWORD)
              .status(STATUS)
              .createdDate(CREATED_DATE)
              .build();

      assertThat(user.id()).isEqualTo(ID);
      assertThat(user.name()).isEqualTo(NAME);
      assertThat(user.lastName()).isEqualTo(LAST_NAME);
      assertThat(user.externalCode()).isEqualTo(Optional.of(EXTERNAL_CODE));
      assertThat(user.password()).isEqualTo(PASSWORD);
      assertThat(user.status()).isEqualTo(STATUS);
      assertThat(user.createdDate()).isEqualTo(CREATED_DATE);
    }

    @Test
    @DisplayName("should implement User interface")
    void shouldImplementUserInterface() {
      UserJpa user =
          UserJpa.builder()
              .id(ID)
              .name(NAME)
              .lastName(LAST_NAME)
              .externalCode(EXTERNAL_CODE)
              .password(PASSWORD)
              .status(STATUS)
              .createdDate(CREATED_DATE)
              .build();

      assertThat(user).isInstanceOf(User.class);
    }
  }

  @Nested
  @DisplayName("when building with minimal fields")
  class MinimalFields {

    @Test
    @DisplayName("should default unset fields to null")
    void shouldDefaultUnsetFieldsToNull() {
      UserJpa user =
          UserJpa.builder()
              .id(ID)
              .name(NAME)
              .lastName(LAST_NAME)
              .password(PASSWORD)
              .status(STATUS)
              .createdDate(CREATED_DATE)
              .build();

      assertThat(user.getId()).isEqualTo(ID);
      assertThat(user.getName()).isEqualTo(NAME);
      assertThat(user.getLastName()).isEqualTo(LAST_NAME);
      assertThat(user.getExternalCode()).isNull();
      assertThat(user.getPassword()).isEqualTo(PASSWORD);
      assertThat(user.getStatus()).isEqualTo(STATUS);
      assertThat(user.getCreatedDate()).isEqualTo(CREATED_DATE);
    }

    @Test
    @DisplayName("should return empty Optional for null externalCode")
    void shouldReturnEmptyOptionalForNullExternalCode() {
      UserJpa user =
          UserJpa.builder()
              .id(ID)
              .name(NAME)
              .lastName(LAST_NAME)
              .password(PASSWORD)
              .status(STATUS)
              .createdDate(CREATED_DATE)
              .build();

      assertThat(user.externalCode()).isEmpty();
    }

    @Test
    @DisplayName("should return present Optional for non-null externalCode")
    void shouldReturnPresentOptionalForNonNullExternalCode() {
      UserJpa user =
          UserJpa.builder()
              .id(ID)
              .name(NAME)
              .lastName(LAST_NAME)
              .externalCode(EXTERNAL_CODE)
              .password(PASSWORD)
              .status(STATUS)
              .createdDate(CREATED_DATE)
              .build();

      assertThat(user.externalCode()).isPresent().contains(EXTERNAL_CODE);
    }
  }

  @Nested
  @DisplayName("equals and hashCode contract")
  class Equality {

    @Test
    @DisplayName("should be equal when ids are the same")
    void shouldBeEqualWhenIdsAreSame() {
      UserJpa user1 =
          UserJpa.builder()
              .id(ID)
              .name(NAME)
              .lastName(LAST_NAME)
              .password(PASSWORD)
              .status(STATUS)
              .createdDate(CREATED_DATE)
              .build();

      UserJpa user2 =
          UserJpa.builder()
              .id(ID)
              .name("Different")
              .lastName("Different")
              .password("other")
              .status(UserStatus.INACTIVE)
              .createdDate(CREATED_DATE.plusDays(1))
              .build();

      assertThat(user1).isEqualTo(user2).hasSameHashCodeAs(user2);
    }

    @Test
    @DisplayName("should not be equal when ids differ")
    void shouldNotBeEqualWhenIdsDiffer() {
      UserJpa user1 =
          UserJpa.builder()
              .id(ID)
              .name(NAME)
              .lastName(LAST_NAME)
              .password(PASSWORD)
              .status(STATUS)
              .createdDate(CREATED_DATE)
              .build();

      UserJpa user2 =
          UserJpa.builder()
              .id("user-002")
              .name(NAME)
              .lastName(LAST_NAME)
              .password(PASSWORD)
              .status(STATUS)
              .createdDate(CREATED_DATE)
              .build();

      assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    @DisplayName("should not be equal to null")
    void shouldNotBeEqualToNull() {
      UserJpa user =
          UserJpa.builder()
              .id(ID)
              .name(NAME)
              .lastName(LAST_NAME)
              .password(PASSWORD)
              .status(STATUS)
              .createdDate(CREATED_DATE)
              .build();

      assertThat(user).isNotEqualTo(null);
    }

    @Test
    @DisplayName("should be equal to itself")
    void shouldBeEqualToItself() {
      UserJpa user =
          UserJpa.builder()
              .id(ID)
              .name(NAME)
              .lastName(LAST_NAME)
              .password(PASSWORD)
              .status(STATUS)
              .createdDate(CREATED_DATE)
              .build();

      assertThat(user).isEqualTo(user);
    }

    @Test
    @DisplayName("should not be equal when other has null id")
    void shouldNotBeEqualWhenOtherHasNullId() {
      UserJpa userWithId =
          UserJpa.builder()
              .id(ID)
              .name(NAME)
              .lastName(LAST_NAME)
              .password(PASSWORD)
              .status(STATUS)
              .createdDate(CREATED_DATE)
              .build();

      UserJpa userWithoutId =
          UserJpa.builder()
              .name(NAME)
              .lastName(LAST_NAME)
              .password(PASSWORD)
              .status(STATUS)
              .createdDate(CREATED_DATE)
              .build();

      assertThat(userWithId).isNotEqualTo(userWithoutId);
    }
  }

  @Nested
  @DisplayName("builder instances")
  class BuilderInstances {

    @Test
    @DisplayName("should produce distinct objects on successive builds")
    void shouldProduceDistinctObjectsOnSuccessiveBuilds() {
      UserJpa.UserJpaBuilder builder =
          UserJpa.builder()
              .id(ID)
              .name(NAME)
              .lastName(LAST_NAME)
              .password(PASSWORD)
              .status(STATUS)
              .createdDate(CREATED_DATE);

      UserJpa first = builder.build();
      UserJpa second = builder.build();

      assertThat(first).isNotSameAs(second);
    }

    @Test
    @DisplayName("should allow partial build and field override")
    void shouldAllowPartialBuildAndFieldOverride() {
      UserJpa user =
          UserJpa.builder()
              .id(ID)
              .name(NAME)
              .lastName(LAST_NAME)
              .password(PASSWORD)
              .status(STATUS)
              .createdDate(CREATED_DATE)
              .name("Maria")
              .build();

      assertThat(user.getName()).isEqualTo("Maria");
      assertThat(user.getId()).isEqualTo(ID);
    }
  }
}
