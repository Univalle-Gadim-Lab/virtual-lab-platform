package edu.univalle.gadim.virtual_lab_platform.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configuration for the users module.
 *
 * <p>Provides a {@link PasswordEncoder} bean backed by Spring Security's
 * {@link BCryptPasswordEncoder} with the default strength (10). This encoder
 * is used across the platform for hashing passwords during user creation
 * and verifying credentials during authentication.
 */
@Configuration
public class UserSecurityConfig {

  /**
   * Creates a BCrypt password encoder with the default strength of 10.
   *
   * <p>BCrypt strength 10 is the industry-standard cost factor, offering a
   * well-balanced trade-off between security and performance. The cost factor
   * is intentional and should not be changed without considering the impact
   * on authentication latency.
   *
   * @return a BCrypt-based password encoder instance
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
