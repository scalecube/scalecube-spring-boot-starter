package org.springframework.boot.scalecube.properties;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.scalecube.properties.discovery.Discovery;
import org.springframework.boot.scalecube.properties.transport.Transport;

@ConfigurationProperties(prefix = "spring.scalecube")
public class ScalecubeProperties {

  private Transport transport;

  /** Discovery service. */
  private Discovery discovery;

  public Transport getTransport() {
    return transport;
  }

  public Discovery getDiscovery() {
    return discovery;
  }

  public Optional<Transport> getTransportIfExist() {
    return Optional.ofNullable(transport);
  }

  public void setTransport(Transport transport) {
    this.transport = transport;
  }

  public Optional<Discovery> getDiscoveryIfExists() {
    return Optional.ofNullable(discovery);
  }

  public void setDiscovery(Discovery discovery) {
    this.discovery = discovery;
  }

  public static <T, C> C setProperty(C config, Function<T, C> c, Supplier<Optional<T>> property) {
    return property.get().map(c).orElse(config);
  }
}
