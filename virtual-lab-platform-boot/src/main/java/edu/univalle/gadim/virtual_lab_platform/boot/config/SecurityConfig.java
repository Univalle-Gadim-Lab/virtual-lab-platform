package edu.univalle.gadim.virtual_lab_platform.boot.config;

import edu.univalle.gadim.virtual_lab_platform.authentication.web.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the Virtual Lab Platform.
 *
 * <p>Configures the Spring Security filter chain with JWT-based authentication.
 * The login and refresh endpoints are publicly accessible; all other endpoints
 * require a valid Bearer token.
 *
 * @see JwtAuthenticationFilter
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  /**
   * Constructs a new {@code SecurityConfig} with the JWT authentication filter.
   *
   * @param jwtAuthenticationFilter the filter that validates JWT tokens on each request
   */
  public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  /**
   * Configures the security filter chain with JWT authentication.
   *
   * @param http the HTTP security builder
   * @return the configured security filter chain
   * @throws Exception if configuration fails
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/api/auth/login", "/api/auth/refresh")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(
            jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
