package edu.univalle.gadim.virtual_lab_platform.instances.data.model;

import static org.assertj.core.api.Assertions.assertThat;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.Instance;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceStatus;
import java.time.LocalDateTime;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@NullMarked
@DisplayName("InstanceJpa")
class InstanceJpaUnTest {

  private static final String ID = "inst-001";
  private static final String NAME = "lab-instance";
  private static final String DESCRIPTION = "KiCad workspace";
  private static final String EXTERNAL_IP = "172.17.0.5";
  private static final String IMAGE_NAME = "lab-kicad";
  private static final String IMAGE_VERSION = "1.0";
  private static final String IMAGE_REGISTRY = "registry.univalle.edu";
  private static final Integer CPU_CORES = 4;
  private static final Integer MEMORY_MB = 8192;
  private static final Integer STORAGE_MB = 20480;
  private static final Boolean GPU_ENABLED = true;
  private static final Integer EXPOSED_PORT = 8080;
  private static final Integer VNC_PORT = 6901;
  private static final Boolean VNC_ENABLED = true;
  private static final String VNC_PASSWORD = "testPass123";
  private static final String INTERNAL_IP = "10.0.0.5";
  private static final LocalDateTime CREATED_AT = LocalDateTime.of(2025, 1, 15, 10, 30, 0);
  private static final LocalDateTime EXPIRES_AT = LocalDateTime.of(2025, 2, 15, 10, 30, 0);
  private static final LocalDateTime STARTED_AT = LocalDateTime.of(2025, 1, 15, 10, 35, 0);
  private static final LocalDateTime STOPPED_AT = LocalDateTime.of(2025, 1, 20, 18, 0, 0);
  private static final LocalDateTime DELETED_AT = LocalDateTime.of(2025, 1, 21, 0, 0, 0);
  private static final LocalDateTime LAST_ACCESSED_AT = LocalDateTime.of(2025, 1, 19, 14, 30, 0);
  private static final InstanceStatus STATUS = InstanceStatus.RUNNING;

  @Nested
  @DisplayName("no-args constructor")
  class NoArgsConstructor {

    @Test
    @DisplayName("should create instance with null fields")
    void shouldCreateInstanceWithNullFields() {
      final var instance = new InstanceJpa();

      assertThat(instance.getId()).isNull();
      assertThat(instance.getName()).isNull();
      assertThat(instance.getDescription()).isNull();
      assertThat(instance.getExternalIp()).isNull();
      assertThat(instance.getImageName()).isNull();
      assertThat(instance.getImageVersion()).isNull();
      assertThat(instance.getImageRegistry()).isNull();
      assertThat(instance.getCpuCores()).isNull();
      assertThat(instance.getMemoryMb()).isNull();
      assertThat(instance.getStorageMb()).isNull();
      assertThat(instance.getGpuEnabled()).isNull();
      assertThat(instance.getExposedPort()).isNull();
      assertThat(instance.getVncPort()).isNull();
      assertThat(instance.getVncEnabled()).isNull();
      assertThat(instance.getVncPassword()).isNull();
      assertThat(instance.getInternalIp()).isNull();
      assertThat(instance.getCreatedAt()).isNull();
      assertThat(instance.getExpiresAt()).isNull();
      assertThat(instance.getStartedAt()).isNull();
      assertThat(instance.getStoppedAt()).isNull();
      assertThat(instance.getDeletedAt()).isNull();
      assertThat(instance.getLastAccessedAt()).isNull();
      assertThat(instance.getStatus()).isNull();
    }
  }

  @Nested
  @DisplayName("all-args constructor")
  class AllArgsConstructor {

    @Test
    @DisplayName("should populate all fields")
    void shouldPopulateAllFields() {
      final var instance =
          new InstanceJpa(
              ID,
              NAME,
              DESCRIPTION,
              EXTERNAL_IP,
              IMAGE_NAME,
              IMAGE_VERSION,
              IMAGE_REGISTRY,
              CPU_CORES,
              MEMORY_MB,
              STORAGE_MB,
              GPU_ENABLED,
              EXPOSED_PORT,
              INTERNAL_IP,
              VNC_PORT,
              VNC_ENABLED,
              VNC_PASSWORD,
              CREATED_AT,
              EXPIRES_AT,
              STARTED_AT,
              STOPPED_AT,
              DELETED_AT,
              LAST_ACCESSED_AT,
              STATUS);

      assertThat(instance)
          .returns(ID, InstanceJpa::getId)
          .returns(NAME, InstanceJpa::getName)
          .returns(DESCRIPTION, InstanceJpa::getDescription)
          .returns(EXTERNAL_IP, InstanceJpa::getExternalIp)
          .returns(IMAGE_NAME, InstanceJpa::getImageName)
          .returns(IMAGE_VERSION, InstanceJpa::getImageVersion)
          .returns(IMAGE_REGISTRY, InstanceJpa::getImageRegistry)
          .returns(CPU_CORES, InstanceJpa::getCpuCores)
          .returns(MEMORY_MB, InstanceJpa::getMemoryMb)
          .returns(STORAGE_MB, InstanceJpa::getStorageMb)
          .returns(GPU_ENABLED, InstanceJpa::getGpuEnabled)
          .returns(EXPOSED_PORT, InstanceJpa::getExposedPort)
          .returns(VNC_PORT, InstanceJpa::getVncPort)
          .returns(VNC_ENABLED, InstanceJpa::getVncEnabled)
          .returns(VNC_PASSWORD, InstanceJpa::getVncPassword)
          .returns(INTERNAL_IP, InstanceJpa::getInternalIp)
          .returns(CREATED_AT, InstanceJpa::getCreatedAt)
          .returns(EXPIRES_AT, InstanceJpa::getExpiresAt)
          .returns(STARTED_AT, InstanceJpa::getStartedAt)
          .returns(STOPPED_AT, InstanceJpa::getStoppedAt)
          .returns(DELETED_AT, InstanceJpa::getDeletedAt)
          .returns(LAST_ACCESSED_AT, InstanceJpa::getLastAccessedAt)
          .returns(STATUS, InstanceJpa::getStatus);
    }
  }

  @Nested
  @DisplayName("setters")
  class Setters {

    @Test
    @DisplayName("should update id")
    void shouldUpdateId() {
      final var instance = new InstanceJpa();
      instance.setId(ID);

      assertThat(instance.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("should update name")
    void shouldUpdateName() {
      final var instance = new InstanceJpa();
      instance.setName(NAME);

      assertThat(instance.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("should update description")
    void shouldUpdateDescription() {
      final var instance = new InstanceJpa();
      instance.setDescription(DESCRIPTION);

      assertThat(instance.getDescription()).isEqualTo(DESCRIPTION);
    }

    @Test
    @DisplayName("should update externalIp")
    void shouldUpdateExternalIp() {
      final var instance = new InstanceJpa();
      instance.setExternalIp(EXTERNAL_IP);

      assertThat(instance.getExternalIp()).isEqualTo(EXTERNAL_IP);
    }

    @Test
    @DisplayName("should update imageName")
    void shouldUpdateImageName() {
      final var instance = new InstanceJpa();
      instance.setImageName(IMAGE_NAME);

      assertThat(instance.getImageName()).isEqualTo(IMAGE_NAME);
    }

    @Test
    @DisplayName("should update imageVersion")
    void shouldUpdateImageVersion() {
      final var instance = new InstanceJpa();
      instance.setImageVersion(IMAGE_VERSION);

      assertThat(instance.getImageVersion()).isEqualTo(IMAGE_VERSION);
    }

    @Test
    @DisplayName("should update imageRegistry")
    void shouldUpdateImageRegistry() {
      final var instance = new InstanceJpa();
      instance.setImageRegistry(IMAGE_REGISTRY);

      assertThat(instance.getImageRegistry()).isEqualTo(IMAGE_REGISTRY);
    }

    @Test
    @DisplayName("should update cpuCores")
    void shouldUpdateCpuCores() {
      final var instance = new InstanceJpa();
      instance.setCpuCores(CPU_CORES);

      assertThat(instance.getCpuCores()).isEqualTo(CPU_CORES);
    }

    @Test
    @DisplayName("should update memoryMb")
    void shouldUpdateMemoryMb() {
      final var instance = new InstanceJpa();
      instance.setMemoryMb(MEMORY_MB);

      assertThat(instance.getMemoryMb()).isEqualTo(MEMORY_MB);
    }

    @Test
    @DisplayName("should update storageMb")
    void shouldUpdateStorageMb() {
      final var instance = new InstanceJpa();
      instance.setStorageMb(STORAGE_MB);

      assertThat(instance.getStorageMb()).isEqualTo(STORAGE_MB);
    }

    @Test
    @DisplayName("should update gpuEnabled")
    void shouldUpdateGpuEnabled() {
      final var instance = new InstanceJpa();
      instance.setGpuEnabled(GPU_ENABLED);

      assertThat(instance.getGpuEnabled()).isEqualTo(GPU_ENABLED);
    }

    @Test
    @DisplayName("should update exposedPort")
    void shouldUpdateExposedPort() {
      final var instance = new InstanceJpa();
      instance.setExposedPort(EXPOSED_PORT);

      assertThat(instance.getExposedPort()).isEqualTo(EXPOSED_PORT);
    }

    @Test
    @DisplayName("should update vncEnabled")
    void shouldUpdateVncEnabled() {
      final var instance = new InstanceJpa();
      instance.setVncEnabled(VNC_ENABLED);

      assertThat(instance.getVncEnabled()).isEqualTo(VNC_ENABLED);
    }

    @Test
    @DisplayName("should update vncPassword")
    void shouldUpdateVncPassword() {
      final var instance = new InstanceJpa();
      instance.setVncPassword(VNC_PASSWORD);

      assertThat(instance.getVncPassword()).isEqualTo(VNC_PASSWORD);
    }

    @Test
    @DisplayName("should update internalIp")
    void shouldUpdateInternalIp() {
      final var instance = new InstanceJpa();
      instance.setInternalIp(INTERNAL_IP);

      assertThat(instance.getInternalIp()).isEqualTo(INTERNAL_IP);
    }

    @Test
    @DisplayName("should update createdAt")
    void shouldUpdateCreatedAt() {
      final var instance = new InstanceJpa();
      instance.setCreatedAt(CREATED_AT);

      assertThat(instance.getCreatedAt()).isEqualTo(CREATED_AT);
    }

    @Test
    @DisplayName("should update expiresAt")
    void shouldUpdateExpiresAt() {
      final var instance = new InstanceJpa();
      instance.setExpiresAt(EXPIRES_AT);

      assertThat(instance.getExpiresAt()).isEqualTo(EXPIRES_AT);
    }

    @Test
    @DisplayName("should update startedAt")
    void shouldUpdateStartedAt() {
      final var instance = new InstanceJpa();
      instance.setStartedAt(STARTED_AT);

      assertThat(instance.getStartedAt()).isEqualTo(STARTED_AT);
    }

    @Test
    @DisplayName("should update stoppedAt")
    void shouldUpdateStoppedAt() {
      final var instance = new InstanceJpa();
      instance.setStoppedAt(STOPPED_AT);

      assertThat(instance.getStoppedAt()).isEqualTo(STOPPED_AT);
    }

    @Test
    @DisplayName("should update deletedAt")
    void shouldUpdateDeletedAt() {
      final var instance = new InstanceJpa();
      instance.setDeletedAt(DELETED_AT);

      assertThat(instance.getDeletedAt()).isEqualTo(DELETED_AT);
    }

    @Test
    @DisplayName("should update lastAccessedAt")
    void shouldUpdateLastAccessedAt() {
      final var instance = new InstanceJpa();
      instance.setLastAccessedAt(LAST_ACCESSED_AT);

      assertThat(instance.getLastAccessedAt()).isEqualTo(LAST_ACCESSED_AT);
    }

    @Test
    @DisplayName("should update status")
    void shouldUpdateStatus() {
      final var instance = new InstanceJpa();
      instance.setStatus(STATUS);

      assertThat(instance.getStatus()).isEqualTo(STATUS);
    }
  }

  @Nested
  @DisplayName("toString")
  class ToString {

    @Test
    @DisplayName("should contain class name and field values")
    void shouldContainClassNameAndFieldValues() {
      final var instance =
          InstanceJpa.builder()
              .id(ID)
              .name(NAME)
              .description(DESCRIPTION)
              .externalIp(EXTERNAL_IP)
              .imageName(IMAGE_NAME)
              .imageVersion(IMAGE_VERSION)
              .imageRegistry(IMAGE_REGISTRY)
              .cpuCores(CPU_CORES)
              .memoryMb(MEMORY_MB)
              .storageMb(STORAGE_MB)
              .gpuEnabled(GPU_ENABLED)
              .exposedPort(EXPOSED_PORT)
              .internalIp(INTERNAL_IP)
              .createdAt(CREATED_AT)
              .expiresAt(EXPIRES_AT)
              .startedAt(STARTED_AT)
              .status(STATUS)
              .build();

      final var result = instance.toString();

      assertThat(result)
          .contains("InstanceJpa")
          .contains(ID)
          .contains(NAME)
          .contains(DESCRIPTION)
          .contains(EXTERNAL_IP)
          .contains(IMAGE_NAME)
          .contains(STATUS.name());
    }
  }

  @Nested
  @DisplayName("interface methods")
  class InterfaceMethods {

    @Test
    @DisplayName("should return Optional.empty for null description")
    void shouldReturnEmptyOptionalForNullDescription() {
      final var instance =
          InstanceJpa.builder()
              .id(ID)
              .name(NAME)
              .description(null)
              .externalIp(EXTERNAL_IP)
              .imageName(IMAGE_NAME)
              .imageVersion(IMAGE_VERSION)
              .imageRegistry(IMAGE_REGISTRY)
              .cpuCores(CPU_CORES)
              .memoryMb(MEMORY_MB)
              .storageMb(STORAGE_MB)
              .gpuEnabled(GPU_ENABLED)
              .exposedPort(EXPOSED_PORT)
              .internalIp(INTERNAL_IP)
              .createdAt(CREATED_AT)
              .expiresAt(EXPIRES_AT)
              .startedAt(STARTED_AT)
              .status(STATUS)
              .build();

      assertThat(instance.description()).isEmpty();
    }

    @Test
    @DisplayName("should return Optional containing description when set")
    void shouldReturnOptionalContainingDescription() {
      final var instance =
          InstanceJpa.builder()
              .id(ID)
              .name(NAME)
              .description(DESCRIPTION)
              .externalIp(EXTERNAL_IP)
              .imageName(IMAGE_NAME)
              .imageVersion(IMAGE_VERSION)
              .imageRegistry(IMAGE_REGISTRY)
              .cpuCores(CPU_CORES)
              .memoryMb(MEMORY_MB)
              .storageMb(STORAGE_MB)
              .gpuEnabled(GPU_ENABLED)
              .exposedPort(EXPOSED_PORT)
              .internalIp(INTERNAL_IP)
              .createdAt(CREATED_AT)
              .expiresAt(EXPIRES_AT)
              .startedAt(STARTED_AT)
              .status(STATUS)
              .build();

      assertThat(instance.description()).isPresent().contains(DESCRIPTION);
    }

    @Test
    @DisplayName("should return Optional.empty for null stoppedAt")
    void shouldReturnEmptyOptionalForNullStoppedAt() {
      final var instance =
          InstanceJpa.builder()
              .id(ID)
              .name(NAME)
              .externalIp(EXTERNAL_IP)
              .imageName(IMAGE_NAME)
              .imageVersion(IMAGE_VERSION)
              .imageRegistry(IMAGE_REGISTRY)
              .cpuCores(CPU_CORES)
              .memoryMb(MEMORY_MB)
              .storageMb(STORAGE_MB)
              .gpuEnabled(GPU_ENABLED)
              .exposedPort(EXPOSED_PORT)
              .internalIp(INTERNAL_IP)
              .createdAt(CREATED_AT)
              .expiresAt(EXPIRES_AT)
              .startedAt(STARTED_AT)
              .stoppedAt(null)
              .status(STATUS)
              .build();

      assertThat(instance.stoppedAt()).isEmpty();
    }

    @Test
    @DisplayName("should return Optional.empty for null deletedAt")
    void shouldReturnEmptyOptionalForNullDeletedAt() {
      final var instance =
          InstanceJpa.builder()
              .id(ID)
              .name(NAME)
              .externalIp(EXTERNAL_IP)
              .imageName(IMAGE_NAME)
              .imageVersion(IMAGE_VERSION)
              .imageRegistry(IMAGE_REGISTRY)
              .cpuCores(CPU_CORES)
              .memoryMb(MEMORY_MB)
              .storageMb(STORAGE_MB)
              .gpuEnabled(GPU_ENABLED)
              .exposedPort(EXPOSED_PORT)
              .internalIp(INTERNAL_IP)
              .createdAt(CREATED_AT)
              .expiresAt(EXPIRES_AT)
              .startedAt(STARTED_AT)
              .deletedAt(null)
              .status(STATUS)
              .build();

      assertThat(instance.deletedAt()).isEmpty();
    }

    @Test
    @DisplayName("should return Optional.empty for null lastAccessedAt")
    void shouldReturnEmptyOptionalForNullLastAccessedAt() {
      final var instance =
          InstanceJpa.builder()
              .id(ID)
              .name(NAME)
              .externalIp(EXTERNAL_IP)
              .imageName(IMAGE_NAME)
              .imageVersion(IMAGE_VERSION)
              .imageRegistry(IMAGE_REGISTRY)
              .cpuCores(CPU_CORES)
              .memoryMb(MEMORY_MB)
              .storageMb(STORAGE_MB)
              .gpuEnabled(GPU_ENABLED)
              .exposedPort(EXPOSED_PORT)
              .internalIp(INTERNAL_IP)
              .createdAt(CREATED_AT)
              .expiresAt(EXPIRES_AT)
              .startedAt(STARTED_AT)
              .lastAccessedAt(null)
              .status(STATUS)
              .build();

      assertThat(instance.lastAccessedAt()).isEmpty();
    }

    @Test
    @DisplayName("should implement Instance interface")
    void shouldImplementInstanceInterface() {
      final var instance =
          InstanceJpa.builder()
              .id(ID)
              .name(NAME)
              .externalIp(EXTERNAL_IP)
              .imageName(IMAGE_NAME)
              .imageVersion(IMAGE_VERSION)
              .imageRegistry(IMAGE_REGISTRY)
              .cpuCores(CPU_CORES)
              .memoryMb(MEMORY_MB)
              .storageMb(STORAGE_MB)
              .gpuEnabled(GPU_ENABLED)
              .exposedPort(EXPOSED_PORT)
              .internalIp(INTERNAL_IP)
              .createdAt(CREATED_AT)
              .expiresAt(EXPIRES_AT)
              .startedAt(STARTED_AT)
              .status(STATUS)
              .build();

      assertThat(instance).isInstanceOf(Instance.class);
    }
  }

  @Nested
  @DisplayName("equals and hashCode")
  class EqualsAndHashCode {

    @Test
    @DisplayName("should be equal when ids are the same")
    void shouldBeEqualWhenIdsAreSame() {
      final var instance1 = InstanceJpa.builder().id(ID).name(NAME).status(STATUS).build();
      final var instance2 =
          InstanceJpa.builder().id(ID).name("other").status(InstanceStatus.STOPPED).build();

      assertThat(instance1).isEqualTo(instance2).hasSameHashCodeAs(instance2);
    }

    @Test
    @DisplayName("should not be equal when ids differ")
    void shouldNotBeEqualWhenIdsDiffer() {
      final var instance1 = InstanceJpa.builder().id(ID).name(NAME).status(STATUS).build();
      final var instance2 = InstanceJpa.builder().id("inst-002").name(NAME).status(STATUS).build();

      assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    @DisplayName("should not be equal to null")
    void shouldNotBeEqualToNull() {
      final var instance = InstanceJpa.builder().id(ID).name(NAME).status(STATUS).build();

      assertThat(instance).isNotEqualTo(null);
    }

    @Test
    @DisplayName("should be equal to itself")
    void shouldBeEqualToItself() {
      final var instance = InstanceJpa.builder().id(ID).name(NAME).status(STATUS).build();

      assertThat(instance).isEqualTo(instance);
    }

    @Test
    @DisplayName("should not be equal when other has null id")
    void shouldNotBeEqualWhenOtherHasNullId() {
      final var instanceWithId = InstanceJpa.builder().id(ID).name(NAME).status(STATUS).build();
      final var instanceWithoutId = InstanceJpa.builder().name(NAME).status(STATUS).build();

      assertThat(instanceWithId).isNotEqualTo(instanceWithoutId);
    }
  }
}
