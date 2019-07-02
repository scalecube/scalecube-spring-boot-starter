package org.springframework.boot.scalecube;

import io.scalecube.services.Microservices;
import io.scalecube.services.annotations.Service;
import io.scalecube.services.transport.api.HeadersCodec;
import io.scalecube.services.transport.api.ServiceMessageCodec;
import io.scalecube.services.transport.api.ServiceTransport;
import java.util.Map;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.scalecube.discovery.DiscoveryInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@ComponentScan("org.springframework.boot.scalecube")
public class ScalecubeAutoConfiguration {

    @Bean
    public ServiceMessageCodec serviceMessageCodec(HeadersCodec headersCodec) {
        return new ServiceMessageCodec(headersCodec);
    }

    @Bean
    public Microservices microservices(
            @Autowired(required = false) DiscoveryInitializer discoveryInitializer,
            ServiceTransport serviceTransport,
            ListableBeanFactory beanFactory
    ) {
        Microservices.Builder builder = Microservices.builder();
        if (discoveryInitializer != null) {
            builder.discovery(discoveryInitializer);
        }
        builder.transport(opts -> opts.serviceTransport(() -> serviceTransport));

        Map<String, Object> services = beanFactory.getBeansWithAnnotation(Service.class);

        return builder.services(services).startAwait();
    }

}
