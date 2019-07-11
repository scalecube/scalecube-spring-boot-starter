package org.springframework.boot.scalecube.beans;

import io.scalecube.services.routing.Router;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;

interface RouterInitializer {

  default Router router(SelectionStrategy annotation, BeanFactory beanFactory) {
    Class<? extends Router> routerType = annotation.routerType();
    String routerBeanName = annotation.routerBeanName();
    Router router;
    if (!routerBeanName.isEmpty()) {
      router = beanFactory.getBean(routerBeanName, Router.class);
    } else {
      if (routerType != Router.class) {
        router = BeanUtils.instantiateClass(routerType);
      } else {
        router = null;
      }
    }
    return router;
  }

}
