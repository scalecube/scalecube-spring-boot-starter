package org.springframework.boot.scalecube.properties.discovery;

import static org.springframework.boot.scalecube.properties.discovery.Discovery.setProperty;

import io.scalecube.cluster.transport.api.MessageCodec;
import io.scalecube.cluster.transport.api.TransportConfig;
import io.scalecube.utils.ServiceLoaderUtil;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class DiscoveryTransport implements UnaryOperator<TransportConfig> {

  private Integer connectionTimeout;
  private Class<? extends MessageCodec> messageCodec;
  private Integer maxFrameLength;
  private Integer port;


  @Override
  public TransportConfig apply(TransportConfig transportConfig) {
    TransportConfig tempConfig = transportConfig;
    tempConfig = setProperty(tempConfig, tempConfig::connectTimeout, this::getConnectionTimeoutOpt);

    Optional<? extends MessageCodec> messageCodec = getMessageCodecOpt()
        .flatMap(ServiceLoaderUtil::findFirst);
    tempConfig = setProperty(tempConfig, tempConfig::messageCodec, () -> messageCodec);

    tempConfig = setProperty(tempConfig, tempConfig::maxFrameLength, this::getMaxFrameLengthOpt);
    tempConfig = setProperty(tempConfig, tempConfig::port, this::getPortOpt);

    return tempConfig;
  }



  public Integer getConnectionTimeout() {
    return connectionTimeout;
  }

  public Class<? extends MessageCodec> getMessageCodec() {
    return messageCodec;
  }

  public Integer getMaxFrameLength() {
    return maxFrameLength;
  }

  public Integer getPort() {
    return port;
  }

  public Optional<Integer> getConnectionTimeoutOpt() {
    return Optional.ofNullable(connectionTimeout);
  }

  public Optional<Class<? extends MessageCodec>> getMessageCodecOpt() {
    return Optional.ofNullable(messageCodec);
  }

  public Optional<Integer> getMaxFrameLengthOpt() {
    return Optional.ofNullable(maxFrameLength);
  }

  public Optional<Integer> getPortOpt() {
    return Optional.ofNullable(port);
  }



  public void setConnectionTimeout(Integer connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public void setMessageCodec(
      Class<? extends MessageCodec> messageCodec) {
    this.messageCodec = messageCodec;
  }

  public void setMaxFrameLength(Integer maxFrameLength) {
    this.maxFrameLength = maxFrameLength;
  }


  public void setPort(Integer port) {
    this.port = port;
  }
}
