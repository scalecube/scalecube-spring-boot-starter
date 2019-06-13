package io.github.eutkin.scalecube;

import io.scalecube.services.transport.rsocket.RSocketServiceTransport;
import io.scalecube.services.transport.rsocket.RSocketTransportResources;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class RSocketTransportAutoConfiguration {

    @Bean
    @ConditionalOnClass(name = "io.scalecube.services.transport.rsocket.RSocketServiceTransport")
    public ScalecubeTransportCustomizer transportInitializer() {
        return bootstrap -> bootstrap
                .resources(RSocketTransportResources::new)
                .server(RSocketServiceTransport.INSTANCE::serverTransport)
                .client(RSocketServiceTransport.INSTANCE::clientTransport);
    }

}
