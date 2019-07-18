package org.springframework.boot.scalecube.beans;

import static org.springframework.boot.scalecube.beans.EnableScalecubeClientsImportSelector.REMOTE_SERVICE_ATTRIBUTE;

import io.scalecube.services.Microservices;
import io.scalecube.services.annotations.Service;
import io.scalecube.services.transport.api.ServiceTransport;
import java.util.stream.Stream;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.scalecube.discovery.DiscoveryInitializer;

/**
 * Factory of Scalecube Microservices.
 */
class MicroservicesFactory extends AbstractFactoryBean<Microservices> implements
    InitializingBean {

  static final String BEAN_NAME = "microservices";

  private final ConfigurableListableBeanFactory beanFactory;

  private ServiceTransport serviceTransport;

  private DiscoveryInitializer discoveryInitializer;

  public MicroservicesFactory(
      ConfigurableListableBeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  protected Microservices createInstance() {
    Microservices.Builder builder = Microservices.builder();
    if (discoveryInitializer != null) {
      builder.discovery(discoveryInitializer);
    }
    if (serviceTransport != null) {
      builder.transport(() -> serviceTransport);
    }
    String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
    Object[] services = Stream
        .of(beanDefinitionNames)
        .filter(serviceBeanName -> !beanFactory.getBeanDefinition(serviceBeanName)
            .hasAttribute(REMOTE_SERVICE_ATTRIBUTE)
            && beanFactory.findAnnotationOnBean(serviceBeanName,
            Service.class) != null
        )
        .map(beanFactory::getBean)
        .toArray();
    Microservices microservices = builder.services(services).startAwait();
    return microservices;
  }

  @Override
  public Class<?> getObjectType() {
    return Microservices.class;
  }

  @Autowired(required = false)
  public void setDiscoveryInitializer(
      DiscoveryInitializer discoveryInitializer) {
    this.discoveryInitializer = discoveryInitializer;
  }

  @Autowired(required = false)
  public void setServiceTransport(ServiceTransport serviceTransport) {
    this.serviceTransport = serviceTransport;
  }
}
