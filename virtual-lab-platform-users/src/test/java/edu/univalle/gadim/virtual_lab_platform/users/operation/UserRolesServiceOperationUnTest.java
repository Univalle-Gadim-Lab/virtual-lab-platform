package edu.univalle.gadim.virtual_lab_platform.users.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus;
import edu.univalle.gadim.virtual_lab_platform.users.data.model.UserJpa;
import edu.univalle.gadim.virtual_lab_platform.users.data.model.UserRoleJpa;
import edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRepository;
import edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRoleRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@NullMarked
@DisplayName("UserRolesServiceOperation")
class UserRolesServiceOperationUnTest {

  private static final String USER_ID = "user-001";
  private static final String GENERATED_ROLE_ID = "role-id-001";

  private UniqueIdGenerator idGenerator;
  private UserRepository userRepository;
  private UserRoleRepository userRoleRepository;
  private UserRolesServiceOperation serviceOperation;

  @BeforeEach
  void setUp() {
    idGenerator = mock(UniqueIdGenerator.class);
    userRepository = mock(UserRepository.class);
    userRoleRepository = mock(UserRoleRepository.class);
    serviceOperation =
        new UserRolesServiceOperation(idGenerator, userRepository, userRoleRepository);
  }

  private UserJpa buildUser() {
    return UserJpa.builder()
        .id(USER_ID)
        .name("Ana")
        .lastName("Martinez")
        .password("encoded")
        .status(UserStatus.ACTIVE)
        .createdDate(LocalDateTime.of(2025, 1, 15, 10, 30, 0))
        .build();
  }

  private UserRoleJpa buildUserRole(String id, Role role) {
    return UserRoleJpa.builder().id(id).userId(USER_ID).role(role).build();
  }

  @Nested
  @DisplayName("createUserRole")
  class CreateUserRole {

    @Test
    @DisplayName("should create role when user exists")
    void shouldCreateRoleWhenUserExists() {
      // Given
      final var user = buildUser();
      final var expectedRole = buildUserRole(GENERATED_ROLE_ID, Role.STUDENT);

      when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
      when(idGenerator.generate()).thenReturn(GENERATED_ROLE_ID);
      when(userRoleRepository.save(any(UserRoleJpa.class))).thenReturn(expectedRole);

      // When
      final var result = serviceOperation.createUserRole(USER_ID, Role.STUDENT);

      // Then
      assertThat(result).isEqualTo(expectedRole);
      verify(userRepository).findById(USER_ID);
      verify(idGenerator).generate();
      verify(userRoleRepository).save(any(UserRoleJpa.class));
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when user does not exist")
    void shouldThrowExceptionWhenUserDoesNotExist() {
      // Given
      when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

      // When / Then
      final ThrowingCallable action =
          () -> serviceOperation.createUserRole("nonexistent", Role.STUDENT);
      assertThatThrownBy(action)
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("nonexistent")
          .hasMessageContaining("does not exist");
    }
  }

  @Nested
  @DisplayName("createUserRoles")
  class CreateUserRoles {

    @Test
    @DisplayName("should create all roles when user exists")
    void shouldCreateAllRolesWhenUserExists() {
      // Given
      final var user = buildUser();
      final var roles = List.of(Role.STUDENT, Role.TEACHER);
      final var role1 = buildUserRole("role-1", Role.STUDENT);
      final var role2 = buildUserRole("role-2", Role.TEACHER);

      when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
      when(idGenerator.generate()).thenReturn("role-1", "role-2");
      when(userRoleRepository.saveAll(any())).thenReturn(List.of(role1, role2));

      // When
      final var result = serviceOperation.createUserRoles(USER_ID, roles);

      // Then
      assertThat(result).hasSize(2).containsExactly(role1, role2);
      verify(userRepository).findById(USER_ID);
      verify(userRoleRepository).saveAll(any());
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when user does not exist")
    void shouldThrowExceptionWhenUserDoesNotExist() {
      // Given
      when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());
      final var roles = List.of(Role.STUDENT);

      // When / Then
      final ThrowingCallable action = () -> serviceOperation.createUserRoles("nonexistent", roles);
      assertThatThrownBy(action)
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("nonexistent")
          .hasMessageContaining("does not exist");
    }
  }

  @Nested
  @DisplayName("getRoleByUserId")
  class GetRoleByUserId {

    @Test
    @DisplayName("should return roles when found")
    void shouldReturnRolesWhenFound() {
      // Given
      final var role1 = buildUserRole("role-1", Role.STUDENT);
      final var role2 = buildUserRole("role-2", Role.TEACHER);
      when(userRoleRepository.findByUserId(USER_ID)).thenReturn(List.of(role1, role2));

      // When
      final var result = serviceOperation.getRoleByUserId(USER_ID);

      // Then
      assertThat(result).hasSize(2).containsExactly(role1, role2);
    }

    @Test
    @DisplayName("should return empty list when no roles assigned")
    void shouldReturnEmptyListWhenNoRoles() {
      // Given
      when(userRoleRepository.findByUserId(USER_ID)).thenReturn(List.of());

      // When
      final var result = serviceOperation.getRoleByUserId(USER_ID);

      // Then
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("deleteUserRole")
  class DeleteUserRole {

    @Test
    @DisplayName("should delete role when it exists")
    void shouldDeleteRoleWhenExists() {
      // Given
      final var roleId = "role-to-delete";
      when(userRoleRepository.existsById(roleId)).thenReturn(true);

      // When
      serviceOperation.deleteUserRole(roleId);

      // Then
      verify(userRoleRepository).existsById(roleId);
      verify(userRoleRepository).deleteById(roleId);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when role not found")
    void shouldThrowWhenRoleNotFound() {
      // Given
      when(userRoleRepository.existsById("nonexistent")).thenReturn(false);

      // When / Then
      assertThatThrownBy(() -> serviceOperation.deleteUserRole("nonexistent"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("nonexistent");
    }
  }
}
