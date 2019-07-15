package org.springframework.boot.scalecube.transport;

import io.scalecube.services.transport.api.ServiceMessageCodec;
import io.scalecube.services.transport.api.ServiceTransport;
import io.scalecube.services.transport.rsocket.RSocketServiceTransport;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
@ConditionalOnClass(RSocketServiceTransport.class)
@ConditionalOnBean(ServiceMessageCodec.class)
public class RSocketTransportAutoConfiguration {


  @Bean
  public ServiceTransport rsocketServiceTransport() {
    return new RSocketServiceTransport();
  }
}
