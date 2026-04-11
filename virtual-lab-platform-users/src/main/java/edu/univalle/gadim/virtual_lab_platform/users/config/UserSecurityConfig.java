package edu.univalle.gadim.virtual_lab_platform.users.config;

import edu.univalle.gadim.virtual_lab_platform.commons.tool.ObjectIdGenerator;
import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configuration for the users module.
 *
 * <p>Provides password encoding beans for user authentication.
 */
@Configuration
public class UserSecurityConfig {

  @Bean
  public UniqueIdGenerator uniqueIdGenerator() {
    return new ObjectIdGenerator();
  }

  /**
   * Creates a BCrypt password encoder bean.
   *
   * @return the password encoder instance
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
