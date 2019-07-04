package org.springframework.boot.scalecube.beans;

import io.scalecube.services.Microservices;
import io.scalecube.services.ServiceCall;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class ServiceCallAwareBeaFactoryProcessor implements BeanFactoryPostProcessor {

  public static final String BEAN_NAME = ServiceCallAwareBeaFactoryProcessor.class.getName();

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
      throws BeansException {

    Microservices microservices = beanFactory.getBean(Microservices.class);
    ServiceCall serviceCall = microservices.call();
    Map<String, ServiceCallAware> beansOfType = beanFactory.getBeansOfType(ServiceCallAware.class);
    for (ServiceCallAware value : beansOfType.values()) {
      value.setServiceCall(serviceCall);
    }

  }
}
