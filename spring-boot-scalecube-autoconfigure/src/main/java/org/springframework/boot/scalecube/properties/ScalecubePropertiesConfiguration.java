package org.springframework.boot.scalecube.properties;

import io.scalecube.net.Address;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

@SpringBootConfiguration
@EnableConfigurationProperties({
    ScalecubeProperties.class
})
public class ScalecubePropertiesConfiguration {


  @Bean
  @ConfigurationPropertiesBinding
  public Converter<String, Address> addressConverter() {
    return new Converter<String, Address>() {
      @Override
      public Address convert(String source) {
        return Address.from(source);
      }
    };
  }
}
