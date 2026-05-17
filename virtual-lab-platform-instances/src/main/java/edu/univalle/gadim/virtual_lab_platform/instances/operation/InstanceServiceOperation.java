package edu.univalle.gadim.virtual_lab_platform.instances.operation;

import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceService;
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
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing virtual lab instances.
 *
 * <p>This class provides the core business logic for creating, managing, and controlling the
 * lifecycle of virtual lab instances, including integration with the workspace provisioner.
 */
@Service
@Transactional
@ParametersAreNonnullByDefault
public class InstanceServiceOperation implements InstanceService {

  private static final Logger logger = LoggerFactory.getLogger(InstanceServiceOperation.class);
  private static final String INSTANCE_NOT_FOUND = "Instance not found: ";

  private final InstanceRepository instanceRepository;
  private final InstanceUserRepository instanceUserRepository;
  private final WorkspaceProvisionerService workspaceProvisionerService;
  private final UniqueIdGenerator uniqueIdGenerator;

  public InstanceServiceOperation(
      InstanceRepository instanceRepository,
      InstanceUserRepository instanceUserRepository,
      WorkspaceProvisionerService workspaceProvisionerService,
      UniqueIdGenerator uniqueIdGenerator) {
    this.instanceRepository = instanceRepository;
    this.instanceUserRepository = instanceUserRepository;
    this.workspaceProvisionerService = workspaceProvisionerService;
    this.uniqueIdGenerator = uniqueIdGenerator;
  }

  @Override
  @Nonnull
  @Transactional
  public Instance createInstance(
      @Nonnull String userId,
      @Nonnull String name,
      @Nonnull Optional<String> description,
      @Nonnull String imageName,
      @Nonnull String imageVersion,
      @Nonnull String imageRegistry,
      int cpuCores,
      int memoryMb,
      int storageMb,
      boolean gpuEnabled,
      int exposedPort) {

    logger.info("Creating instance for user: {}", userId);

    String instanceId = uniqueIdGenerator.generate();
    String containerId = workspaceProvisionerService.createWorkspace(userId, true);

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expiresAt = now.plusDays(7); // Default 7 days expiration

    InstanceJpa instance =
        InstanceJpa.builder()
            .id(instanceId)
            .name(name)
            .description(description.orElse(null))
            .externalIp(containerId)
            .imageName(imageName)
            .imageVersion(imageVersion)
            .imageRegistry(imageRegistry)
            .cpuCores(cpuCores)
            .memoryMb(memoryMb)
            .storageMb(storageMb)
            .gpuEnabled(gpuEnabled)
            .exposedPort(exposedPort)
            .internalIp("127.0.0.1") // Will be updated when container is running
            .createdAt(now)
            .expiresAt(expiresAt)
            .startedAt(now)
            .status(InstanceStatus.CREATED)
            .build();

    InstanceJpa savedInstance = instanceRepository.save(instance);

    // Create the user-instance association
    InstanceUserJpa instanceUser =
        InstanceUserJpa.builder()
            .id(uniqueIdGenerator.generate())
            .instanceId(instanceId)
            .userId(userId)
            .build();
    instanceUserRepository.save(instanceUser);

    logger.info("Instance created successfully with ID: {}", instanceId);
    return savedInstance;
  }

  @Override
  @Nonnull
  @Transactional(readOnly = true)
  public Optional<Instance> getInstanceById(@Nonnull String instanceId) {
    logger.debug("Retrieving instance by ID: {}", instanceId);
    return instanceRepository.findById(instanceId).map(Instance.class::cast);
  }

  @Override
  @Nonnull
  @Transactional(readOnly = true)
  public List<Instance> getInstancesByUserId(@Nonnull String userId) {
    logger.debug("Retrieving instances for user: {}", userId);
    return instanceRepository.findByUserId(userId).stream()
        .map(Instance.class::cast)
        .toList();
  }

  @Override
  @Nonnull
  @Transactional
  public Instance startInstance(@Nonnull String instanceId) {
    logger.info("Starting instance: {}", instanceId);

    InstanceJpa instance =
        instanceRepository
            .findById(instanceId)
            .orElseThrow(() -> new IllegalArgumentException(INSTANCE_NOT_FOUND + instanceId));

    if (instance.getStatus() == InstanceStatus.RUNNING) {
      logger.warn("Instance {} is already running", instanceId);
      return instance;
    }

    instance.setStatus(InstanceStatus.STARTING);
    instance.setStartedAt(LocalDateTime.now());
    InstanceJpa savedInstance = instanceRepository.save(instance);

    // Start the container via workspace provisioner
    try {
      workspaceProvisionerService.createWorkspace(instance.getExternalIp(), true);
      savedInstance.setStatus(InstanceStatus.RUNNING);
      savedInstance.setLastAccessedAt(LocalDateTime.now());
    } catch (Exception e) {
      logger.error("Failed to start container for instance: {}", instanceId, e);
      savedInstance.setStatus(InstanceStatus.STOPPED);
    }

    return instanceRepository.save(savedInstance);
  }

  @Override
  @Nonnull
  @Transactional
  public Instance stopInstance(@Nonnull String instanceId) {
    logger.info("Stopping instance: {}", instanceId);

    InstanceJpa instance =
        instanceRepository
            .findById(instanceId)
            .orElseThrow(() -> new IllegalArgumentException(INSTANCE_NOT_FOUND + instanceId));

    if (instance.getStatus() == InstanceStatus.STOPPED) {
      logger.warn("Instance {} is already stopped", instanceId);
      return instance;
    }

    instance.setStatus(InstanceStatus.STOPPED);
    instance.setStoppedAt(LocalDateTime.now());

    // Stop the container via workspace provisioner
    try {
      workspaceProvisionerService.stopWorkSpace(instance.getExternalIp());
    } catch (Exception e) {
      logger.error("Failed to stop container for instance: {}", instanceId, e);
    }

    return instanceRepository.save(instance);
  }

  @Override
  @Transactional
  public void deleteInstance(@Nonnull String instanceId) {
    logger.info("Deleting instance: {}", instanceId);

    InstanceJpa instance =
        instanceRepository
            .findById(instanceId)
            .orElseThrow(() -> new IllegalArgumentException(INSTANCE_NOT_FOUND + instanceId));

    // Stop the container if running
    if (instance.getStatus() == InstanceStatus.RUNNING) {
      try {
        workspaceProvisionerService.stopWorkSpace(instance.getExternalIp());
      } catch (Exception e) {
        logger.error("Failed to stop container during instance deletion: {}", instanceId, e);
      }
    }

    instance.setStatus(InstanceStatus.DELETED);
    instance.setDeletedAt(LocalDateTime.now());
    instanceRepository.save(instance);

    // Remove user associations
    instanceUserRepository.findByInstanceId(instanceId).forEach(instanceUserRepository::delete);

    logger.info("Instance deleted successfully: {}", instanceId);
  }
}
