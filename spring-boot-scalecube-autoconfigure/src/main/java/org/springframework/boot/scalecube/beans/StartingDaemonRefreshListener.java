package org.springframework.boot.scalecube.beans;

import io.scalecube.services.Microservices;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class StartingDaemonRefreshListener implements ApplicationListener<ContextRefreshedEvent>,
    BeanClassLoaderAware, DisposableBean {

  private ClassLoader classLoader;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
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

  @Override
  public void destroy() throws Exception {

  }
}
