package org.springframework.boot.test.scalecube.inject.case2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.scalecube.services.Microservices;
import io.scalecube.services.discovery.ScalecubeServiceDiscovery;
import io.scalecube.services.transport.rsocket.RSocketServiceTransport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.scalecube.ScalecubeAutoConfiguration;
import org.springframework.boot.scalecube.beans.EnableScalecubeClients;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.scalecube.example.ExternalScalecubeService1;
import org.springframework.boot.test.scalecube.example.ExternalScalecubeService2;
import org.springframework.boot.test.scalecube.example.InternalScalecubeService1;
import org.springframework.boot.test.scalecube.inject.case2.EssInjectInSsTest.EssInjectInSsContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
    EssInjectInSsContext.class, ScalecubeAutoConfiguration.class
})
public class EssInjectInSsTest {

  @Autowired
  private SomeInternalScalecubeService internalScalecubeService;

  @Autowired
  private Microservices microservices;

  @Autowired
  private BeanFactory beanFactory;

  @BeforeEach
  void setUp() {
    ExternalScalecubeService1 service1 = input -> input.map(String::toLowerCase);
    ExternalScalecubeService2 service2 = input -> input.map(String::toLowerCase);
    Microservices
        .builder()
        .services(service1, service2)
        .discovery(endpoint -> new ScalecubeServiceDiscovery(endpoint)
            .options(opts -> opts.seedMembers(microservices.discovery().address()))
        )
        .transport(opts -> opts.serviceTransport(RSocketServiceTransport::new))
        .startAwait();
  }

  @Test
  void check() {
  String result1 = internalScalecubeService
        .externalScalecubeService1
        .lower(Flux.just("A"))
        .blockLast();
    assertEquals("a", result1);

    String result2 = internalScalecubeService.externalScalecubeService2
        .lower(Flux.just("A"))
        .blockLast();
    assertEquals("a", result2);
  }

  @Configuration
  @EnableScalecubeClients({ExternalScalecubeService1.class, ExternalScalecubeService2.class})
  @ComponentScan("org.springframework.boot.test.scalecube.inject.case2")
  static class EssInjectInSsContext {

  }

  @Component("internal-service")
  static class SomeInternalScalecubeService implements InternalScalecubeService1 {

    private final ExternalScalecubeService1 externalScalecubeService1;

    private ExternalScalecubeService2 externalScalecubeService2;

    SomeInternalScalecubeService(
        ExternalScalecubeService1 externalScalecubeService1) {
      this.externalScalecubeService1 = externalScalecubeService1;
    }

    @Autowired
    public SomeInternalScalecubeService setExternalScalecubeService2(
        ExternalScalecubeService2 externalScalecubeService2) {
      this.externalScalecubeService2 = externalScalecubeService2;
      return this;
    }

    @Override
    public Flux<String> lower(Flux<String> input) {
      return externalScalecubeService1.lower(input);
    }
  }
}
