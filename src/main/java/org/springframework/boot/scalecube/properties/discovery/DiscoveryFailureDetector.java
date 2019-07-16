package org.springframework.boot.scalecube.properties.discovery;

import static org.springframework.boot.scalecube.properties.discovery.Discovery.setProperty;

import io.scalecube.cluster.fdetector.FailureDetectorConfig;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class DiscoveryFailureDetector implements UnaryOperator<FailureDetectorConfig> {

  private Integer pingInterval;
  private Integer pingTimeout;
  private Integer pingReqMembers;

  @Override
  public FailureDetectorConfig apply(FailureDetectorConfig failureDetectorConfig) {
    FailureDetectorConfig tempConfig = failureDetectorConfig;
    tempConfig = setProperty(tempConfig, tempConfig::pingInterval, this::getPingInterval);
    tempConfig = setProperty(tempConfig, tempConfig::pingReqMembers, this::getPingReqMembers);
    tempConfig = setProperty(tempConfig, tempConfig::pingTimeout, this::getPingTimeout);
    return tempConfig;
  }

  public Optional<Integer> getPingInterval() {
    return Optional.ofNullable(pingInterval);
  }

  public void setPingInterval(Integer pingInterval) {
    this.pingInterval = pingInterval;
  }

  public Optional<Integer> getPingTimeout() {
    return Optional.ofNullable(pingTimeout);
  }

  public void setPingTimeout(Integer pingTimeout) {
    this.pingTimeout = pingTimeout;
  }

  public Optional<Integer> getPingReqMembers() {
    return Optional.ofNullable(pingReqMembers);
  }

  public void setPingReqMembers(Integer pingReqMembers) {
    this.pingReqMembers = pingReqMembers;
  }
}
