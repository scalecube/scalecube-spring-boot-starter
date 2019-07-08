package org.springframework.boot.scalecube.beans;

import io.scalecube.services.Microservices;
import io.scalecube.services.ServiceCall;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class ServiceCallAwarePostProcessor
    implements BeanPostProcessor, BeanFactoryAware {

  public static final String BEAN_NAME = ServiceCallAwarePostProcessor.class.getName();

  private BeanFactory beanFactory;

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof ServiceCallAware) {
      Microservices microservices = beanFactory.getBean(Microservices.class);
      ServiceCall serviceCall = microservices.call();
      ((ServiceCallAware) bean).setServiceCall(serviceCall);
    }
    return bean;
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }
}
