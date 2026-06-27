package edu.univalle.gadim.virtual_lab_platform.instances.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

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

  /**
   * Provides the shared {@link RestTemplate} used by service operations that need to call
   * container-side endpoints (VNC health probe, KasmVNC proxy). Centralizing this bean
   * avoids instantiating {@code new RestTemplate()} ad-hoc in each consumer and gives
   * a single point of configuration for timeouts and message converters.
   *
   * @return a {@link RestTemplate} instance ready for dependency injection
   */
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
