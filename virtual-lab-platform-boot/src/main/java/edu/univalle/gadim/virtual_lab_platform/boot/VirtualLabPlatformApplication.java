package edu.univalle.gadim.virtual_lab_platform.boot;

import edu.univalle.gadim.virtual_lab_platform.boot.config.BootConfig;
import edu.univalle.gadim.virtual_lab_platform.boot.config.SecurityConfig;
import edu.univalle.gadim.virtual_lab_platform.instances.data.repository.InstanceRepository;
import edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
    scanBasePackageClasses = {BootConfig.class, SecurityConfig.class})
@EntityScan(basePackages = {
    "edu.univalle.gadim.virtual_lab_platform.instances.data.model",
    "edu.univalle.gadim.virtual_lab_platform.users.data.model",
})
@EnableJpaRepositories(basePackageClasses = {
    UserRepository.class,
    InstanceRepository.class
})
public class VirtualLabPlatformApplication {

  public static void main(String[] args) {
    SpringApplication.run(VirtualLabPlatformApplication.class, args);
  }
}
