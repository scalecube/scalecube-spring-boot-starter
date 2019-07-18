package org.springframework.boot.test.scalecube.inject.positive.case2;

import org.springframework.boot.scalecube.beans.EnableScalecube;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.scalecube.example.RemoteServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@EnableScalecube({RemoteServiceClient.class})
@ComponentScan(basePackageClasses = RemoteServiceClientInjectInLocalServiceTest.class)
public class TestSpringContext {

  @Bean
  public InternalServiceFromFactoryMethod customBeanFromFactoryMethod(RemoteServiceClient service) {
    return new InternalServiceFromFactoryMethod(service);
  }
}
