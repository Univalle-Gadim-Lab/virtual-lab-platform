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

  @Override
  @Nonnull
  @Transactional
  public InstanceUser assignUserToInstance(@Nonnull String userId, @Nonnull String instanceId) {
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

  @Override
  @Nonnull
  @Transactional(readOnly = true)
  public List<InstanceUser> getUsersByInstanceId(@Nonnull String instanceId) {
    logger.debug("Retrieving users for instance: {}", instanceId);
    return instanceUserRepository.findByInstanceId(instanceId).stream()
        .map(InstanceUser.class::cast)
        .toList();
  }

  @Override
  @Nonnull
  @Transactional(readOnly = true)
  public List<InstanceUser> getInstancesByUserId(@Nonnull String userId) {
    logger.debug("Retrieving instances for user: {}", userId);
    return instanceUserRepository.findByUserId(userId).stream()
        .map(InstanceUser.class::cast)
        .toList();
  }

  @Override
  @Transactional
  public void removeUserFromInstance(@Nonnull String userId, @Nonnull String instanceId) {
    logger.info("Removing user {} from instance {}", userId, instanceId);

    List<InstanceUserJpa> associations = instanceUserRepository.findByInstanceId(instanceId);
    associations.stream()
        .filter(a -> a.getUserId().equals(userId))
        .forEach(instanceUserRepository::delete);

    logger.debug("User removed from instance successfully");
  }
}
