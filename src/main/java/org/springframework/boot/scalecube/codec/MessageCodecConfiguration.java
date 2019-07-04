package org.springframework.boot.scalecube.codec;

import io.scalecube.services.transport.api.HeadersCodec;
import io.scalecube.services.transport.api.ServiceMessageCodec;
import io.scalecube.services.transport.jackson.JacksonCodec;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class MessageCodecConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public HeadersCodec defaultHeadersCodec() {
    return new JacksonCodec();
  }

  @Bean
  @ConditionalOnMissingBean
  public ServiceMessageCodec serviceMessageCodec(HeadersCodec headersCodec) {
    return new ServiceMessageCodec(headersCodec);
  }

}
