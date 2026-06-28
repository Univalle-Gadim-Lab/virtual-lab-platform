package edu.univalle.gadim.virtual_lab_platform.authentication.web.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.univalle.gadim.virtual_lab_platform.authentication.api.service.TokenService;
import edu.univalle.gadim.virtual_lab_platform.users.api.type.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

@NullMarked
@DisplayName("JwtAuthenticationFilter")
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterUnTest {

  private static final String TOKEN = "valid.access.token";
  private static final String USER_ID = "ana.martinez@correounivalle.edu.co";

  @Mock private TokenService tokenService;
  @Mock private FilterChain filterChain;

  private JwtAuthenticationFilter filter;

  @BeforeEach
  void setUp() {
    filter = new JwtAuthenticationFilter(tokenService);
    SecurityContextHolder.clearContext();
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Nested
  @DisplayName("Authorization: Bearer header extraction")
  class AuthorizationHeader {

    @Test
    @DisplayName("should authenticate via Authorization Bearer header")
    void shouldAuthenticateViaHeader() throws Exception {
      // Given
      final var request = new MockHttpServletRequest();
      request.addHeader("Authorization", "Bearer " + TOKEN);
      final var response = new MockHttpServletResponse();
      when(tokenService.validateAccessToken(TOKEN)).thenReturn(true);
      when(tokenService.extractUserId(TOKEN)).thenReturn(USER_ID);
      when(tokenService.extractRoles(TOKEN)).thenReturn(List.of(Role.STUDENT));

      // When
      filter.doFilter(request, response, filterChain);

      // Then
      verify(tokenService).validateAccessToken(TOKEN);
      verify(filterChain).doFilter(request, response);

      final var auth = SecurityContextHolder.getContext().getAuthentication();
      assertThat(auth).isNotNull();
      assertThat(auth.getName()).isEqualTo(USER_ID);
      assertThat(auth.getAuthorities())
          .extracting("authority")
          .containsExactly("ROLE_STUDENT");
    }

    @Test
    @DisplayName("should skip authentication and continue chain when header is missing")
    void shouldSkipWhenHeaderMissing() throws Exception {
      // Given
      final var request = new MockHttpServletRequest();
      final var response = new MockHttpServletResponse();

      // When
      filter.doFilter(request, response, filterChain);

      // Then
      verify(tokenService, never()).validateAccessToken(any());
      verify(filterChain).doFilter(request, response);
      assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("should skip authentication when token is invalid")
    void shouldSkipOnInvalidToken() throws Exception {
      // Given
      final var request = new MockHttpServletRequest();
      request.addHeader("Authorization", "Bearer " + TOKEN);
      final var response = new MockHttpServletResponse();
      when(tokenService.validateAccessToken(TOKEN)).thenReturn(false);

      // When
      filter.doFilter(request, response, filterChain);

      // Then
      verify(filterChain).doFilter(request, response);
      assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
  }

  @Nested
  @DisplayName("Query parameter and cookie extraction")
  class QueryParamAndCookie {

    @Test
    @DisplayName("should authenticate via ?token= query parameter")
    void shouldAuthenticateViaQueryParam() throws Exception {
      // Given
      final var request = new MockHttpServletRequest();
      request.setParameter("token", TOKEN);
      final var response = new MockHttpServletResponse();
      when(tokenService.validateAccessToken(TOKEN)).thenReturn(true);
      when(tokenService.extractUserId(TOKEN)).thenReturn(USER_ID);
      when(tokenService.extractRoles(TOKEN)).thenReturn(List.of(Role.TEACHER));

      // When
      filter.doFilter(request, response, filterChain);

      // Then
      verify(tokenService).validateAccessToken(TOKEN);
      assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities())
          .extracting("authority")
          .containsExactly("ROLE_TEACHER");
    }

    @Test
    @DisplayName("should authenticate via vnc_token cookie (for iframe/WebSocket flows)")
    void shouldAuthenticateViaCookie() throws Exception {
      // Given
      final var request = new MockHttpServletRequest();
      request.setCookies(new Cookie("vnc_token", TOKEN));
      final var response = new MockHttpServletResponse();
      when(tokenService.validateAccessToken(TOKEN)).thenReturn(true);
      when(tokenService.extractUserId(TOKEN)).thenReturn(USER_ID);
      when(tokenService.extractRoles(TOKEN)).thenReturn(List.of(Role.STUDENT));

      // When
      filter.doFilter(request, response, filterChain);

      // Then
      verify(tokenService).validateAccessToken(TOKEN);
      assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
    }
  }

  @Nested
  @DisplayName("Role mapping")
  class RoleMapping {

    @Test
    @DisplayName("should propagate each role as a Spring Security ROLE_<role> prefix")
    void shouldMapAllRoles() throws Exception {
      // Given
      final var request = new MockHttpServletRequest();
      request.addHeader("Authorization", "Bearer " + TOKEN);
      final var response = new MockHttpServletResponse();
      when(tokenService.validateAccessToken(TOKEN)).thenReturn(true);
      when(tokenService.extractUserId(TOKEN)).thenReturn(USER_ID);
      when(tokenService.extractRoles(TOKEN))
          .thenReturn(List.of(Role.ADMIN, Role.STUDENT));

      // When
      filter.doFilter(request, response, filterChain);

      // Then
      assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities())
          .extracting("authority")
          .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_STUDENT");
    }
  }

  @Nested
  @DisplayName("Path bypass")
  class BypassPaths {

    @Test
    @DisplayName("should skip filter for /api/auth/login")
    void shouldBypassLogin() throws Exception {
      // Given
      final var request = new MockHttpServletRequest("POST", "/api/auth/login");
      final var response = new MockHttpServletResponse();

      // When
      filter.doFilter(request, response, filterChain);

      // Then
      verify(tokenService, never()).validateAccessToken(eq(TOKEN));
      verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("should skip filter for /api/auth/refresh")
    void shouldBypassRefresh() throws Exception {
      // Given
      final var request = new MockHttpServletRequest("POST", "/api/auth/refresh");
      final var response = new MockHttpServletResponse();

      // When
      filter.doFilter(request, response, filterChain);

      // Then
      verify(tokenService, never()).validateAccessToken(eq(TOKEN));
      verify(filterChain).doFilter(request, response);
    }
  }
}
