package org.springframework.boot.scalecube.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.scalecube.services.transport.jackson.JacksonCodec;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
@AutoConfigureAfter(JacksonAutoConfiguration.class)
@ConditionalOnBean(ObjectMapper.class)
@ConditionalOnProperty(
    prefix = "spring.scalecube.transport", name = "data-codec-type",
    havingValue = "jackson", matchIfMissing = true)
public class JacksonCodecAutoConfiguration {

  @Bean
  public JacksonCodec jacksonMessageCodec(ObjectMapper objectMapper) {
    return new JacksonCodec(objectMapper);
  }


}
