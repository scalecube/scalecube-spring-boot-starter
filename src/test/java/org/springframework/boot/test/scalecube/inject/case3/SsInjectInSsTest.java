package org.springframework.boot.test.scalecube.inject.case3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.scalecube.services.Microservices;
import io.scalecube.services.discovery.ScalecubeServiceDiscovery;
import io.scalecube.services.routing.RandomServiceRouter;
import io.scalecube.services.transport.rsocket.RSocketServiceTransport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.scalecube.ScalecubeAutoConfiguration;
import org.springframework.boot.scalecube.beans.EnableScalecubeClients;
import org.springframework.boot.scalecube.beans.SelectionStrategy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.scalecube.example.ExternalScalecubeService1;
import org.springframework.boot.test.scalecube.example.ExternalScalecubeService2;
import org.springframework.boot.test.scalecube.example.InternalScalecubeService1;
import org.springframework.boot.test.scalecube.example.InternalScalecubeService2;
import org.springframework.boot.test.scalecube.inject.case3.SsInjectInSsTest.SsInjectInSsContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
    SsInjectInSsContext.class, ScalecubeAutoConfiguration.class
})
public class SsInjectInSsTest {

  @Autowired
  private InternalScalecubeService1 internalScalecubeService;

  @Autowired
  private Microservices microservices;

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
  void checkInjectedField() {
    SomeInternalScalecubeService service = (SomeInternalScalecubeService) this.internalScalecubeService;
    String result1 = service.internalScalecubeService1
        .lower(Flux.just("A"))
        .blockLast();
    assertEquals("A", result1);

    String result2 = service.internalScalecubeService2
        .lower(Flux.just("A"))
        .blockLast();
    assertEquals("A", result2);
  }

  @Configuration
  @EnableScalecubeClients({ExternalScalecubeService1.class, ExternalScalecubeService2.class})
  @ComponentScan("org.springframework.boot.test.scalecube.inject.case3")
  static class SsInjectInSsContext {

    @Bean
    public InternalScalecubeService2 service2() {
      return input -> input;
    }
  }

  @Component("internal-service")
  static class SomeInternalScalecubeService implements InternalScalecubeService1 {

    private final InternalScalecubeService2 internalScalecubeService1;

    private InternalScalecubeService2 internalScalecubeService2;

    SomeInternalScalecubeService(@SelectionStrategy(routerType = RandomServiceRouter.class)
        InternalScalecubeService2 internalScalecubeService1) {
      this.internalScalecubeService1 = internalScalecubeService1;
    }

    @Autowired
    public void setInternalScalecubeService2(
        InternalScalecubeService2 internalScalecubeService2) {
      this.internalScalecubeService2 = internalScalecubeService2;
    }

    @Override
    public Flux<String> lower(Flux<String> input) {
      return internalScalecubeService1.lower(input);
    }
  }
}
