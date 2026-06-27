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
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
  private static final int DEFAULT_VNC_PORT = 6901;
  private static final int DEFAULT_EXPOSED_PORT = 8080;
  private static final String VNC_PASSWORD_CHARS =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final int VNC_PASSWORD_LENGTH = 12;

  private final InstanceRepository instanceRepository;
  private final InstanceUserRepository instanceUserRepository;
  private final WorkspaceProvisionerService workspaceProvisionerService;
  private final UniqueIdGenerator uniqueIdGenerator;
  private final RestTemplate restTemplate;
  private final SecureRandom secureRandom = new SecureRandom();

  public InstanceServiceOperation(
      InstanceRepository instanceRepository,
      InstanceUserRepository instanceUserRepository,
      WorkspaceProvisionerService workspaceProvisionerService,
      UniqueIdGenerator uniqueIdGenerator) {
    this.instanceRepository = instanceRepository;
    this.instanceUserRepository = instanceUserRepository;
    this.workspaceProvisionerService = workspaceProvisionerService;
    this.uniqueIdGenerator = uniqueIdGenerator;
    this.restTemplate = new RestTemplate();
  }

  /**
   * Creates a new virtual lab instance and provisions a Docker workspace for the given user.
   *
   * <p>A unique ID is generated for the instance, a persistent workspace container is started via
   * the provisioner, and an ownership association between the user and the instance is persisted.
   * The instance is created with a default 7-day expiration.
   *
   * @param userId the ID of the user creating the instance
   * @param name the display name for the instance
   * @param description an optional description of the instance
   * @param imageName the Docker image name to use for the workspace
   * @param imageVersion the Docker image version tag
   * @param imageRegistry the Docker image registry URL
   * @param cpuCores the number of CPU cores to allocate
   * @param memoryMb the amount of memory in megabytes to allocate
   * @param storageMb the amount of storage in megabytes to allocate
   * @param gpuEnabled whether GPU acceleration is enabled
   * @param exposedPort the port to expose on the container
   * @return the persisted instance with its generated ID and timestamps
   */
  @Override
  @Nonnull
  @Transactional
  public Instance createInstance(
      String userId,
      String name,
      Optional<String> description,
      String imageName,
      String imageVersion,
      String imageRegistry,
      int cpuCores,
      int memoryMb,
      int storageMb,
      boolean gpuEnabled,
      int exposedPort) {

    logger.info("Creating instance for user: {}", userId);

    String instanceId = uniqueIdGenerator.generate();
    String vncPassword = generateVncPassword();
    String containerId =
        workspaceProvisionerService.createWorkspace(
            userId,
            true,
            imageName,
            imageVersion,
            cpuCores,
            memoryMb,
            storageMb,
            gpuEnabled,
            exposedPort,
            vncPassword);

    int hostVncPort = workspaceProvisionerService.getHostVncPort(containerId);
    String internalIp = workspaceProvisionerService.getContainerIp(containerId);

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expiresAt = now.plusDays(7);

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
            .vncPort(hostVncPort)
            .vncEnabled(true)
            .vncPassword(vncPassword)
            .internalIp(internalIp)
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

  /**
   * Retrieves a single instance by its unique identifier.
   *
   * @param instanceId the unique identifier of the instance to look up
   * @return the instance if found, or {@code Optional.empty()} if no instance exists with the ID
   */
  @Override
  @Nonnull
  @Transactional(readOnly = true)
  public Optional<Instance> getInstanceById(String instanceId) {
    logger.debug("Retrieving instance by ID: {}", instanceId);
    return instanceRepository
        .findById(instanceId)
        .filter(i -> i.getStatus() != InstanceStatus.DELETED)
        .map(Instance.class::cast);
  }

  @Override
  @Nonnull
  @Transactional(readOnly = true)
  public List<Instance> getInstancesByUserId(String userId) {
    logger.debug("Retrieving instances for user: {}", userId);
    return instanceRepository.findByUserId(userId).stream()
        .filter(i -> i.getStatus() != InstanceStatus.DELETED)
        .map(Instance.class::cast)
        .toList();
  }

  @Override
  @Nonnull
  @Transactional
  public Instance startInstance(String instanceId) {
    logger.info("Starting instance: {}", instanceId);

    final var instance = requireInstanceById(instanceId);
    rejectDeleted(instance);

    if (instance.getStatus() == InstanceStatus.RUNNING) {
      logger.warn("Instance {} is already running", instanceId);
      return instance;
    }

    instance.setStatus(InstanceStatus.STARTING);
    instance.setStartedAt(LocalDateTime.now());
    InstanceJpa savedInstance = instanceRepository.save(instance);

    try {
      workspaceProvisionerService.startWorkspace(instance.getExternalIp());
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
  public Instance stopInstance(String instanceId) {
    logger.info("Stopping instance: {}", instanceId);

    final var instance = requireInstanceById(instanceId);
    rejectDeleted(instance);

    if (instance.getStatus() == InstanceStatus.STOPPED) {
      logger.warn("Instance {} is already stopped", instanceId);
      return instance;
    }

    instance.setStatus(InstanceStatus.STOPPED);
    instance.setStoppedAt(LocalDateTime.now());

    try {
      workspaceProvisionerService.stopWorkSpace(instance.getExternalIp());
    } catch (Exception e) {
      logger.error("Failed to stop container for instance: {}", instanceId, e);
    }

    return instanceRepository.save(instance);
  }

  @Override
  @Transactional
  public void deleteInstance(String instanceId) {
    logger.info("Deleting instance: {}", instanceId);

    final var instance = requireInstanceById(instanceId);

    if (instance.getStatus() != InstanceStatus.STOPPED) {
      throw new IllegalStateException(
          "Instance must be STOPPED before deletion, current status: " + instance.getStatus());
    }

    instance.setStatus(InstanceStatus.DELETED);
    instance.setDeletedAt(LocalDateTime.now());
    instanceRepository.save(instance);

    logger.info("Instance deleted successfully: {}", instanceId);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkOwnership(String instanceId, String userId) {
    return instanceUserRepository.findByInstanceId(instanceId).stream()
        .anyMatch(a -> a.getUserId().equals(userId));
  }

  @Override
  @Nonnull
  @Transactional(readOnly = true)
  public Instance getRemoteSessionInfo(String instanceId, String userId) {
    if (!checkOwnership(instanceId, userId)) {
      throw new SecurityException("User does not own instance: " + instanceId);
    }
    return requireInstanceById(instanceId);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkVncHealth(String instanceId, String userId) {
    if (!checkOwnership(instanceId, userId)) {
      throw new SecurityException("User does not own instance: " + instanceId);
    }
    final var instance = requireInstanceById(instanceId);
    if (instance.getStatus() != InstanceStatus.RUNNING) {
      return false;
    }
    try {
      final var healthUrl =
          "http://localhost:" + instance.getVncPort() + "/";
      restTemplate.getForObject(healthUrl, String.class);
      return true;
    } catch (RestClientException e) {
      logger.warn("VNC health check failed for instance {}: {}", instanceId, e.getMessage());
      return false;
    }
  }

  private InstanceJpa requireInstanceById(String instanceId) {
    return instanceRepository
        .findById(instanceId)
        .orElseThrow(() -> new IllegalArgumentException(INSTANCE_NOT_FOUND + instanceId));
  }

  private void rejectDeleted(InstanceJpa instance) {
    if (instance.getStatus() == InstanceStatus.DELETED) {
      throw new IllegalArgumentException(INSTANCE_NOT_FOUND + instance.getId());
    }
  }

  private String generateVncPassword() {
    var sb = new StringBuilder(VNC_PASSWORD_LENGTH);
    for (int i = 0; i < VNC_PASSWORD_LENGTH; i++) {
      sb.append(VNC_PASSWORD_CHARS.charAt(secureRandom.nextInt(VNC_PASSWORD_CHARS.length())));
    }
    return sb.toString();
  }
}
