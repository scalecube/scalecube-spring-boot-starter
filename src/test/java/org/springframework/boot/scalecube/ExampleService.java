package org.springframework.boot.scalecube;

import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;
import reactor.core.publisher.Flux;

@Service
public interface ExampleService {

  @ServiceMethod
  Flux<String> get(Flux<String> input);

}
