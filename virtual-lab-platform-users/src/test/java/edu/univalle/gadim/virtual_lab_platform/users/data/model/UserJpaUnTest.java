package edu.univalle.gadim.virtual_lab_platform.users.data.model;

import static org.assertj.core.api.Assertions.assertThat;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus;
import java.time.LocalDateTime;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@NullMarked
@DisplayName("UserJpa")
class UserJpaUnTest {

    private static final String ID = "user-001";
    private static final String NAME = "Ana";
    private static final String LAST_NAME = "Martinez";
    private static final String EXTERNAL_CODE = "2024101001";
    private static final String PASSWORD = "s3cur3p4ss";
    private static final UserStatus STATUS = UserStatus.ACTIVE;
    private static final LocalDateTime CREATED_DATE = LocalDateTime.of(2025, 1, 15, 10, 30, 0);

    @Nested
    @DisplayName("no-args constructor")
    class NoArgsConstructor {

        @Test
        @DisplayName("should create instance with null fields")
        void shouldCreateInstanceWithNullFields() {
            final var user = new UserJpa();

            assertThat(user.getId()).isNull();
            assertThat(user.getName()).isNull();
            assertThat(user.getLastName()).isNull();
            assertThat(user.getExternalCode()).isNull();
            assertThat(user.getPassword()).isNull();
            assertThat(user.getStatus()).isNull();
            assertThat(user.getCreatedDate()).isNull();
        }
    }

    @Nested
    @DisplayName("all-args constructor")
    class AllArgsConstructor {

        @Test
        @DisplayName("should populate all fields")
        void shouldPopulateAllFields() {
            final var user = new UserJpa(ID, NAME, LAST_NAME, EXTERNAL_CODE, PASSWORD, STATUS, CREATED_DATE);

            assertThat(user.getId()).isEqualTo(ID);
            assertThat(user.getName()).isEqualTo(NAME);
            assertThat(user.getLastName()).isEqualTo(LAST_NAME);
            assertThat(user.getExternalCode()).isEqualTo(EXTERNAL_CODE);
            assertThat(user.getPassword()).isEqualTo(PASSWORD);
            assertThat(user.getStatus()).isEqualTo(STATUS);
            assertThat(user.getCreatedDate()).isEqualTo(CREATED_DATE);
        }
    }

    @Nested
    @DisplayName("setters")
    class Setters {

        @Test
        @DisplayName("should update id")
        void shouldUpdateId() {
            final var user = new UserJpa();
            user.setId(ID);

            assertThat(user.getId()).isEqualTo(ID);
        }

        @Test
        @DisplayName("should update name")
        void shouldUpdateName() {
            final var user = new UserJpa();
            user.setName(NAME);

            assertThat(user.getName()).isEqualTo(NAME);
        }

        @Test
        @DisplayName("should update lastName")
        void shouldUpdateLastName() {
            final var user = new UserJpa();
            user.setLastName(LAST_NAME);

            assertThat(user.getLastName()).isEqualTo(LAST_NAME);
        }

        @Test
        @DisplayName("should update externalCode")
        void shouldUpdateExternalCode() {
            final var user = new UserJpa();
            user.setExternalCode(EXTERNAL_CODE);

            assertThat(user.getExternalCode()).isEqualTo(EXTERNAL_CODE);
        }

        @Test
        @DisplayName("should update password")
        void shouldUpdatePassword() {
            final var user = new UserJpa();
            user.setPassword(PASSWORD);

            assertThat(user.getPassword()).isEqualTo(PASSWORD);
        }

        @Test
        @DisplayName("should update status")
        void shouldUpdateStatus() {
            final var user = new UserJpa();
            user.setStatus(STATUS);

            assertThat(user.getStatus()).isEqualTo(STATUS);
        }

        @Test
        @DisplayName("should update createdDate")
        void shouldUpdateCreatedDate() {
            final var user = new UserJpa();
            user.setCreatedDate(CREATED_DATE);

            assertThat(user.getCreatedDate()).isEqualTo(CREATED_DATE);
        }
    }

    @Nested
    @DisplayName("toString")
    class ToString {

        @Test
        @DisplayName("should contain class name and field values")
        void shouldContainClassNameAndFieldValues() {
            final var user = UserJpa.builder()
                    .id(ID)
                    .name(NAME)
                    .lastName(LAST_NAME)
                    .externalCode(EXTERNAL_CODE)
                    .password(PASSWORD)
                    .status(STATUS)
                    .createdDate(CREATED_DATE)
                    .build();

            final var result = user.toString();

            assertThat(result)
                    .contains("UserJpa")
                    .contains(ID)
                    .contains(NAME)
                    .contains(LAST_NAME)
                    .contains(EXTERNAL_CODE)
                    .contains(PASSWORD)
                    .contains(STATUS.name())
                    .contains(CREATED_DATE.toString());
        }
    }
}
