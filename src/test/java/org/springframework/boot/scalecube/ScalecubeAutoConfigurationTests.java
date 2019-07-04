package org.springframework.boot.scalecube;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.scalecube.beans.EnableScalecubeClients;
import org.springframework.boot.scalecube.ScalecubeAutoConfigurationTests.C;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ScalecubeAutoConfiguration.class, C.class})
public class ScalecubeAutoConfigurationTests {

  @Autowired
  private ExampleService exampleService;

  @Test
  void name() {
    System.out.println(exampleService.toString());
  }

  @Configuration
  @EnableScalecubeClients(ExampleService.class)
  public static class C {

  }
}
