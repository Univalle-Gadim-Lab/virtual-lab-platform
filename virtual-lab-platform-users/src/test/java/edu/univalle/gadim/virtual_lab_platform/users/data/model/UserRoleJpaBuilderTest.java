package edu.univalle.gadim.virtual_lab_platform.users.data.model;

import static org.assertj.core.api.Assertions.assertThat;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("UserRoleJpa Builder")
class UserRoleJpaBuilderTest {

    private static final String ID = "user-role-001";
    private static final String USER_ID = "user-001";
    private static final Role ROLE = Role.STUDENT;

    @Nested
    @DisplayName("when building with all fields")
    class AllFields {

        @Test
        @DisplayName("should populate every field via builder")
        void shouldPopulateEveryField() {
            UserRoleJpa userRole = UserRoleJpa.builder()
                    .id(ID)
                    .userId(USER_ID)
                    .role(ROLE)
                    .build();

            assertThat(userRole.getId()).isEqualTo(ID);
            assertThat(userRole.getUserId()).isEqualTo(USER_ID);
            assertThat(userRole.getRole()).isEqualTo(ROLE);
        }

        @Test
        @DisplayName("should return correct values from UserRole interface methods")
        void shouldReturnCorrectValuesFromInterfaceMethods() {
            UserRoleJpa userRole = UserRoleJpa.builder()
                    .id(ID)
                    .userId(USER_ID)
                    .role(ROLE)
                    .build();

            assertThat(userRole.id()).isEqualTo(ID);
            assertThat(userRole.userId()).isEqualTo(USER_ID);
            assertThat(userRole.role()).isEqualTo(ROLE);
        }

        @Test
        @DisplayName("should implement UserRole interface")
        void shouldImplementUserRoleInterface() {
            UserRoleJpa userRole = UserRoleJpa.builder()
                    .id(ID)
                    .userId(USER_ID)
                    .role(ROLE)
                    .build();

            assertThat(userRole).isInstanceOf(UserRole.class);
        }

        @Test
        @DisplayName("should support all Role values")
        void shouldSupportAllRoleValues() {
            for (Role role : Role.values()) {
                UserRoleJpa userRole = UserRoleJpa.builder()
                        .id(ID)
                        .userId(USER_ID)
                        .role(role)
                        .build();

                assertThat(userRole.role()).isEqualTo(role);
            }
        }
    }

    @Nested
    @DisplayName("equals and hashCode contract")
    class Equality {

        @Test
        @DisplayName("should be equal when ids are the same")
        void shouldBeEqualWhenIdsAreSame() {
            UserRoleJpa role1 = UserRoleJpa.builder()
                    .id(ID)
                    .userId(USER_ID)
                    .role(ROLE)
                    .build();

            UserRoleJpa role2 = UserRoleJpa.builder()
                    .id(ID)
                    .userId("different-user")
                    .role(Role.ADMIN)
                    .build();

            assertThat(role1).isEqualTo(role2);
            assertThat(role1.hashCode()).isEqualTo(role2.hashCode());
        }

        @Test
        @DisplayName("should not be equal when ids differ")
        void shouldNotBeEqualWhenIdsDiffer() {
            UserRoleJpa role1 = UserRoleJpa.builder()
                    .id(ID)
                    .userId(USER_ID)
                    .role(ROLE)
                    .build();

            UserRoleJpa role2 = UserRoleJpa.builder()
                    .id("user-role-002")
                    .userId(USER_ID)
                    .role(ROLE)
                    .build();

            assertThat(role1).isNotEqualTo(role2);
        }

        @Test
        @DisplayName("should not be equal to null")
        void shouldNotBeEqualToNull() {
            UserRoleJpa userRole = UserRoleJpa.builder()
                    .id(ID)
                    .userId(USER_ID)
                    .role(ROLE)
                    .build();

            assertThat(userRole).isNotEqualTo(null);
        }

        @Test
        @DisplayName("should be equal to itself")
        void shouldBeEqualToItself() {
            UserRoleJpa userRole = UserRoleJpa.builder()
                    .id(ID)
                    .userId(USER_ID)
                    .role(ROLE)
                    .build();

            assertThat(userRole).isEqualTo(userRole);
        }

        @Test
        @DisplayName("should not be equal when other has null id")
        void shouldNotBeEqualWhenOtherHasNullId() {
            UserRoleJpa roleWithId = UserRoleJpa.builder()
                    .id(ID)
                    .userId(USER_ID)
                    .role(ROLE)
                    .build();

            UserRoleJpa roleWithoutId = UserRoleJpa.builder()
                    .userId(USER_ID)
                    .role(ROLE)
                    .build();

            assertThat(roleWithId).isNotEqualTo(roleWithoutId);
        }
    }

    @Nested
    @DisplayName("builder instances")
    class BuilderInstances {

        @Test
        @DisplayName("should produce distinct objects on successive builds")
        void shouldProduceDistinctObjectsOnSuccessiveBuilds() {
            UserRoleJpa.UserRoleJpaBuilder builder = UserRoleJpa.builder()
                    .id(ID)
                    .userId(USER_ID)
                    .role(ROLE);

            UserRoleJpa first = builder.build();
            UserRoleJpa second = builder.build();

            assertThat(first).isNotSameAs(second);
        }

        @Test
        @DisplayName("should allow field override on builder")
        void shouldAllowFieldOverrideOnBuilder() {
            UserRoleJpa userRole = UserRoleJpa.builder()
                    .id(ID)
                    .userId(USER_ID)
                    .role(ROLE)
                    .role(Role.TEACHER)
                    .build();

            assertThat(userRole.role()).isEqualTo(Role.TEACHER);
            assertThat(userRole.id()).isEqualTo(ID);
            assertThat(userRole.userId()).isEqualTo(USER_ID);
        }
    }
}