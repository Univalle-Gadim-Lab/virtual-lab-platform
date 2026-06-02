package edu.univalle.gadim.virtual_lab_platform.boot;

import edu.univalle.gadim.virtual_lab_platform.authentication.config.AuthenticationConfig;
import edu.univalle.gadim.virtual_lab_platform.authentication.data.repository.RefreshTokenRepository;

import edu.univalle.gadim.virtual_lab_platform.instances.data.repository.InstanceRepository;
import edu.univalle.gadim.virtual_lab_platform.users.data.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
    scanBasePackageClasses = {
      AuthenticationConfig.class
    },
    scanBasePackages = {
      "edu.univalle.gadim.virtual_lab_platform.boot",
      "edu.univalle.gadim.virtual_lab_platform.authentication",
      "edu.univalle.gadim.virtual_lab_platform.users",
      "edu.univalle.gadim.virtual_lab_platform.instances"
    })
@EntityScan(
    basePackages = {
      "edu.univalle.gadim.virtual_lab_platform.instances.data.model",
      "edu.univalle.gadim.virtual_lab_platform.users.data.model",
      "edu.univalle.gadim.virtual_lab_platform.authentication.data.model"
    })
@EnableJpaRepositories(
    basePackageClasses = {
      UserRepository.class,
      InstanceRepository.class,
      RefreshTokenRepository.class
    })
public class VirtualLabPlatformApplication {

  public static void main(String[] args) {
    SpringApplication.run(VirtualLabPlatformApplication.class, args);
  }
}
