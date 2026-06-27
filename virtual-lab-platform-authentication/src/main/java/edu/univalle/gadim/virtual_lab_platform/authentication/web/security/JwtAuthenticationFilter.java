package edu.univalle.gadim.virtual_lab_platform.authentication.web.security;

import edu.univalle.gadim.virtual_lab_platform.authentication.api.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nonnull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT authentication filter that validates Bearer tokens on each request.
 *
 * <p>Extracts the {@code Authorization: Bearer <token>} header, validates the
 * token via {@link TokenService}, and populates the {@link SecurityContextHolder}
 * with an authenticated principal carrying the user's ID and roles.
 *
 * <p>This filter is skipped for the login and refresh endpoints, which do not
 * require prior authentication.
 *
 * @see TokenService
 * @see org.springframework.security.core.context.SecurityContextHolder
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";
  private static final String TOKEN_PARAM = "token";
  private static final String VNC_TOKEN_COOKIE = "vnc_token";

  private final TokenService tokenService;

  /**
   * Constructs a new {@code JwtAuthenticationFilter} with the specified token service.
   *
   * @param tokenService the token service for JWT validation and claim extraction
   */
  public JwtAuthenticationFilter(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  protected void doFilterInternal(
      @Nonnull HttpServletRequest request,
      @Nonnull HttpServletResponse response,
      @Nonnull FilterChain filterChain)
      throws ServletException, IOException {

    final var token = extractToken(request);

    if (token == null || !tokenService.validateAccessToken(token)) {
      filterChain.doFilter(request, response);
      return;
    }

    final var userId = tokenService.extractUserId(token);
    final var roles = tokenService.extractRoles(token);

    final var authorities =
        roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
            .toList();

    final var authentication =
        new UsernamePasswordAuthenticationToken(userId, null, authorities);
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }

  private String extractToken(HttpServletRequest request) {
    final var authHeader = request.getHeader(AUTHORIZATION_HEADER);
    if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
      return authHeader.substring(BEARER_PREFIX.length());
    }
    final var queryToken = request.getParameter(TOKEN_PARAM);
    if (queryToken != null && !queryToken.isEmpty()) {
      return queryToken;
    }
    final var cookies = request.getCookies();
    if (cookies != null) {
      for (var cookie : cookies) {
        if (VNC_TOKEN_COOKIE.equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }

  @Override
  protected boolean shouldNotFilter(@Nonnull HttpServletRequest request) {
    final var path = request.getRequestURI();
    return "/api/auth/login".equals(path) || "/api/auth/refresh".equals(path);
  }
}
