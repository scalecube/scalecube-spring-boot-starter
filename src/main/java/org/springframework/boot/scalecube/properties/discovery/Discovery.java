package org.springframework.boot.scalecube.properties.discovery;

import io.scalecube.cluster.ClusterConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Discovery implements UnaryOperator<ClusterConfig> {

  private Map<String, String> metadata = new HashMap<>();

  private DiscoveryTransport transport = new DiscoveryTransport();
  private DiscoveryMembership membership = new DiscoveryMembership();
  private DiscoveryGossip gossip = new DiscoveryGossip();
  private DiscoveryFailureDetector failureDetector = new DiscoveryFailureDetector();
  private DiscoveryTransportType type = DiscoveryTransportType.LAN;

  private String memberHost;
  private Integer memberPort;


  @Override
  public ClusterConfig apply(ClusterConfig clusterConfig) {
    // FIXME: 16.07.2019 https://github.com/scalecube/scalecube-services/issues/631
    ClusterConfig tempConfig;
    switch (getType()) {
      case WAN:
        tempConfig = ClusterConfig
            .defaultWanConfig();
        break;
      case LAN:
        tempConfig = ClusterConfig.defaultLanConfig();
        break;
      case LOCAL:
        tempConfig = ClusterConfig.defaultLocalConfig();
        break;
      default:
        tempConfig = ClusterConfig.defaultConfig();
    }
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

  public DiscoveryTransportType getType() {
    return type;
  }

  public void setType(DiscoveryTransportType type) {
    this.type = type;
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

  static <T, C> C setProperty(C config, Function<T, C> c, Supplier<Optional<T>> property) {
    return property.get().map(c).orElse(config);
  }
}
