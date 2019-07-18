package org.springframework.boot.scalecube.beans;

import io.scalecube.services.annotations.Service;
import io.scalecube.services.routing.Router;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

/**
 * BeanPostProcessor for {@link SelectionStrategy}.
 */
class SelectionStrategyPostProcessor implements BeanPostProcessor, BeanFactoryAware,
    RouterCreator {

  static final String BEAN_NAME = "selectionStrategyBeanPostProcessor";

  private static final Logger logger = LoggerFactory
      .getLogger(SelectionStrategyPostProcessor.class);

  private BeanFactory beanFactory;

  /**
   * Injects custom router in handling bean.
   *
   * @param bean the new bean instance
   * @param beanName the name of the bean
   * @return the bean instance to use, either the original or a wrapped one; if {@code null}, no
   * subsequent BeanPostProcessors will be invoked
   * @throws org.springframework.beans.BeansException in case of errors
   * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet
   */
  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    customizeRouterThroughSetter(bean);
    customizeRouterOnThroughField(bean);
    return bean;
  }

  private void customizeRouterThroughSetter(Object bean) {
    Class<?> targetClass = bean.getClass();
    do {
      ReflectionUtils.doWithLocalMethods(bean.getClass(), method -> {
        PropertyDescriptor property = BeanUtils.findPropertyForMethod(method);
        if (property == null) {
          return;
        }
        if (!isAutowireExternalService(method)) {
          return;
        }
        String propertyName = property.getName();
        Class<?> propertyType = property.getPropertyType();
        Object externalService;
        Method readMethod = property.getReadMethod();
        if (readMethod != null) {
          externalService = ReflectionUtils.invokeMethod(readMethod, bean);
        } else {
          Field field = ReflectionUtils.findField(bean.getClass(), propertyName, propertyType);
          ReflectionUtils.makeAccessible(field);
          externalService = ReflectionUtils.getField(field, bean);
        }
        if (externalService instanceof RouterConsumer) {
          Method writeMethod = property.getWriteMethod();
          Parameter parameter = writeMethod.getParameters()[0];
          SelectionStrategy annotation = parameter.getAnnotation(SelectionStrategy.class);
          Router router = router(annotation, beanFactory);
          ((RouterConsumer) externalService).setRouter(router);
        }
      });
      targetClass = targetClass.getSuperclass();
    }
    while (targetClass != null && targetClass != Object.class);
  }

  private void customizeRouterOnThroughField(final Object bean) {
    Class<?> targetClass = bean.getClass();
    do {
      ReflectionUtils.doWithLocalFields(targetClass, field -> {
        SelectionStrategy annotation = field.getAnnotation(SelectionStrategy.class);
        if (annotation != null) {
          if (Modifier.isStatic(field.getModifiers())) {
            logger.info("Static fields not supported for injection of custom router.");
            return;
          }
          Router router = router(annotation, beanFactory);
          ReflectionUtils.makeAccessible(field);
          RouterConsumer externalService = (RouterConsumer) field.get(bean);
          externalService.setRouter(router);
        }
      });
      targetClass = targetClass.getSuperclass();
    }
    while (targetClass != null && targetClass != Object.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  private boolean isAutowireExternalService(Method method) {
    boolean isAutowire = method.isAnnotationPresent(Autowired.class);
    boolean hasExternalServiceAsArg = false;
    for (Parameter parameter : method.getParameters()) {
      boolean b = AnnotationUtils.findAnnotation(parameter.getType(), Service.class) != null &&
          parameter.isAnnotationPresent(SelectionStrategy.class);
      hasExternalServiceAsArg = hasExternalServiceAsArg || b;
    }
    return isAutowire && hasExternalServiceAsArg;
  }

}
