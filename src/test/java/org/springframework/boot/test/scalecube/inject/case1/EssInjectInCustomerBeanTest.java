package org.springframework.boot.test.scalecube.inject.case1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.scalecube.services.Microservices;
import io.scalecube.services.discovery.ScalecubeServiceDiscovery;
import io.scalecube.services.transport.rsocket.RSocketServiceTransport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.scalecube.ScalecubeAutoConfiguration;
import org.springframework.boot.scalecube.beans.EnableScalecubeClients;
import org.springframework.boot.test.scalecube.example.ExternalScalecubeService1;
import org.springframework.boot.test.scalecube.example.ExternalScalecubeService2;
import org.springframework.boot.test.scalecube.inject.case1.EssInjectInCustomerBeanTest.EssInjectInCustomerBeanContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
    EssInjectInCustomerBeanContext.class, ScalecubeAutoConfiguration.class
})
public class EssInjectInCustomerBeanTest {

  @Autowired
  private CustomerBean customerBean;

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
  void check() {
    String result1 = customerBean
        .externalScalecubeService1
        .lower(Flux.just("A"))
        .blockLast();
    assertEquals("a", result1);

    String result2 = customerBean.externalScalecubeService2
        .lower(Flux.just("A"))
        .blockLast();
    assertEquals("a", result2);
  }

  @TestConfiguration
  @EnableScalecubeClients({ExternalScalecubeService1.class, ExternalScalecubeService2.class})
  @ComponentScan("org.springframework.boot.test.scalecube.inject.case1")
  static class EssInjectInCustomerBeanContext {

  }

  @Component
  static class CustomerBean {

    private final ExternalScalecubeService1 externalScalecubeService1;

    private ExternalScalecubeService2 externalScalecubeService2;

    CustomerBean(
        ExternalScalecubeService1 externalScalecubeService1) {
      this.externalScalecubeService1 = externalScalecubeService1;
    }

    @Autowired
    public CustomerBean setExternalScalecubeService2(
        ExternalScalecubeService2 externalScalecubeService2) {
      this.externalScalecubeService2 = externalScalecubeService2;
      return this;
    }
  }
}
