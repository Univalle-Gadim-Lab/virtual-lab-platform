package edu.univalle.gadim.virtual_lab_platform.instances.operation;

import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceUserService;
import edu.univalle.gadim.virtual_lab_platform.instances.api.type.InstanceUser;
import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceUserJpa;
import edu.univalle.gadim.virtual_lab_platform.instances.data.repository.InstanceUserRepository;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing instance-user associations.
 *
 * <p>This class provides the core business logic for managing the relationships between users and
 * instances, including ownership and access control.
 */
@Service
@Transactional
@ParametersAreNonnullByDefault
public class InstanceUserServiceOperation implements InstanceUserService {

  private static final Logger logger = LoggerFactory.getLogger(InstanceUserServiceOperation.class);

  private final InstanceUserRepository instanceUserRepository;
  private final UniqueIdGenerator uniqueIdGenerator;

  public InstanceUserServiceOperation(
      InstanceUserRepository instanceUserRepository, UniqueIdGenerator uniqueIdGenerator) {
    this.instanceUserRepository = instanceUserRepository;
    this.uniqueIdGenerator = uniqueIdGenerator;
  }

  /**
   * Creates an ownership association between a user and an instance.
   *
   * @param userId the ID of the user to assign
   * @param instanceId the ID of the instance to assign the user to
   * @return the persisted instance-user association with its generated ID
   */
  @Override
  @Nonnull
  @Transactional
  public InstanceUser assignUserToInstance(String userId, String instanceId) {
    logger.info("Assigning user {} to instance {}", userId, instanceId);

    InstanceUserJpa instanceUser =
        InstanceUserJpa.builder()
            .id(uniqueIdGenerator.generate())
            .instanceId(instanceId)
            .userId(userId)
            .build();

    InstanceUserJpa saved = instanceUserRepository.save(instanceUser);
    logger.debug("User assigned to instance successfully with ID: {}", saved.getId());
    return saved;
  }

  /**
   * Retrieves all user associations for the specified instance.
   *
   * @param instanceId the ID of the instance whose users to retrieve
   * @return a list of instance-user associations, never null but may be empty
   */
  @Override
  @Nonnull
  @Transactional(readOnly = true)
  public List<InstanceUser> getUsersByInstanceId(String instanceId) {
    logger.debug("Retrieving users for instance: {}", instanceId);
    return instanceUserRepository.findByInstanceId(instanceId).stream()
        .map(InstanceUser.class::cast)
        .toList();
  }

  /**
   * Retrieves all instance associations for the specified user.
   *
   * @param userId the ID of the user whose instances to retrieve
   * @return a list of instance-user associations, never null but may be empty
   */
  @Override
  @Nonnull
  @Transactional(readOnly = true)
  public List<InstanceUser> getInstancesByUserId(String userId) {
    logger.debug("Retrieving instances for user: {}", userId);
    return instanceUserRepository.findByUserId(userId).stream()
        .map(InstanceUser.class::cast)
        .toList();
  }

  /**
   * Removes the association between a user and an instance.
   *
   * <p>If no matching association exists, the call is a no-op.
   *
   * @param userId the ID of the user to remove
   * @param instanceId the ID of the instance to remove the user from
   */
  @Override
  @Transactional
  public void removeUserFromInstance(String userId, String instanceId) {
    logger.info("Removing user {} from instance {}", userId, instanceId);

    List<InstanceUserJpa> associations = instanceUserRepository.findByInstanceId(instanceId);
    associations.stream()
        .filter(a -> a.getUserId().equals(userId))
        .forEach(instanceUserRepository::delete);

    logger.debug("User removed from instance successfully");
  }
}
