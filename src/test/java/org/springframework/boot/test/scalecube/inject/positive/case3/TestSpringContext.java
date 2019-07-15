package org.springframework.boot.test.scalecube.inject.positive.case3;

import org.springframework.boot.scalecube.beans.EnableScalecubeClients;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.scalecube.example.RemoteServiceClient;
import org.springframework.boot.test.scalecube.example.ThirdLocalService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@EnableScalecubeClients({RemoteServiceClient.class})
@ComponentScan(basePackageClasses = LocalServiceInjectInLocalServiceTest.class)
public class TestSpringContext {

  @Bean
  public LocalServiceFromFactoryMethod customBeanFromFactoryMethod(ThirdLocalService service) {
    return new LocalServiceFromFactoryMethod(service);
  }
}
