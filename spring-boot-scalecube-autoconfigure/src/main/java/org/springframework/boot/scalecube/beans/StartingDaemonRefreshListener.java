package org.springframework.boot.scalecube.beans;

import io.scalecube.services.Microservices;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.ClassUtils;

public class StartingDaemonRefreshListener implements ApplicationListener<ContextRefreshedEvent>,
    BeanClassLoaderAware {

  private static final String SERVLET_APPLICATION_CONTEXT_CLASS =
      "org.springframework.web.context.WebApplicationContext";

  private static final String REACTIVE_APPLICATION_CONTEXT_CLASS =
      "org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext";

  private ClassLoader classLoader;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    Class<?> applicationContextClass = event.getApplicationContext().getClass();
    boolean webApp =
        isAssignable(SERVLET_APPLICATION_CONTEXT_CLASS, applicationContextClass) || isAssignable(
            REACTIVE_APPLICATION_CONTEXT_CLASS, applicationContextClass);
    if (webApp) {
      return;
    }
    Thread awaitThread = new Thread(() -> {
      Microservices microservices = event.getApplicationContext().getBean(Microservices.class);
      microservices.onShutdown().block();
    });
    awaitThread.setDaemon(false);
    awaitThread.setContextClassLoader(classLoader);
    awaitThread.setName("await");
    awaitThread.start();
  }

  @Override
  public void setBeanClassLoader(ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  private static boolean isAssignable(String target, Class<?> type) {
    try {
      return ClassUtils.resolveClassName(target, null).isAssignableFrom(type);
    }
    catch (Throwable ex) {
      return false;
    }
  }

}
