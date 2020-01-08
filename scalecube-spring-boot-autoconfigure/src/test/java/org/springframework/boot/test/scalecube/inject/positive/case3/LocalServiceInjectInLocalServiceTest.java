package org.springframework.boot.test.scalecube.inject.positive.case3;

import io.scalecube.services.Microservices;
import io.scalecube.services.discovery.ScalecubeServiceDiscovery;
import io.scalecube.services.transport.rsocket.RSocketServiceTransport;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.scalecube.ScalecubeAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.scalecube.example.RemoteServiceClient;
import org.springframework.boot.test.scalecube.example.ThirdLocalService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

/**
 * Checks injection mechanism proxy for external scalecube service in user's defined bean.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
    TestSpringContext.class, ScalecubeAutoConfiguration.class
})
class LocalServiceInjectInLocalServiceTest {

  @Autowired
  private FirstLocalServiceImpl someInternalService;

  @Autowired
  private LocalServiceFromFactoryMethod localServiceFromFactoryMethod;

  @Autowired
  private Microservices microservices;

  @BeforeEach
  void setUp() {
    RemoteServiceClient service1 = input -> input;
    Microservices
        .builder()
        .services(service1)
        .discovery(endpoint -> new ScalecubeServiceDiscovery(endpoint)
            .options(
                opts -> opts.membership(o -> o.seedMembers(microservices.discovery().address())))
        )
        .transport(RSocketServiceTransport::new)
        .startAwait();
  }

  @Test
  void check() {
    callService(someInternalService::getInjectedThroughConstructor);
    callService(someInternalService::getInjectedThroughField);
    callService(someInternalService::getInjectedThroughSetter);
  }

  @Test
  void checkFactoryMethod() {
    callService(localServiceFromFactoryMethod::getInjectedThroughConstructor);
    callService(localServiceFromFactoryMethod::getInjectedThroughField);
    callService(localServiceFromFactoryMethod::getInjectedThroughSetter);
  }

  private void callService(Supplier<ThirdLocalService> service) {
    service.get().identity(Flux.just("A")).blockLast();
  }


}
