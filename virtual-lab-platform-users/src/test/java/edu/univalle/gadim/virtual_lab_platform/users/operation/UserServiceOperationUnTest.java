package edu.univalle.gadim.virtual_lab_platform.users.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
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

  private static final String GENERATED_ID = "generated-id-001";
  private static final String RAW_PASSWORD = "plaintext";
  private static final String ENCODED_PASSWORD = "encoded-hash";

  private UniqueIdGenerator idGenerator;
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  private UserServiceOperation serviceOperation;

  @BeforeEach
  void setUp() {
    idGenerator = mock(UniqueIdGenerator.class);
    userRepository = mock(UserRepository.class);
    passwordEncoder = mock(PasswordEncoder.class);
    serviceOperation = new UserServiceOperation(idGenerator, userRepository, passwordEncoder);
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

  private User buildInputUser(String name, String lastName) {
    return UserJpa.builder()
        .name(name)
        .lastName(lastName)
        .externalCode(null)
        .password(RAW_PASSWORD)
        .status(UserStatus.ACTIVE)
        .build();
  }

  @Nested
  @DisplayName("createUser")
  class CreateUser {

    @Test
    @DisplayName(
        "should generate id, encode password, set created date, save via repository, and return saved user")
    void shouldCreateUserWithAllFields() {
      // Given
      final var inputUser = buildInputUser("Ana", "Martinez");
      final var savedUser = buildUser(GENERATED_ID, "Ana", "Martinez");

      when(idGenerator.generate()).thenReturn(GENERATED_ID);
      when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(ENCODED_PASSWORD);
      when(userRepository.save(any(UserJpa.class))).thenReturn(savedUser);

      // When
      final var result = serviceOperation.createUser(inputUser);

      // Then
      assertThat(result).isEqualTo(savedUser);
      verify(idGenerator).generate();
      verify(passwordEncoder).encode(RAW_PASSWORD);
      verify(userRepository).save(any(UserJpa.class));
    }

    @Test
    @DisplayName("should handle user without external code")
    void shouldHandleUserWithoutExternalCode() {
      // Given
      final var inputUser = buildInputUser("Ana", "Martinez");
      final var savedUser = buildUser(GENERATED_ID, "Ana", "Martinez");

      when(idGenerator.generate()).thenReturn(GENERATED_ID);
      when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(ENCODED_PASSWORD);
      when(userRepository.save(any(UserJpa.class))).thenReturn(savedUser);

      // When
      final var result = serviceOperation.createUser(inputUser);

      // Then
      assertThat(result).isEqualTo(savedUser);
    }
  }

  @Nested
  @DisplayName("getUserById")
  class GetUserById {

    @Test
    @DisplayName("should return user when found")
    void shouldReturnUserWhenFound() {
      // Given
      final var expectedUser = buildUser(GENERATED_ID, "Ana", "Martinez");
      when(userRepository.findById(GENERATED_ID)).thenReturn(Optional.of(expectedUser));

      // When
      final var result = serviceOperation.getUserById(GENERATED_ID);

      // Then
      assertThat(result).isPresent().contains(expectedUser);
    }

    @Test
    @DisplayName("should return empty optional when not found")
    void shouldReturnEmptyWhenNotFound() {
      // Given
      when(userRepository.findById(anyString())).thenReturn(Optional.empty());

      // When
      final var result = serviceOperation.getUserById("nonexistent");

      // Then
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("getUserByUsername")
  class GetUserByUsername {

    @Test
    @DisplayName("should return user when found")
    void shouldReturnUserWhenFound() {
      // Given
      final var expectedUser = buildUser(GENERATED_ID, "Ana", "Martinez");
      when(userRepository.findByName("Ana")).thenReturn(Optional.of(expectedUser));

      // When
      final var result = serviceOperation.getUserByUsername("Ana");

      // Then
      assertThat(result).isPresent().contains(expectedUser);
    }

    @Test
    @DisplayName("should return empty optional when not found")
    void shouldReturnEmptyWhenNotFound() {
      // Given
      when(userRepository.findByName(anyString())).thenReturn(Optional.empty());

      // When
      final var result = serviceOperation.getUserByUsername("nonexistent");

      // Then
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("getAllUsers")
  class GetAllUsers {

    @Test
    @DisplayName("should return list of all users")
    void shouldReturnAllUsers() {
      // Given
      final var user1 = buildUser("id-1", "Ana", "Martinez");
      final var user2 = buildUser("id-2", "Carlos", "Lopez");
      when(userRepository.findAll()).thenReturn(List.of(user1, user2));

      // When
      final var result = serviceOperation.getAllUsers();

      // Then
      assertThat(result).hasSize(2).containsExactly(user1, user2);
    }

    @Test
    @DisplayName("should return empty list when no users exist")
    void shouldReturnEmptyListWhenNoUsers() {
      // Given
      when(userRepository.findAll()).thenReturn(List.of());

      // When
      final var result = serviceOperation.getAllUsers();

      // Then
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("updateUser")
  class UpdateUser {

    @Test
    @DisplayName("should update mutable fields and save")
    void shouldUpdateMutableFieldsAndSave() {
      // Given
      final var existing = buildUser(GENERATED_ID, "Ana", "Martinez");
      final var updates = buildInputUser("Maria", "Garcia");

      when(userRepository.findById(GENERATED_ID)).thenReturn(Optional.of(existing));
      when(userRepository.save(any(UserJpa.class))).thenReturn(existing);

      // When
      final var result = serviceOperation.updateUser(GENERATED_ID, updates);

      // Then
      assertThat(result).isEqualTo(existing);
      verify(userRepository).findById(GENERATED_ID);
      verify(userRepository).save(any(UserJpa.class));
    }

    @Test
    @DisplayName("should re-encode password when non-empty")
    void shouldReEncodePasswordWhenNonEmpty() {
      // Given
      final var existing = buildUser(GENERATED_ID, "Ana", "Martinez");
      final var updates = buildInputUser("Ana", "Martinez");

      when(userRepository.findById(GENERATED_ID)).thenReturn(Optional.of(existing));
      when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn("new-encoded");
      when(userRepository.save(any(UserJpa.class))).thenReturn(existing);

      // When
      serviceOperation.updateUser(GENERATED_ID, updates);

      // Then
      verify(passwordEncoder).encode(RAW_PASSWORD);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when user not found")
    void shouldThrowWhenUserNotFound() {
      // Given
      final var updates = buildInputUser("Ana", "Martinez");
      when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

      // When / Then
      assertThatThrownBy(() -> serviceOperation.updateUser("nonexistent", updates))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("nonexistent");
    }
  }

  @Nested
  @DisplayName("deleteUser")
  class DeleteUser {

    @Test
    @DisplayName("should set status to DELETED when user is INACTIVE")
    void shouldSetStatusToDeletedWhenInactive() {
      // Given
      final var user = buildUser(GENERATED_ID, "Ana", "Martinez");
      user.setStatus(UserStatus.INACTIVE);

      when(userRepository.findById(GENERATED_ID)).thenReturn(Optional.of(user));
      when(userRepository.save(any(UserJpa.class))).thenReturn(user);

      // When
      serviceOperation.deleteUser(GENERATED_ID);

      // Then
      assertThat(user.status()).isEqualTo(UserStatus.DELETED);
      verify(userRepository).save(user);
    }

    @Test
    @DisplayName("should throw IllegalStateException when user is ACTIVE")
    void shouldThrowWhenUserIsActive() {
      // Given
      final var user = buildUser(GENERATED_ID, "Ana", "Martinez");
      user.setStatus(UserStatus.ACTIVE);

      when(userRepository.findById(GENERATED_ID)).thenReturn(Optional.of(user));

      // When / Then
      assertThatThrownBy(() -> serviceOperation.deleteUser(GENERATED_ID))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("INACTIVE");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when user not found")
    void shouldThrowWhenUserNotFound() {
      // Given
      when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

      // When / Then
      assertThatThrownBy(() -> serviceOperation.deleteUser("nonexistent"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("nonexistent");
    }
  }
}
