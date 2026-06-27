package edu.univalle.gadim.virtual_lab_platform.instances.web.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.CreateInstanceUserRequest;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.InstanceUserResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.ops.InstanceUsersWsOps;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@NullMarked
@DisplayName("InstanceUsersController")
@ExtendWith(MockitoExtension.class)
class InstanceUsersControllerUnTest {

  private static final String USER_ID = "ana.martinez@correounivalle.edu.co";
  private static final String INSTANCE_ID = "inst-001";

  @Mock private InstanceUsersWsOps instanceUsersWsOps;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    final var controller = new InstanceUsersController(instanceUsersWsOps);
    mockMvc = standaloneSetup(controller).build();
    objectMapper = new ObjectMapper().findAndRegisterModules();
  }

  private InstanceUserResponse buildAssociation() {
    return new InstanceUserResponse("assoc-1", INSTANCE_ID, USER_ID);
  }

  @Nested
  @DisplayName("POST /api/instance-users")
  class Assign {

    @Test
    @DisplayName("should return 200 with the created association")
    void shouldReturnCreatedAssociation() throws Exception {
      // Given
      final var request = new CreateInstanceUserRequest(USER_ID, INSTANCE_ID);
      when(instanceUsersWsOps.assignUserToInstance(request)).thenReturn(buildAssociation());

      // When / Then
      mockMvc
          .perform(
              post("/api/instance-users")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value("assoc-1"))
          .andExpect(jsonPath("$.userId").value(USER_ID));
    }
  }

  @Nested
  @DisplayName("GET /api/instance-users")
  class ListAssociations {

    @Test
    @DisplayName("should return users filtered by instanceId")
    void shouldReturnUsersByInstance() throws Exception {
      // Given
      when(instanceUsersWsOps.getUsersByInstanceId(INSTANCE_ID))
          .thenReturn(List.of(buildAssociation()));

      // When / Then
      mockMvc
          .perform(get("/api/instance-users").param("instanceId", INSTANCE_ID))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.length()").value(1))
          .andExpect(jsonPath("$[0].instanceId").value(INSTANCE_ID));
    }

    @Test
    @DisplayName("should return instances filtered by userId")
    void shouldReturnInstancesByUser() throws Exception {
      // Given
      when(instanceUsersWsOps.getInstancesByUserId(USER_ID))
          .thenReturn(List.of(buildAssociation()));

      // When / Then
      mockMvc
          .perform(get("/api/instance-users").param("userId", USER_ID))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.length()").value(1))
          .andExpect(jsonPath("$[0].userId").value(USER_ID));
    }

    @Test
    @DisplayName("should return 400 when both query params are missing")
    void shouldReturn400WhenNoFilters() throws Exception {
      mockMvc.perform(get("/api/instance-users")).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when both query params are present")
    void shouldReturn400WhenBothFiltersPresent() throws Exception {
      mockMvc
          .perform(
              get("/api/instance-users")
                  .param("instanceId", INSTANCE_ID)
                  .param("userId", USER_ID))
          .andExpect(status().isBadRequest());
    }
  }

  @Nested
  @DisplayName("DELETE /api/instance-users")
  class Remove {

    @Test
    @DisplayName("should return 204 and delegate to WsOps")
    void shouldReturn204OnSuccess() throws Exception {
      // Given
      doNothing()
          .when(instanceUsersWsOps)
          .removeUserFromInstance(USER_ID, INSTANCE_ID);

      // When / Then
      mockMvc
          .perform(
              delete("/api/instance-users")
                  .param("userId", USER_ID)
                  .param("instanceId", INSTANCE_ID))
          .andExpect(status().isNoContent());

      verify(instanceUsersWsOps).removeUserFromInstance(USER_ID, INSTANCE_ID);
    }
  }
}
