package org.springframework.boot.scalecube.beans;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;

class ExternalServiceBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

  static final String ESS_BFPP_BEAN_NAME = ExternalServiceBeanFactoryPostProcessor.class.getName();

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory factory)
      throws BeansException {
    if (factory instanceof AbstractAutowireCapableBeanFactory) {
      ((AbstractAutowireCapableBeanFactory) factory)
          .setInstantiationStrategy(new ExternalServiceInstantiationStrategy());
    }
  }
}
