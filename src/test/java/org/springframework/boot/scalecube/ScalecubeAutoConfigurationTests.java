package org.springframework.boot.scalecube;

import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;
import io.scalecube.services.routing.RandomServiceRouter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.scalecube.ScalecubeAutoConfigurationTests.C;
import org.springframework.boot.scalecube.beans.EnableScalecubeClients;
import org.springframework.boot.scalecube.beans.SelectionStrategy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ScalecubeAutoConfiguration.class, C.class})
public class ScalecubeAutoConfigurationTests {

  @Autowired
  private BusinessService businessService;

  @Autowired
  private BeanFactory beanFactory;

  @Autowired
  private ObjectFactory<ExampleService> exampleService;


  @Test
  void name() {
    BusinessService bean1 = beanFactory.getBean(BusinessService.class);
    SomeBusinessService bean2 = beanFactory.getBean(SomeBusinessService.class);
  }

  @Configuration
  @EnableScalecubeClients(ExampleService.class)
  public static class C {


  }

  @Component
  public static class BusinessService {

    @SelectionStrategy(RandomServiceRouter.class)
    private final ExampleService service;

    public BusinessService(ExampleService service) {
      this.service = service;
    }

    public Flux<String> go() {
      return service.get(Flux.just("hello", "world"));
    }
  }

  @Service
  interface S {

    @ServiceMethod
    Flux<String> go();
  }

  @Component
  public static class SomeBusinessService implements S {

    private final ExampleService service;

    public SomeBusinessService(ExampleService service) {
      this.service = service;
    }

    @Override
    public Flux<String> go() {
      return service.get(Flux.just("hello", "world"));
    }
  }
}
