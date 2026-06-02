package edu.univalle.gadim.virtual_lab_platform.users.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.users.api.type.User;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.UserStatus;
import edu.univalle.gadim.virtual_lab_platform.users.data.model.UserJpa;
import edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

@NullMarked
@DisplayName("UserServiceOperation")
class UserServiceOperationUnTest {

  private static final String EMAIL = "ana.martinez@correounivalle.edu.co";
  private static final String ENCODED_PASSWORD = "encoded-hash";

  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  private UserServiceOperation serviceOperation;

  @BeforeEach
  void setUp() {
    userRepository = mock(UserRepository.class);
    passwordEncoder = mock(PasswordEncoder.class);
    serviceOperation = new UserServiceOperation(userRepository, passwordEncoder);
  }

  private UserJpa buildUser(String id, String name, String lastName) {
    return UserJpa.builder()
        .id(id)
        .name(name)
        .lastName(lastName)
        .password(ENCODED_PASSWORD)
        .status(UserStatus.ACTIVE)
        .createdDate(LocalDateTime.of(2025, 1, 15, 10, 30, 0))
        .build();
  }

  private User buildInputUser(String id, String name, String lastName) {
    return UserJpa.builder()
        .id(id)
        .name(name)
        .lastName(lastName)
        .externalCode(null)
        .password("plaintext")
        .status(UserStatus.ACTIVE)
        .build();
  }

  @Nested
  @DisplayName("createUser")
  class CreateUser {

    @Test
    @DisplayName("should set id from input, encode password, set created date, and save via repository")
    void shouldCreateUserWithAllFields() {
      final var inputUser = buildInputUser(EMAIL, "Ana", "Martinez");
      final var savedUser = buildUser(EMAIL, "Ana", "Martinez");

      when(passwordEncoder.encode("plaintext")).thenReturn(ENCODED_PASSWORD);
      when(userRepository.save(any(UserJpa.class))).thenReturn(savedUser);

      final var result = serviceOperation.createUser(inputUser);

      assertThat(result).isEqualTo(savedUser);
      verify(passwordEncoder).encode("plaintext");
      verify(userRepository).save(any(UserJpa.class));
    }

    @Test
    @DisplayName("should handle user without external code")
    void shouldHandleUserWithoutExternalCode() {
      final var inputUser = buildInputUser(EMAIL, "Ana", "Martinez");
      final var savedUser = buildUser(EMAIL, "Ana", "Martinez");

      when(passwordEncoder.encode("plaintext")).thenReturn(ENCODED_PASSWORD);
      when(userRepository.save(any(UserJpa.class))).thenReturn(savedUser);

      final var result = serviceOperation.createUser(inputUser);

      assertThat(result).isEqualTo(savedUser);
    }
  }

  @Nested
  @DisplayName("getUserById")
  class GetUserById {

    @Test
    @DisplayName("should return user when found")
    void shouldReturnUserWhenFound() {
      final var expectedUser = buildUser(EMAIL, "Ana", "Martinez");
      when(userRepository.findById(EMAIL)).thenReturn(Optional.of(expectedUser));

      final var result = serviceOperation.getUserById(EMAIL);

      assertThat(result).isPresent().contains(expectedUser);
    }

    @Test
    @DisplayName("should return empty optional when not found")
    void shouldReturnEmptyWhenNotFound() {
      when(userRepository.findById(anyString())).thenReturn(Optional.empty());

      final var result = serviceOperation.getUserById("nonexistent@correounivalle.edu.co");

      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("getAllUsers")
  class GetAllUsers {

    @Test
    @DisplayName("should return list of all users")
    void shouldReturnAllUsers() {
      final var user1 = buildUser("ana.martinez@correounivalle.edu.co", "Ana", "Martinez");
      final var user2 = buildUser("carlos.lopez@correounivalle.edu.co", "Carlos", "Lopez");
      when(userRepository.findAll()).thenReturn(List.of(user1, user2));

      final var result = serviceOperation.getAllUsers();

      assertThat(result).hasSize(2).containsExactly(user1, user2);
    }

    @Test
    @DisplayName("should return empty list when no users exist")
    void shouldReturnEmptyListWhenNoUsers() {
      when(userRepository.findAll()).thenReturn(List.of());

      final var result = serviceOperation.getAllUsers();

      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("updateUser")
  class UpdateUser {

    @Test
    @DisplayName("should update mutable fields and save")
    void shouldUpdateMutableFieldsAndSave() {
      final var existing = buildUser(EMAIL, "Ana", "Martinez");
      final var updates = buildInputUser(EMAIL, "Maria", "Garcia");

      when(userRepository.findById(EMAIL)).thenReturn(Optional.of(existing));
      when(userRepository.save(any(UserJpa.class))).thenReturn(existing);

      final var result = serviceOperation.updateUser(EMAIL, updates);

      assertThat(result).isEqualTo(existing);
      verify(userRepository).findById(EMAIL);
      verify(userRepository).save(any(UserJpa.class));
    }

    @Test
    @DisplayName("should re-encode password when non-empty")
    void shouldReEncodePasswordWhenNonEmpty() {
      final var existing = buildUser(EMAIL, "Ana", "Martinez");
      final var updates = buildInputUser(EMAIL, "Ana", "Martinez");

      when(userRepository.findById(EMAIL)).thenReturn(Optional.of(existing));
      when(passwordEncoder.encode("plaintext")).thenReturn("new-encoded");
      when(userRepository.save(any(UserJpa.class))).thenReturn(existing);

      serviceOperation.updateUser(EMAIL, updates);

      verify(passwordEncoder).encode("plaintext");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when user not found")
    void shouldThrowWhenUserNotFound() {
      final var updates = buildInputUser(EMAIL, "Ana", "Martinez");
      when(userRepository.findById("nonexistent@correounivalle.edu.co")).thenReturn(Optional.empty());

      assertThatThrownBy(() -> serviceOperation.updateUser("nonexistent@correounivalle.edu.co", updates))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("nonexistent@correounivalle.edu.co");
    }
  }

  @Nested
  @DisplayName("deleteUser")
  class DeleteUser {

    @Test
    @DisplayName("should set status to DELETED when user is INACTIVE")
    void shouldSetStatusToDeletedWhenInactive() {
      final var user = buildUser(EMAIL, "Ana", "Martinez");
      user.setStatus(UserStatus.INACTIVE);

      when(userRepository.findById(EMAIL)).thenReturn(Optional.of(user));
      when(userRepository.save(any(UserJpa.class))).thenReturn(user);

      serviceOperation.deleteUser(EMAIL);

      assertThat(user.status()).isEqualTo(UserStatus.DELETED);
      verify(userRepository).save(user);
    }

    @Test
    @DisplayName("should throw IllegalStateException when user is ACTIVE")
    void shouldThrowWhenUserIsActive() {
      final var user = buildUser(EMAIL, "Ana", "Martinez");
      user.setStatus(UserStatus.ACTIVE);

      when(userRepository.findById(EMAIL)).thenReturn(Optional.of(user));

      assertThatThrownBy(() -> serviceOperation.deleteUser(EMAIL))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("INACTIVE");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when user not found")
    void shouldThrowWhenUserNotFound() {
      when(userRepository.findById("nonexistent@correounivalle.edu.co")).thenReturn(Optional.empty());

      assertThatThrownBy(() -> serviceOperation.deleteUser("nonexistent@correounivalle.edu.co"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("nonexistent@correounivalle.edu.co");
    }
  }
}