package org.springframework.boot.test.scalecube.inject.positive.case1;

import org.springframework.boot.scalecube.beans.EnableScalecube;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.scalecube.example.RemoteServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@EnableScalecube(remoteServices = {RemoteServiceClient.class})
@ComponentScan(basePackageClasses = EssInjectInCustomerBeanTest.class)
public class TestSpringContext {

  @Bean
  public CustomBeanFromFactoryMethod customBeanFromFactoryMethod(RemoteServiceClient service) {
    return new CustomBeanFromFactoryMethod(service);
  }
}
