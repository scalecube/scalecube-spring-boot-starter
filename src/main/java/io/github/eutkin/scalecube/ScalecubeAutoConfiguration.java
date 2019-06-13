package io.github.eutkin.scalecube;

import io.github.eutkin.scalecube.properties.ScalecubeProperties;
import io.scalecube.services.Microservices;
import io.scalecube.services.annotations.Service;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootConfiguration
@EnableConfigurationProperties(ScalecubeProperties.class)
@ComponentScan("io.github.eutkin.scalecube")
public class ScalecubeAutoConfiguration {

    private ScalecubeProperties properties;

    @Bean
    public Microservices microservices(
            @Autowired(required = false) DiscoveryInitializer discoveryInitializer,
            List<ScalecubeTransportCustomizer> transportCustomizers,
            ListableBeanFactory beanFactory
    ) {


        Microservices.Builder builder = Microservices.builder();
        if (discoveryInitializer != null) {
            builder.discovery(discoveryInitializer);
        }
        ScalecubeTransportCustomizer transportCustomizer = transportCustomizers.stream().reduce(this::transport, (tc1, tc2) -> tc1.andThen(tc2));
        Map<String, Object> services = beanFactory.getBeansWithAnnotation(Service.class);

        return builder.transport(transportCustomizer).services(services).startAwait();
    }


    @Autowired
    public void setProperties(ScalecubeProperties properties) {
        this.properties = properties;
    }

    private Microservices.ServiceTransportBootstrap transport(Microservices.ServiceTransportBootstrap bootstrap) {
        Optional<String> host = properties.getTransportIfExist().flatMap(ScalecubeProperties.Transport::getHostIfExists);
        Optional<Integer> port = properties.getTransportIfExist().flatMap(ScalecubeProperties.Transport::getPortIfExists);
        bootstrap = host.map(bootstrap::host).orElse(bootstrap);
        bootstrap = port.map(bootstrap::port).orElse(bootstrap);
        return bootstrap;
    }


}
