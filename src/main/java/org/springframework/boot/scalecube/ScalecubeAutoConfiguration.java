package org.springframework.boot.scalecube;

import io.scalecube.services.Microservices;
import io.scalecube.services.annotations.Service;
import io.scalecube.services.transport.api.ServiceTransport;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.scalecube.discovery.DiscoveryInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@ComponentScan("org.springframework.boot.scalecube")
public class ScalecubeAutoConfiguration {


  @Bean(name = "microservices")
  public Microservices microservices(
      @Autowired(required = false) DiscoveryInitializer discoveryInitializer,
      ServiceTransport serviceTransport,
      ConfigurableListableBeanFactory beanFactory
  ) {
    Microservices.Builder builder = Microservices.builder();
    if (discoveryInitializer != null) {
      builder.discovery(discoveryInitializer);
    }
    builder.transport(opts -> opts.serviceTransport(() -> serviceTransport));

    String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
    List<Object> services = Stream
        .of(beanDefinitionNames)
        .filter(serviceBeanName -> !beanFactory.getBeanDefinition(serviceBeanName)
            .hasAttribute("external-service") && beanFactory.findAnnotationOnBean(serviceBeanName,
            Service.class) != null
        )
        .map(beanFactory::getBean)
        .collect(Collectors.toList());

    return builder.services(services).startAwait();
  }

}
