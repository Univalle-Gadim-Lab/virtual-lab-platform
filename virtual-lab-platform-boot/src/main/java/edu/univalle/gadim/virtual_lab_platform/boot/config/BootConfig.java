package edu.univalle.gadim.virtual_lab_platform.boot.config;

import edu.univalle.gadim.virtual_lab_platform.commons.tool.ObjectIdGenerator;
import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BootConfig {

  @Bean
  public UniqueIdGenerator uniqueIdGenerator() {
    return new ObjectIdGenerator();
  }
}
