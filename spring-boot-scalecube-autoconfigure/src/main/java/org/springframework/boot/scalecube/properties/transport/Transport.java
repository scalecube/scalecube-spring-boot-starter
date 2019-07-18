package org.springframework.boot.scalecube.properties.transport;

import java.util.Optional;

public class Transport {

  private Integer port;

  private RSocketTransport rsocket;

  public Optional<Integer> getPort() {
    return Optional.ofNullable(port);
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public Optional<RSocketTransport> getRsocket() {
    return Optional.ofNullable(rsocket);
  }

  public void setRsocket(RSocketTransport rsocket) {
    this.rsocket = rsocket;
  }
}
