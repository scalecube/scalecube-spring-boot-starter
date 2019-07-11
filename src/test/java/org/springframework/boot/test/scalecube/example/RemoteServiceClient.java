package org.springframework.boot.test.scalecube.example;

import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;
import reactor.core.publisher.Flux;

@Service("sample-external-service")
public interface RemoteServiceClient {

  @ServiceMethod
  Flux<String> identity(Flux<String> input);

}
