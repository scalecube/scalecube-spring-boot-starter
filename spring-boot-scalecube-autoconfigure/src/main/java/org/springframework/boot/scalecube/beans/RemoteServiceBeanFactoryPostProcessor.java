package org.springframework.boot.scalecube.beans;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;

/**
 * Overrides {@link org.springframework.beans.factory.support.InstantiationStrategy} to {@link
 * InjectRouterRemoteServiceInstantiationStrategy}.
 */
class RemoteServiceBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

  static final String BEAN_NAME = RemoteServiceBeanFactoryPostProcessor.class.getName();

  /**
   * {@inheritDoc}
   */
  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory factory)
      throws BeansException {
    if (factory instanceof AbstractAutowireCapableBeanFactory) {
      ((AbstractAutowireCapableBeanFactory) factory)
          .setInstantiationStrategy(new InjectRouterRemoteServiceInstantiationStrategy());
    }
  }
}
