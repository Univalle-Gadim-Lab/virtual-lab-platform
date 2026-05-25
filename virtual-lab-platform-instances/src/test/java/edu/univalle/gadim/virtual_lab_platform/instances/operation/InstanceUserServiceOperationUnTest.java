package edu.univalle.gadim.virtual_lab_platform.instances.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceUser;
import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceUserJpa;
import edu.univalle.gadim.virtual_lab_platform.instances.data.repository.InstanceUserRepository;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@NullMarked
@DisplayName("InstanceUserServiceOperation")
class InstanceUserServiceOperationUnTest {

  private static final String USER_ASSOCIATION_ID = "iu-001";
  private static final String USER_ID = "user-001";
  private static final String INSTANCE_ID = "inst-001";

  @Mock private InstanceUserRepository instanceUserRepository;

  @Mock private UniqueIdGenerator uniqueIdGenerator;

  private InstanceUserServiceOperation serviceOperation;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    serviceOperation = new InstanceUserServiceOperation(instanceUserRepository, uniqueIdGenerator);
  }

  private InstanceUserJpa buildInstanceUser(String id, String instanceId, String userId) {
    return InstanceUserJpa.builder().id(id).instanceId(instanceId).userId(userId).build();
  }

  @Nested
  @DisplayName("assignUserToInstance")
  class AssignUserToInstance {

    @Test
    @DisplayName("should generate id, save, and return association")
    void shouldAssignUserToInstance() {
      // Given
      final var savedAssociation = buildInstanceUser(USER_ASSOCIATION_ID, INSTANCE_ID, USER_ID);
      when(uniqueIdGenerator.generate()).thenReturn(USER_ASSOCIATION_ID);
      when(instanceUserRepository.save(any(InstanceUserJpa.class))).thenReturn(savedAssociation);

      // When
      final var result = serviceOperation.assignUserToInstance(USER_ID, INSTANCE_ID);

      // Then
      assertThat(result).isNotNull();
      assertThat(result.id()).isEqualTo(USER_ASSOCIATION_ID);
      assertThat(result.instanceId()).isEqualTo(INSTANCE_ID);
      assertThat(result.userId()).isEqualTo(USER_ID);
      assertThat(result).isInstanceOf(InstanceUser.class);
      verify(uniqueIdGenerator).generate();
      verify(instanceUserRepository).save(any(InstanceUserJpa.class));
    }
  }

  @Nested
  @DisplayName("getUsersByInstanceId")
  class GetUsersByInstanceId {

    @Test
    @DisplayName("should return list of user associations for instance")
    void shouldReturnListOfAssociations() {
      // Given
      final var user1 = buildInstanceUser("iu-001", INSTANCE_ID, "user-001");
      final var user2 = buildInstanceUser("iu-002", INSTANCE_ID, "user-002");
      when(instanceUserRepository.findByInstanceId(INSTANCE_ID)).thenReturn(List.of(user1, user2));

      // When
      final var result = serviceOperation.getUsersByInstanceId(INSTANCE_ID);

      // Then
      assertThat(result).hasSize(2);
      assertThat(result.get(0)).isInstanceOf(InstanceUser.class);
    }

    @Test
    @DisplayName("should return empty list when no associations exist")
    void shouldReturnEmptyListWhenNoneExist() {
      // Given
      when(instanceUserRepository.findByInstanceId(INSTANCE_ID)).thenReturn(List.of());

      // When
      final var result = serviceOperation.getUsersByInstanceId(INSTANCE_ID);

      // Then
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("getInstancesByUserId")
  class GetInstancesByUserId {

    @Test
    @DisplayName("should return list of instance associations for user")
    void shouldReturnListOfAssociations() {
      // Given
      final var inst1 = buildInstanceUser("iu-001", "inst-001", USER_ID);
      final var inst2 = buildInstanceUser("iu-002", "inst-002", USER_ID);
      when(instanceUserRepository.findByUserId(USER_ID)).thenReturn(List.of(inst1, inst2));

      // When
      final var result = serviceOperation.getInstancesByUserId(USER_ID);

      // Then
      assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("should return empty list when no associations exist")
    void shouldReturnEmptyListWhenNoneExist() {
      // Given
      when(instanceUserRepository.findByUserId(USER_ID)).thenReturn(List.of());

      // When
      final var result = serviceOperation.getInstancesByUserId(USER_ID);

      // Then
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("removeUserFromInstance")
  class RemoveUserFromInstance {

    @Test
    @DisplayName("should delete matching associations")
    void shouldDeleteMatchingAssociations() {
      // Given
      final var association = buildInstanceUser("iu-001", INSTANCE_ID, USER_ID);
      when(instanceUserRepository.findByInstanceId(INSTANCE_ID)).thenReturn(List.of(association));

      // When
      serviceOperation.removeUserFromInstance(USER_ID, INSTANCE_ID);

      // Then
      verify(instanceUserRepository).delete(association);
    }

    @Test
    @DisplayName("should do nothing when no matching association exists")
    void shouldDoNothingWhenNoMatch() {
      // Given
      final var otherAssociation = buildInstanceUser("iu-001", INSTANCE_ID, "other-user");
      when(instanceUserRepository.findByInstanceId(INSTANCE_ID))
          .thenReturn(List.of(otherAssociation));

      // When
      serviceOperation.removeUserFromInstance(USER_ID, INSTANCE_ID);

      // Then
      verify(instanceUserRepository).findByInstanceId(INSTANCE_ID);
    }
  }
}
