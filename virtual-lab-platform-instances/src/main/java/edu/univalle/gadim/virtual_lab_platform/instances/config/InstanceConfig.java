package edu.univalle.gadim.virtual_lab_platform.instances.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class for the instances module.
 *
 * <p>Provides beans and configuration for the instances bounded context.
 * Enables binding of {@link WorkspaceImageProperties} from {@code application.yml}.
 */
@Configuration
@EnableConfigurationProperties(WorkspaceImageProperties.class)
public class InstanceConfig {}
