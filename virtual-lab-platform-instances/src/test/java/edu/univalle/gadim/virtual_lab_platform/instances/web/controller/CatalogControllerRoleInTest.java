package edu.univalle.gadim.virtual_lab_platform.instances.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import edu.univalle.gadim.virtual_lab_platform.authentication.api.service.TokenService;
import edu.univalle.gadim.virtual_lab_platform.authentication.web.security.JwtAuthenticationFilter;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.CatalogEntryResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.model.WorkspaceImageResponse;
import edu.univalle.gadim.virtual_lab_platform.instances.web.ops.CatalogWsOps;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

@NullMarked
@DisplayName("CatalogController Role-Based Access Tests")
@ExtendWith(MockitoExtension.class)
class CatalogControllerRoleInTest {

  private static final String ACCESS_TOKEN = "valid.access.token";
  private static final String USER_ID = "user-001";

  @Mock private TokenService tokenService;
  @Mock private CatalogWsOps catalogWsOps;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    final var filter = new JwtAuthenticationFilter(tokenService);
    final var controller = new CatalogController(catalogWsOps);
    mockMvc = standaloneSetup(controller).addFilter(filter).build();
  }

  private void mockValidToken(String userId, List<Role> roles) {
    when(tokenService.validateAccessToken(ACCESS_TOKEN)).thenReturn(true);
    when(tokenService.extractUserId(ACCESS_TOKEN)).thenReturn(userId);
    when(tokenService.extractRoles(ACCESS_TOKEN)).thenReturn(roles);
  }

  private WorkspaceImageResponse buildImageResponse() {
    return new WorkspaceImageResponse(
        "kicad", "KiCad", "Electronic design automation suite",
        "latest", "lab-kicad:latest", "EDA");
  }

  @Test
  @DisplayName("should allow getAvailableImages with STUDENT token")
  void shouldAllowGetAvailableImagesWithStudentToken() throws Exception {
    mockValidToken(USER_ID, List.of(Role.STUDENT));
    when(catalogWsOps.getAvailableImages()).thenReturn(List.of(buildImageResponse()));

    mockMvc
        .perform(get("/api/catalog/images").header("Authorization", "Bearer " + ACCESS_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value("kicad"))
        .andExpect(jsonPath("$[0].name").value("KiCad"));
  }

  @Test
  @DisplayName("should allow getAvailableImages with TEACHER token")
  void shouldAllowGetAvailableImagesWithTeacherToken() throws Exception {
    mockValidToken(USER_ID, List.of(Role.TEACHER));
    when(catalogWsOps.getAvailableImages()).thenReturn(List.of(buildImageResponse()));

    mockMvc
        .perform(get("/api/catalog/images").header("Authorization", "Bearer " + ACCESS_TOKEN))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("should allow getAvailableImages with ADMIN token")
  void shouldAllowGetAvailableImagesWithAdminToken() throws Exception {
    mockValidToken(USER_ID, List.of(Role.ADMIN));
    when(catalogWsOps.getAvailableImages()).thenReturn(List.of(buildImageResponse()));

    mockMvc
        .perform(get("/api/catalog/images").header("Authorization", "Bearer " + ACCESS_TOKEN))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("should allow getCatalog with STUDENT token")
  void shouldAllowGetCatalogWithStudentToken() throws Exception {
    mockValidToken(USER_ID, List.of(Role.STUDENT));
    when(catalogWsOps.getCatalog())
        .thenReturn(List.of(new CatalogEntryResponse(buildImageResponse(), 5L)));

    mockMvc
        .perform(get("/api/catalog").header("Authorization", "Bearer " + ACCESS_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].image.id").value("kicad"))
        .andExpect(jsonPath("$[0].runningInstanceCount").value(5));
  }

  @Test
  @DisplayName("should allow getCatalog with TEACHER token")
  void shouldAllowGetCatalogWithTeacherToken() throws Exception {
    mockValidToken(USER_ID, List.of(Role.TEACHER));
    when(catalogWsOps.getCatalog())
        .thenReturn(List.of(new CatalogEntryResponse(buildImageResponse(), 2L)));

    mockMvc
        .perform(get("/api/catalog").header("Authorization", "Bearer " + ACCESS_TOKEN))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("should allow getCatalog with ADMIN token")
  void shouldAllowGetCatalogWithAdminToken() throws Exception {
    mockValidToken(USER_ID, List.of(Role.ADMIN));
    when(catalogWsOps.getCatalog())
        .thenReturn(List.of(new CatalogEntryResponse(buildImageResponse(), 0L)));

    mockMvc
        .perform(get("/api/catalog").header("Authorization", "Bearer " + ACCESS_TOKEN))
        .andExpect(status().isOk());
  }
}
