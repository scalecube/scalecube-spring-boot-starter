package org.springframework.boot.scalecube.beans;

import io.scalecube.services.annotations.Service;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.CglibSubclassingInstantiationStrategy;
import org.springframework.beans.factory.support.InstantiationStrategy;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.annotation.AnnotationUtils;

public class ExternalServiceInstantiationStrategy implements InstantiationStrategy {

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
    Annotation[][] parameterAnnotations = ctor.getParameterAnnotations();
    if (parameterAnnotations.length != 0) {

      for (int i = 0; i < args.length; i++) {
        Object arg = args[i];
        if (AnnotationUtils.findAnnotation(arg.getClass(), Service.class) != null) {
          for (Annotation annotation : parameterAnnotations[i]) {
            if (annotation.annotationType() == SelectionStrategy.class) {
              if (arg instanceof RouterConsumer) {
                SelectionStrategy annotationValue = ctor.getParameters()[i]
                    .getAnnotation(SelectionStrategy.class);
                ((RouterConsumer) arg).setRouter(annotationValue.value());
              }
            }
          }
        }
      }
    }
    return delegate.instantiate(bd, beanName, owner, ctor, args);
  }

  @Override
  public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner,
      Object factoryBean, Method factoryMethod, Object... args) throws BeansException {
    return delegate.instantiate(bd, beanName, owner, factoryBean, factoryMethod, args);
  }
}
