package edu.univalle.gadim.virtual_lab_platform.instances.config;

import edu.univalle.gadim.virtual_lab_platform.commons.tool.ObjectIdGenerator;
import edu.univalle.gadim.virtual_lab_platform.commons.type.UniqueIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InstanceConfig {

  @Bean
  public UniqueIdGenerator uniqueIdGenerator() {
    return new ObjectIdGenerator();
  }
}
