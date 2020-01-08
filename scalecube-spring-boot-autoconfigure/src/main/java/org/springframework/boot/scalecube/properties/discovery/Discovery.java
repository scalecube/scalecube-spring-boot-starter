package org.springframework.boot.scalecube.properties.discovery;

import static org.springframework.boot.scalecube.properties.ScalecubeProperties.setProperty;

import io.scalecube.cluster.ClusterConfig;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * Properties for configuration discovery service.
 */
public class Discovery implements UnaryOperator<ClusterConfig> {

  private DiscoveryTransport transport = new DiscoveryTransport();
  private DiscoveryMembership membership = new DiscoveryMembership();
  private DiscoveryGossip gossip = new DiscoveryGossip();
  private DiscoveryFailureDetector failureDetector = new DiscoveryFailureDetector();

  private String memberHost;
  private Integer memberPort;


  @Override
  public ClusterConfig apply(ClusterConfig clusterConfig) {
    ClusterConfig tempConfig = clusterConfig;
    tempConfig = tempConfig.transport(getTransport());
    tempConfig = tempConfig.membership(getMembership());
    tempConfig = tempConfig.gossip(getGossip());
    tempConfig = tempConfig.failureDetector(getFailureDetector());
    tempConfig = setProperty(tempConfig, tempConfig::memberHost, this::getMemberHost);
    tempConfig = setProperty(tempConfig, tempConfig::memberPort, this::getMemberPort);
    return tempConfig;
  }

  public DiscoveryTransport getTransport() {
    return transport;
  }

  public void setTransport(DiscoveryTransport transport) {
    this.transport = transport;
  }

  public DiscoveryMembership getMembership() {
    return membership;
  }

  public void setMembership(
      DiscoveryMembership membership) {
    this.membership = membership;
  }

  public DiscoveryGossip getGossip() {
    return gossip;
  }

  public void setGossip(DiscoveryGossip gossip) {
    this.gossip = gossip;
  }

  public DiscoveryFailureDetector getFailureDetector() {
    return failureDetector;
  }

  public void setFailureDetector(DiscoveryFailureDetector failureDetector) {
    this.failureDetector = failureDetector;
  }

  public Optional<String> getMemberHost() {
    return Optional.ofNullable(memberHost);
  }

  public void setMemberHost(String memberHost) {
    this.memberHost = memberHost;
  }

  public Optional<Integer> getMemberPort() {
    return Optional.ofNullable(memberPort);
  }

  public void setMemberPort(Integer memberPort) {
    this.memberPort = memberPort;
  }
}
