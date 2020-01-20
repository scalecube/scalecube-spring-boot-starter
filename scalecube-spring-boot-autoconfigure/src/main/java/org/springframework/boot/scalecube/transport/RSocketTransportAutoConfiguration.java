package org.springframework.boot.scalecube.transport;

import static org.springframework.boot.scalecube.properties.ScalecubeProperties.setProperty;

import io.scalecube.services.transport.api.ServiceMessageCodec;
import io.scalecube.services.transport.api.ServiceTransport;
import io.scalecube.services.transport.rsocket.RSocketServiceTransport;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.scalecube.properties.ScalecubeProperties;
import org.springframework.boot.scalecube.properties.ScalecubePropertiesConfiguration;
import org.springframework.boot.scalecube.properties.transport.RSocketTransport;
import org.springframework.boot.scalecube.properties.transport.Transport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import reactor.netty.resources.LoopResources;
import reactor.netty.tcp.TcpServer;

@SpringBootConfiguration
@Import(ScalecubePropertiesConfiguration.class)
@ConditionalOnClass(RSocketServiceTransport.class)
@ConditionalOnBean(ServiceMessageCodec.class)
public class RSocketTransportAutoConfiguration {

  /**
   * {@link ServiceTransport} bean.
   *
   * @return instance of {@link ServiceTransport}
   */
  @Bean
  public ServiceTransport rsocketServiceTransport(ScalecubeProperties properties) {
    RSocketServiceTransport serviceTransport = new RSocketServiceTransport();

    Optional<Function<LoopResources, TcpServer>> portCustomizerOpt =
        properties
            .getTransportIfExist()
            .flatMap(Transport::getPort)
            .map(port -> loopResources -> TcpServer.create().runOn(loopResources).port(port));
    Optional<Integer> workerCountOpt =
        properties
            .getTransportIfExist()
            .flatMap(Transport::getRsocket)
            .flatMap(RSocketTransport::getWorkerCount);
    serviceTransport =
        setProperty(serviceTransport, serviceTransport::tcpServer, () -> portCustomizerOpt);
    serviceTransport =
        setProperty(serviceTransport, serviceTransport::numOfWorkers, () -> workerCountOpt);

    return serviceTransport;
  }
}
