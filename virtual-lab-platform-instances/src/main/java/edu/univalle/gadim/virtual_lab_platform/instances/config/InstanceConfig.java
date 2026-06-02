package edu.univalle.gadim.virtual_lab_platform.instances.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class for the instances module.
 *
 * <p>Provides beans and configuration for the instances bounded context.
 * Enables binding of {@link WorkspaceImageProperties} from {@code application.yml}.
 */
@Configuration
@EnableConfigurationProperties(WorkspaceImageProperties.class)
public class InstanceConfig {

  @Bean
  public DockerClient dockerClient() {
    var config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
    var httpClient =
        new ApacheDockerHttpClient.Builder().dockerHost(config.getDockerHost()).build();
    return DockerClientImpl.getInstance(config, httpClient);
  }
}
