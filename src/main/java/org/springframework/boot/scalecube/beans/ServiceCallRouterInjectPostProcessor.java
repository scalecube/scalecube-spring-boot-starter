package org.springframework.boot.scalecube.beans;

import io.scalecube.services.annotations.Service;
import java.lang.reflect.Field;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

@Component
@DependsOn("microservices")
public class ServiceCallRouterInjectPostProcessor implements BeanPostProcessor {

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {

    Class<?> beanClass = bean.getClass();
    for (Field field : beanClass.getDeclaredFields()) {
      Class<?> fieldType = field.getType();
      SelectionStrategy annotation = field.getAnnotation(SelectionStrategy.class);
      if (AnnotationUtils.findAnnotation(fieldType, Service.class) != null && annotation != null) {
        boolean accessible = field.isAccessible();
        try {
          field.setAccessible(true);
          Object service = field.get(bean);
          if (service instanceof ServiceCallAware) {
            ((ServiceCallAware) service).setRouter(annotation.value());
          }
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        } finally {
          field.setAccessible(accessible);
        }
      }
    }

    return bean;
  }

}
