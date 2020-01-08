package org.springframework.boot.scalecube.beans;

import io.scalecube.services.routing.Router;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;

/**
 * Utility class for creation {@link Router} from annotation value.
 */
interface RouterCreator {

  /**
   * Creates router from annotation value.
   * @param annotation annotation
   * @param beanFactory bean factory, if annotation has value as name of bean
   * @return
   */
  default Router router(SelectionStrategy annotation, BeanFactory beanFactory) {
    Class<? extends Router> routerType = annotation.routerType();
    String routerBeanName = annotation.router();
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
