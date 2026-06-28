package edu.univalle.gadim.virtual_lab_platform.instances.web.operation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.instances.api.service.InstanceUserService;
import edu.univalle.gadim.virtual_lab_platform.instances.data.model.InstanceUserJpa;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.CreateInstanceUserRequest;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@NullMarked
@DisplayName("InstanceUsersSpringWsOps")
@ExtendWith(MockitoExtension.class)
class InstanceUsersSpringWsOpsUnTest {

  private static final String USER_ID = "ana.martinez@correounivalle.edu.co";
  private static final String INSTANCE_ID = "inst-001";

  @Mock private InstanceUserService instanceUserService;

  private InstanceUsersSpringWsOps wsOps;

  @BeforeEach
  void setUp() {
    wsOps = new InstanceUsersSpringWsOps(instanceUserService);
  }

  private InstanceUserJpa buildAssociation() {
    return InstanceUserJpa.builder().id("assoc-1").userId(USER_ID).instanceId(INSTANCE_ID).build();
  }

  @Nested
  @DisplayName("assignUserToInstance")
  class Assign {

    @Test
    @DisplayName("should map service result to InstanceUserResponse")
    void shouldMap() {
      // Given
      final var request = new CreateInstanceUserRequest(USER_ID, INSTANCE_ID);
      when(instanceUserService.assignUserToInstance(USER_ID, INSTANCE_ID))
          .thenReturn(buildAssociation());

      // When
      final var response = wsOps.assignUserToInstance(request);

      // Then
      assertThat(response)
          .returns("assoc-1", edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceUserResponse::id)
          .returns(USER_ID, edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceUserResponse::userId)
          .returns(INSTANCE_ID, edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceUserResponse::instanceId);
    }
  }

  @Nested
  @DisplayName("getUsersByInstanceId and getInstancesByUserId")
  class Lookups {

    @Test
    @DisplayName("should map users by instance")
    void shouldMapUsersByInstance() {
      // Given
      when(instanceUserService.getUsersByInstanceId(INSTANCE_ID))
          .thenReturn(List.of(buildAssociation()));

      // When
      final var response = wsOps.getUsersByInstanceId(INSTANCE_ID);

      // Then
      assertThat(response).hasSize(1);
    }

    @Test
    @DisplayName("should map instances by user")
    void shouldMapInstancesByUser() {
      // Given
      when(instanceUserService.getInstancesByUserId(USER_ID))
          .thenReturn(List.of(buildAssociation()));

      // When
      final var response = wsOps.getInstancesByUserId(USER_ID);

      // Then
      assertThat(response).hasSize(1);
    }
  }

  @Nested
  @DisplayName("removeUserFromInstance")
  class Remove {

    @Test
    @DisplayName("should delegate to service without mapping")
    void shouldDelegate() {
      // When
      wsOps.removeUserFromInstance(USER_ID, INSTANCE_ID);

      // Then
      verify(instanceUserService).removeUserFromInstance(USER_ID, INSTANCE_ID);
    }
  }
}
