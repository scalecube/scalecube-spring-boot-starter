package org.springframework.boot.scalecube.codec;

import io.scalecube.services.transport.api.DataCodec;
import io.scalecube.services.transport.api.DefaultHeadersCodec;
import io.scalecube.services.transport.api.HeadersCodec;
import io.scalecube.services.transport.api.ServiceMessageCodec;
import java.util.Collection;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class MessageCodecConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public HeadersCodec defaultHeadersCodec() {
    return new DefaultHeadersCodec();
  }

  @Bean
  @ConditionalOnMissingBean
  public ServiceMessageCodec serviceMessageCodec(HeadersCodec headersCodec,
      Collection<DataCodec> dataCodecs) {
    return new ServiceMessageCodec(headersCodec, dataCodecs);
  }

}
