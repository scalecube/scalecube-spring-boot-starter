package org.springframework.boot.scalecube.discovery;

import io.scalecube.services.discovery.ScalecubeServiceDiscovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.scalecube.properties.ScalecubeProperties;
import org.springframework.boot.scalecube.properties.ScalecubePropertiesConfiguration;
import org.springframework.boot.scalecube.properties.discovery.Discovery;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@Import(ScalecubePropertiesConfiguration.class)
@ConditionalOnClass(ScalecubeServiceDiscovery.class)
public class ScalecubeDiscoveryConfiguration {

  private ScalecubeProperties properties;

  @Bean
  public DiscoveryInitializer discoveryInitializer() {
    return endpoint -> {
      ScalecubeServiceDiscovery serviceDiscovery = new ScalecubeServiceDiscovery(endpoint);
      Discovery discovery = properties.getDiscovery();
      if (discovery != null) {
        serviceDiscovery = serviceDiscovery.options(discovery);
      }
      return serviceDiscovery;
    };
  }


  @Autowired
  public void setProperties(ScalecubeProperties properties) {
    this.properties = properties;
  }
}
