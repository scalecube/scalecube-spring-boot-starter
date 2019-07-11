package org.springframework.boot.scalecube.beans;

import io.scalecube.services.annotations.Service;
import io.scalecube.services.routing.Router;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.CglibSubclassingInstantiationStrategy;
import org.springframework.beans.factory.support.InstantiationStrategy;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.annotation.AnnotationUtils;

public class ExternalServiceInstantiationStrategy implements InstantiationStrategy,
    RouterInitializer {

  private final InstantiationStrategy delegate;

  public ExternalServiceInstantiationStrategy(
      InstantiationStrategy delegate) {
    this.delegate = delegate;
  }

  public ExternalServiceInstantiationStrategy() {
    this.delegate = new CglibSubclassingInstantiationStrategy();
  }

  @Override
  public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner)
      throws BeansException {
    return delegate.instantiate(bd, beanName, owner);
  }

  @Override
  public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner,
      Constructor<?> ctor, Object... args) throws BeansException {
    injectCustomRouter(ctor, args, owner);
    return delegate.instantiate(bd, beanName, owner, ctor, args);
  }

  @Override
  public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner,
      Object factoryBean, Method factoryMethod, Object... args) throws BeansException {
    injectCustomRouter(factoryMethod, args, owner);
    return delegate.instantiate(bd, beanName, owner, factoryBean, factoryMethod, args);
  }

  private void injectCustomRouter(Executable executable, Object[] args, BeanFactory owner) {
    Annotation[][] parameterAnnotations = executable.getParameterAnnotations();
    if (parameterAnnotations.length == 0) {
      return;
    }
    for (int i = 0; i < args.length; i++) {
      Object arg = args[i];
      if (AnnotationUtils.findAnnotation(arg.getClass(), Service.class) == null
          || !(arg instanceof RouterConsumer)) {
        continue;
      }
      for (Annotation ann : parameterAnnotations[i]) {
        if (ann.annotationType() == SelectionStrategy.class) {
          SelectionStrategy annVal = executable.getParameters()[i]
              .getAnnotation(SelectionStrategy.class);
          Router router = router(annVal, owner);
          ((RouterConsumer) arg).setRouter(router);
        }
      }
    }
  }
}
