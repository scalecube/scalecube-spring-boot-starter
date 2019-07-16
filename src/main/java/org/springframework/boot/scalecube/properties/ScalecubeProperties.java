package org.springframework.boot.scalecube.properties;


import java.util.Optional;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.scalecube.properties.discovery.Discovery;

@ConfigurationProperties(prefix = "spring.scalecube")
public class ScalecubeProperties {

  private Transport transport;

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

  public static class Transport {

    private String host;
    private Integer port;

    public String getHost() {
      return host;
    }

    public Integer getPort() {
      return port;
    }

    public Optional<Integer> getPortIfExists() {
      return Optional.ofNullable(port);
    }

    public void setPort(Integer port) {
      this.port = port;
    }

    public Optional<String> getHostIfExists() {
      return Optional.ofNullable(host);
    }

    public void setHost(String host) {
      this.host = host;
    }
  }


}
