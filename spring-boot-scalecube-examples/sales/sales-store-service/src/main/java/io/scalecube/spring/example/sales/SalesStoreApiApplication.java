package io.scalecube.spring.example.sales;

import io.scalecube.spring.example.domain.Sale;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

@SpringBootApplication
public class SalesStoreApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(SalesStoreApiApplication.class, args);
  }

  @Bean
  @ConfigurationPropertiesBinding
  public Converter<String, Year> yearConverter() {
    return new Converter<String, Year>() {
      @Override
      public Year convert(String source) {
        return Year.parse(source);
      }
    };
  }

  @Bean
  @ConfigurationProperties(prefix = "sales")
  public Map<String, Sale> sales() {
    return new HashMap<>();
  }

}
