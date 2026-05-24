package edu.univalle.gadim.virtual_lab_platform.instances.data.model;

import static org.assertj.core.api.Assertions.assertThat;

import edu.univalle.gadim.virtual_lab_platform.instances.api.type.Instance;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceStatus;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("InstanceJpa Builder")
class InstanceJpaBuilderTest {

  private static final String ID = "inst-001";
  private static final String NAME = "lab-instance";
  private static final String DESCRIPTION = "Kicad workspace";
  private static final String EXTERNAL_IP = "172.17.0.5";
  private static final String IMAGE_NAME = "lab-kicad";
  private static final String IMAGE_VERSION = "1.0";
  private static final String IMAGE_REGISTRY = "registry.univalle.edu";
  private static final int CPU_CORES = 4;
  private static final int MEMORY_MB = 8192;
  private static final int STORAGE_MB = 20480;
  private static final boolean GPU_ENABLED = true;
  private static final int EXPOSED_PORT = 8080;
  private static final String INTERNAL_IP = "10.0.0.5";
  private static final LocalDateTime CREATED_AT = LocalDateTime.of(2025, 1, 15, 10, 30, 0);
  private static final LocalDateTime EXPIRES_AT = LocalDateTime.of(2025, 2, 15, 10, 30, 0);
  private static final LocalDateTime STARTED_AT = LocalDateTime.of(2025, 1, 15, 10, 35, 0);
  private static final LocalDateTime STOPPED_AT = LocalDateTime.of(2025, 1, 20, 18, 0, 0);
  private static final LocalDateTime DELETED_AT = LocalDateTime.of(2025, 1, 21, 0, 0, 0);
  private static final LocalDateTime LAST_ACCESSED_AT = LocalDateTime.of(2025, 1, 19, 14, 30, 0);

  private InstanceJpa.InstanceJpaBuilder fullBuilder() {
    return InstanceJpa.builder()
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
        .stoppedAt(STOPPED_AT)
        .deletedAt(DELETED_AT)
        .lastAccessedAt(LAST_ACCESSED_AT)
        .status(InstanceStatus.RUNNING);
  }

  @Nested
  @DisplayName("when building with all fields")
  class AllFields {

    @Test
    @DisplayName("should populate every field via builder getters")
    void shouldPopulateEveryFieldViaGetters() {
      InstanceJpa instance = fullBuilder().build();

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
          .returns(INTERNAL_IP, InstanceJpa::getInternalIp)
          .returns(CREATED_AT, InstanceJpa::getCreatedAt)
          .returns(EXPIRES_AT, InstanceJpa::getExpiresAt)
          .returns(STARTED_AT, InstanceJpa::getStartedAt)
          .returns(STOPPED_AT, InstanceJpa::getStoppedAt)
          .returns(DELETED_AT, InstanceJpa::getDeletedAt)
          .returns(LAST_ACCESSED_AT, InstanceJpa::getLastAccessedAt)
          .returns(InstanceStatus.RUNNING, InstanceJpa::getStatus);
    }

    @Test
    @DisplayName("should return correct values from Instance interface methods")
    void shouldReturnCorrectValuesFromInterfaceMethods() {
      InstanceJpa instance = fullBuilder().build();

      assertThat(instance)
          .returns(ID, Instance::id)
          .returns(NAME, Instance::name)
          .returns(Optional.of(DESCRIPTION), Instance::description)
          .returns(EXTERNAL_IP, Instance::externalIp)
          .returns(IMAGE_NAME, Instance::imageName)
          .returns(IMAGE_VERSION, Instance::imageVersion)
          .returns(IMAGE_REGISTRY, Instance::imageRegistry)
          .returns(CPU_CORES, Instance::cpuCores)
          .returns(MEMORY_MB, Instance::memoryMb)
          .returns(STORAGE_MB, Instance::storageMb)
          .returns(GPU_ENABLED, Instance::gpuEnabled)
          .returns(EXPOSED_PORT, Instance::exposedPort)
          .returns(INTERNAL_IP, Instance::internalIp)
          .returns(CREATED_AT, Instance::createdAt)
          .returns(EXPIRES_AT, Instance::expiresAt)
          .returns(STARTED_AT, Instance::startedAt)
          .returns(Optional.of(STOPPED_AT), Instance::stoppedAt)
          .returns(Optional.of(DELETED_AT), Instance::deletedAt)
          .returns(Optional.of(LAST_ACCESSED_AT), Instance::lastAccessedAt)
          .returns(InstanceStatus.RUNNING, Instance::status);
    }

    @Test
    @DisplayName("should implement Instance interface")
    void shouldImplementInstanceInterface() {
      InstanceJpa instance = fullBuilder().build();

      assertThat(instance).isInstanceOf(Instance.class);
    }
  }

  @Nested
  @DisplayName("when building with optional fields as null")
  class OptionalFields {

    @Test
    @DisplayName("should return empty Optional for null description")
    void shouldReturnEmptyOptionalForNullDescription() {
      InstanceJpa instance =
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
              .status(InstanceStatus.CREATED)
              .build();

      assertThat(instance.description()).isEmpty();
    }

    @Test
    @DisplayName("should return present Optional for non-null description")
    void shouldReturnPresentOptionalForNonNullDescription() {
      InstanceJpa instance =
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
              .status(InstanceStatus.CREATED)
              .build();

      assertThat(instance.description()).isPresent().contains(DESCRIPTION);
    }

    @Test
    @DisplayName("should return empty Optional for null stoppedAt, deletedAt, lastAccessedAt")
    void shouldReturnEmptyOptionalsForNullTimestamps() {
      InstanceJpa instance =
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
              .status(InstanceStatus.RUNNING)
              .build();

      assertThat(instance)
          .returns(Optional.empty(), Instance::stoppedAt)
          .returns(Optional.empty(), Instance::deletedAt)
          .returns(Optional.empty(), Instance::lastAccessedAt);
    }

    @Test
    @DisplayName("should return present Optionals for non-null timestamps")
    void shouldReturnPresentOptionalsForNonNullTimestamps() {
      InstanceJpa instance = fullBuilder().build();

      assertThat(instance)
          .returns(Optional.of(STOPPED_AT), Instance::stoppedAt)
          .returns(Optional.of(DELETED_AT), Instance::deletedAt)
          .returns(Optional.of(LAST_ACCESSED_AT), Instance::lastAccessedAt);
    }
  }

  @Nested
  @DisplayName("InstanceStatus values")
  class StatusValues {

    @ParameterizedTest(name = "should accept status {0}")
    @EnumSource(InstanceStatus.class)
    @DisplayName("should accept all InstanceStatus values")
    void shouldAcceptAllInstanceStatusValues(InstanceStatus status) {
      InstanceJpa instance =
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
              .status(status)
              .build();

      assertThat(instance.getStatus()).isEqualTo(status);
      assertThat(instance.status()).isEqualTo(status);
    }
  }

  @Nested
  @DisplayName("equals and hashCode contract")
  class Equality {

    @Test
    @DisplayName("should be equal when ids are the same")
    void shouldBeEqualWhenIdsAreSame() {
      InstanceJpa instance1 =
          InstanceJpa.builder().id(ID).name(NAME).status(InstanceStatus.RUNNING).build();
      InstanceJpa instance2 =
          InstanceJpa.builder().id(ID).name("other").status(InstanceStatus.STOPPED).build();

      assertThat(instance1).isEqualTo(instance2).hasSameHashCodeAs(instance2);
    }

    @Test
    @DisplayName("should not be equal when ids differ")
    void shouldNotBeEqualWhenIdsDiffer() {
      InstanceJpa instance1 =
          InstanceJpa.builder().id(ID).name(NAME).status(InstanceStatus.RUNNING).build();
      InstanceJpa instance2 =
          InstanceJpa.builder().id("inst-002").name(NAME).status(InstanceStatus.RUNNING).build();

      assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    @DisplayName("should not be equal to null")
    void shouldNotBeEqualToNull() {
      InstanceJpa instance =
          InstanceJpa.builder().id(ID).name(NAME).status(InstanceStatus.RUNNING).build();

      assertThat(instance).isNotEqualTo(null);
    }

    @Test
    @DisplayName("should be equal to itself")
    void shouldBeEqualToItself() {
      InstanceJpa instance =
          InstanceJpa.builder().id(ID).name(NAME).status(InstanceStatus.RUNNING).build();

      assertThat(instance).isEqualTo(instance);
    }

    @Test
    @DisplayName("should not be equal when other has null id")
    void shouldNotBeEqualWhenOtherHasNullId() {
      InstanceJpa instanceWithId =
          InstanceJpa.builder().id(ID).name(NAME).status(InstanceStatus.RUNNING).build();
      InstanceJpa instanceWithoutId =
          InstanceJpa.builder().name(NAME).status(InstanceStatus.RUNNING).build();

      assertThat(instanceWithId).isNotEqualTo(instanceWithoutId);
    }
  }

  @Nested
  @DisplayName("builder instances")
  class BuilderInstances {

    @Test
    @DisplayName("should produce distinct objects on successive builds")
    void shouldProduceDistinctObjectsOnSuccessiveBuilds() {
      InstanceJpa.InstanceJpaBuilder builder =
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
              .status(InstanceStatus.RUNNING);

      InstanceJpa first = builder.build();
      InstanceJpa second = builder.build();

      assertThat(first).isNotSameAs(second);
    }

    @Test
    @DisplayName("should allow field override on builder")
    void shouldAllowFieldOverrideOnBuilder() {
      InstanceJpa instance =
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
              .status(InstanceStatus.RUNNING)
              .name("overridden-name")
              .build();

      assertThat(instance)
          .returns("overridden-name", InstanceJpa::getName)
          .returns(ID, InstanceJpa::getId);
    }
  }
}
