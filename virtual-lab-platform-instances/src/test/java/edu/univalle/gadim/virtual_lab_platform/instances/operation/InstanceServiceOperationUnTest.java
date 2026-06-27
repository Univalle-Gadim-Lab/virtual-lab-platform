package edu.univalle.gadim.virtual_lab_platform.instances.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
import edu.univalle.gadim.virtual_lab_platform.instances.api.service.WorkspaceProvisionerService;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.Instance;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceStatus;
import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceJpa;
import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceUserJpa;
import edu.univalle.gadim.virtual_lab_platform.instances.data.repository.InstanceRepository;
import edu.univalle.gadim.virtual_lab_platform.instances.data.repository.InstanceUserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@NullMarked
@DisplayName("InstanceServiceOperation")
class InstanceServiceOperationUnTest {

  private static final String USER_ID = "ana.martinez@correounivalle.edu.co";
  private static final String INSTANCE_ID = "inst-001";
  private static final String NAME = "lab-instance";
  private static final String DESCRIPTION = "KiCad workspace";
  private static final String IMAGE_NAME = "lab-kicad";
  private static final String IMAGE_VERSION = "1.0";
  private static final String IMAGE_REGISTRY = "registry.univalle.edu";
  private static final int CPU_CORES = 4;
  private static final int MEMORY_MB = 8192;
  private static final int STORAGE_MB = 20480;
  private static final boolean GPU_ENABLED = true;
  private static final int EXPOSED_PORT = 8080;
  private static final String CONTAINER_ID = "container-001";

  @Mock private InstanceRepository instanceRepository;

  @Mock private InstanceUserRepository instanceUserRepository;

  @Mock private WorkspaceProvisionerService workspaceProvisionerService;

  @Mock private UniqueIdGenerator uniqueIdGenerator;

  private InstanceServiceOperation serviceOperation;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    serviceOperation =
        new InstanceServiceOperation(
            instanceRepository,
            instanceUserRepository,
            workspaceProvisionerService,
            uniqueIdGenerator);
  }

  private InstanceJpa buildInstance(String id, InstanceStatus status) {
    return InstanceJpa.builder()
        .id(id)
        .name(NAME)
        .externalIp(CONTAINER_ID)
        .imageName(IMAGE_NAME)
        .imageVersion(IMAGE_VERSION)
        .imageRegistry(IMAGE_REGISTRY)
        .cpuCores(CPU_CORES)
        .memoryMb(MEMORY_MB)
        .storageMb(STORAGE_MB)
        .gpuEnabled(GPU_ENABLED)
        .exposedPort(EXPOSED_PORT)
        .vncPassword("testPass123")
        .internalIp("127.0.0.1")
        .createdAt(LocalDateTime.of(2025, 1, 15, 10, 30, 0))
        .expiresAt(LocalDateTime.of(2025, 2, 15, 10, 30, 0))
        .startedAt(LocalDateTime.of(2025, 1, 15, 10, 35, 0))
        .status(status)
        .build();
  }

  @Nested
  @DisplayName("createInstance")
  class CreateInstance {

    @Test
    @DisplayName("should create instance with all fields and save user association")
    void shouldCreateInstanceWithAllFields() {
      // Given
      final var savedInstance = buildInstance(INSTANCE_ID, InstanceStatus.CREATED);
      when(uniqueIdGenerator.generate()).thenReturn(INSTANCE_ID, "iu-001");
      when(workspaceProvisionerService.createWorkspace(
              eq(USER_ID),
              eq(true),
              eq(IMAGE_NAME),
              eq(IMAGE_VERSION),
              eq(CPU_CORES),
              eq(MEMORY_MB),
              eq(STORAGE_MB),
              eq(GPU_ENABLED),
              eq(EXPOSED_PORT),
              anyString()))
          .thenReturn(CONTAINER_ID);
      when(workspaceProvisionerService.getHostVncPort(CONTAINER_ID)).thenReturn(32768);
      when(instanceRepository.save(any(InstanceJpa.class))).thenReturn(savedInstance);
      when(instanceUserRepository.save(any(InstanceUserJpa.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // When
      final var result =
          serviceOperation.createInstance(
              USER_ID,
              NAME,
              Optional.of(DESCRIPTION),
              IMAGE_NAME,
              IMAGE_VERSION,
              IMAGE_REGISTRY,
              CPU_CORES,
              MEMORY_MB,
              STORAGE_MB,
              GPU_ENABLED,
              EXPOSED_PORT);

      // Then
      assertThat(result).isNotNull();
      verify(workspaceProvisionerService)
          .createWorkspace(
              eq(USER_ID),
              eq(true),
              eq(IMAGE_NAME),
              eq(IMAGE_VERSION),
              eq(CPU_CORES),
              eq(MEMORY_MB),
              eq(STORAGE_MB),
              eq(GPU_ENABLED),
              eq(EXPOSED_PORT),
              anyString());
      verify(instanceRepository).save(any(InstanceJpa.class));
      verify(instanceUserRepository).save(any(InstanceUserJpa.class));
    }

    @Test
    @DisplayName("should handle absent description by storing null")
    void shouldHandleAbsentDescription() {
      // Given
      final var savedInstance = buildInstance(INSTANCE_ID, InstanceStatus.CREATED);
      when(uniqueIdGenerator.generate()).thenReturn(INSTANCE_ID, "iu-001");
      when(workspaceProvisionerService.createWorkspace(
              eq(USER_ID),
              eq(true),
              eq(IMAGE_NAME),
              eq(IMAGE_VERSION),
              eq(CPU_CORES),
              eq(MEMORY_MB),
              eq(STORAGE_MB),
              eq(GPU_ENABLED),
              eq(EXPOSED_PORT),
              anyString()))
          .thenReturn(CONTAINER_ID);
      when(workspaceProvisionerService.getHostVncPort(CONTAINER_ID)).thenReturn(32768);
      when(instanceRepository.save(any(InstanceJpa.class))).thenReturn(savedInstance);

      // When
      final var result =
          serviceOperation.createInstance(
              USER_ID,
              NAME,
              Optional.empty(),
              IMAGE_NAME,
              IMAGE_VERSION,
              IMAGE_REGISTRY,
              CPU_CORES,
              MEMORY_MB,
              STORAGE_MB,
              GPU_ENABLED,
              EXPOSED_PORT);

      // Then
      assertThat(result).isNotNull();
    }
  }

  @Nested
  @DisplayName("getInstanceById")
  class GetInstanceById {

    @Test
    @DisplayName("should return instance when found")
    void shouldReturnInstanceWhenFound() {
      // Given
      final var instance = buildInstance(INSTANCE_ID, InstanceStatus.RUNNING);
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.of(instance));

      // When
      final var result = serviceOperation.getInstanceById(INSTANCE_ID);

      // Then
      assertThat(result).isPresent();
      assertThat(result.get()).isInstanceOf(Instance.class);
    }

    @Test
    @DisplayName("should return empty when not found")
    void shouldReturnEmptyWhenNotFound() {
      // Given
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.empty());

      // When
      final var result = serviceOperation.getInstanceById(INSTANCE_ID);

      // Then
      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should return empty when instance is DELETED")
    void shouldReturnEmptyWhenDeleted() {
      // Given
      final var instance = buildInstance(INSTANCE_ID, InstanceStatus.DELETED);
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.of(instance));

      // When
      final var result = serviceOperation.getInstanceById(INSTANCE_ID);

      // Then
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("getInstancesByUserId")
  class GetInstancesByUserId {

    @Test
    @DisplayName("should return list of instances for user")
    void shouldReturnListOfInstances() {
      // Given
      final var instance1 = buildInstance("inst-001", InstanceStatus.RUNNING);
      final var instance2 = buildInstance("inst-002", InstanceStatus.STOPPED);
      when(instanceRepository.findByUserId(USER_ID)).thenReturn(List.of(instance1, instance2));

      // When
      final var result = serviceOperation.getInstancesByUserId(USER_ID);

      // Then
      assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("should return empty list when no instances exist")
    void shouldReturnEmptyListWhenNoneExist() {
      // Given
      when(instanceRepository.findByUserId(USER_ID)).thenReturn(List.of());

      // When
      final var result = serviceOperation.getInstancesByUserId(USER_ID);

      // Then
      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should filter out DELETED instances")
    void shouldFilterOutDeletedInstances() {
      // Given
      final var instance1 = buildInstance("inst-001", InstanceStatus.RUNNING);
      final var instance2 = buildInstance("inst-002", InstanceStatus.DELETED);
      when(instanceRepository.findByUserId(USER_ID)).thenReturn(List.of(instance1, instance2));

      // When
      final var result = serviceOperation.getInstancesByUserId(USER_ID);

      // Then
      assertThat(result).hasSize(1);
      assertThat(result.get(0).id()).isEqualTo("inst-001");
    }
  }

  @Nested
  @DisplayName("startInstance")
  class StartInstance {

    @Test
    @DisplayName("should transition from STOPPED to RUNNING")
    void shouldTransitionFromStoppedToRunning() {
      // Given
      final var instance = buildInstance(INSTANCE_ID, InstanceStatus.STOPPED);
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.of(instance));
      when(instanceRepository.save(any(InstanceJpa.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // When
      final var result = serviceOperation.startInstance(INSTANCE_ID);

      // Then
      assertThat(result.status()).isEqualTo(InstanceStatus.RUNNING);
      verify(workspaceProvisionerService).startWorkspace(CONTAINER_ID);
    }

    @Test
    @DisplayName("should return immediately when already RUNNING")
    void shouldReturnImmediatelyWhenAlreadyRunning() {
      // Given
      final var instance = buildInstance(INSTANCE_ID, InstanceStatus.RUNNING);
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.of(instance));

      // When
      final var result = serviceOperation.startInstance(INSTANCE_ID);

      // Then
      assertThat(result.status()).isEqualTo(InstanceStatus.RUNNING);
      verify(workspaceProvisionerService, never()).startWorkspace(anyString());
    }

    @Test
    @DisplayName("should set STOPPED status when workspace provisioner throws")
    void shouldSetStoppedStatusWhenProvisionerThrows() {
      // Given
      final var instance = buildInstance(INSTANCE_ID, InstanceStatus.CREATED);
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.of(instance));
      doThrow(new RuntimeException("Docker error"))
          .when(workspaceProvisionerService)
          .startWorkspace(CONTAINER_ID);
      when(instanceRepository.save(any(InstanceJpa.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // When
      final var result = serviceOperation.startInstance(INSTANCE_ID);

      // Then
      assertThat(result.status()).isEqualTo(InstanceStatus.STOPPED);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when instance not found")
    void shouldThrowExceptionWhenNotFound() {
      // Given
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.empty());

      // When / Then
      assertThatThrownBy(() -> serviceOperation.startInstance(INSTANCE_ID))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining(INSTANCE_ID);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when instance is DELETED")
    void shouldThrowExceptionWhenDeleted() {
      // Given
      final var instance = buildInstance(INSTANCE_ID, InstanceStatus.DELETED);
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.of(instance));

      // When / Then
      assertThatThrownBy(() -> serviceOperation.startInstance(INSTANCE_ID))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining(INSTANCE_ID);
    }
  }

  @Nested
  @DisplayName("stopInstance")
  class StopInstance {

    @Test
    @DisplayName("should transition to STOPPED and set stoppedAt")
    void shouldTransitionToStopped() {
      // Given
      final var instance = buildInstance(INSTANCE_ID, InstanceStatus.RUNNING);
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.of(instance));
      when(instanceRepository.save(any(InstanceJpa.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // When
      final var result = serviceOperation.stopInstance(INSTANCE_ID);

      // Then
      assertThat(result.status()).isEqualTo(InstanceStatus.STOPPED);
      assertThat(result.stoppedAt()).isPresent();
      verify(workspaceProvisionerService).stopWorkSpace(CONTAINER_ID);
    }

    @Test
    @DisplayName("should return immediately when already STOPPED")
    void shouldReturnImmediatelyWhenAlreadyStopped() {
      // Given
      final var instance = buildInstance(INSTANCE_ID, InstanceStatus.STOPPED);
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.of(instance));

      // When
      final var result = serviceOperation.stopInstance(INSTANCE_ID);

      // Then
      assertThat(result.status()).isEqualTo(InstanceStatus.STOPPED);
      verify(workspaceProvisionerService, never()).stopWorkSpace(anyString());
    }

    @Test
    @DisplayName("should swallow provisioner exceptions and still save")
    void shouldSwallowProvisionerExceptions() {
      // Given
      final var instance = buildInstance(INSTANCE_ID, InstanceStatus.RUNNING);
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.of(instance));
      doThrow(new RuntimeException("Docker error"))
          .when(workspaceProvisionerService)
          .stopWorkSpace(CONTAINER_ID);
      when(instanceRepository.save(any(InstanceJpa.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // When
      final var result = serviceOperation.stopInstance(INSTANCE_ID);

      // Then
      assertThat(result.status()).isEqualTo(InstanceStatus.STOPPED);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when instance not found")
    void shouldThrowExceptionWhenNotFound() {
      // Given
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.empty());

      // When / Then
      assertThatThrownBy(() -> serviceOperation.stopInstance(INSTANCE_ID))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining(INSTANCE_ID);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when instance is DELETED")
    void shouldThrowExceptionWhenDeleted() {
      // Given
      final var instance = buildInstance(INSTANCE_ID, InstanceStatus.DELETED);
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.of(instance));

      // When / Then
      assertThatThrownBy(() -> serviceOperation.stopInstance(INSTANCE_ID))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining(INSTANCE_ID);
    }
  }

  @Nested
  @DisplayName("deleteInstance")
  class DeleteInstance {

    @Test
    @DisplayName("should mark STOPPED instance as DELETED")
    void shouldMarkStoppedInstanceAsDeleted() {
      // Given
      final var instance = buildInstance(INSTANCE_ID, InstanceStatus.STOPPED);
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.of(instance));
      when(instanceRepository.save(any(InstanceJpa.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // When
      serviceOperation.deleteInstance(INSTANCE_ID);

      // Then
      verify(instanceRepository).save(any(InstanceJpa.class));
      verify(instanceUserRepository, never()).findByInstanceId(anyString());
    }

    @Test
    @DisplayName("should throw IllegalStateException when instance is RUNNING")
    void shouldThrowIllegalStateExceptionWhenRunning() {
      // Given
      final var instance = buildInstance(INSTANCE_ID, InstanceStatus.RUNNING);
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.of(instance));

      // When / Then
      assertThatThrownBy(() -> serviceOperation.deleteInstance(INSTANCE_ID))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("STOPPED")
          .hasMessageContaining("RUNNING");
    }

    @Test
    @DisplayName("should throw IllegalStateException when instance is CREATED")
    void shouldThrowIllegalStateExceptionWhenCreated() {
      // Given
      final var instance = buildInstance(INSTANCE_ID, InstanceStatus.CREATED);
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.of(instance));

      // When / Then
      assertThatThrownBy(() -> serviceOperation.deleteInstance(INSTANCE_ID))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("STOPPED");
    }

    @Test
    @DisplayName("should not delete instance_user associations")
    void shouldNotDeleteInstanceUserAssociations() {
      // Given
      final var instance = buildInstance(INSTANCE_ID, InstanceStatus.STOPPED);
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.of(instance));
      when(instanceRepository.save(any(InstanceJpa.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // When
      serviceOperation.deleteInstance(INSTANCE_ID);

      // Then
      verify(instanceUserRepository, never()).findByInstanceId(anyString());
      verify(instanceUserRepository, never()).delete(any());
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when instance not found")
    void shouldThrowExceptionWhenNotFound() {
      // Given
      when(instanceRepository.findById(INSTANCE_ID)).thenReturn(Optional.empty());

      // When / Then
      assertThatThrownBy(() -> serviceOperation.deleteInstance(INSTANCE_ID))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining(INSTANCE_ID);
    }
  }

  @Nested
  @DisplayName("checkOwnership")
  class CheckOwnership {

    @Test
    @DisplayName("should return true when user is associated with instance")
    void shouldReturnTrueWhenAssociated() {
      // Given
      final var association =
          InstanceUserJpa.builder().id("iu-001").instanceId(INSTANCE_ID).userId(USER_ID).build();
      when(instanceUserRepository.findByInstanceId(INSTANCE_ID))
          .thenReturn(List.of(association));

      // When
      final var result = serviceOperation.checkOwnership(INSTANCE_ID, USER_ID);

      // Then
      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should return false when user is not associated with instance")
    void shouldReturnFalseWhenNotAssociated() {
      // Given
      final var association =
          InstanceUserJpa.builder()
              .id("iu-001")
              .instanceId(INSTANCE_ID)
              .userId("other-user")
              .build();
      when(instanceUserRepository.findByInstanceId(INSTANCE_ID))
          .thenReturn(List.of(association));

      // When
      final var result = serviceOperation.checkOwnership(INSTANCE_ID, USER_ID);

      // Then
      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should return false when no associations exist")
    void shouldReturnFalseWhenNoAssociations() {
      // Given
      when(instanceUserRepository.findByInstanceId(INSTANCE_ID)).thenReturn(List.of());

      // When
      final var result = serviceOperation.checkOwnership(INSTANCE_ID, USER_ID);

      // Then
      assertThat(result).isFalse();
    }
  }
}
