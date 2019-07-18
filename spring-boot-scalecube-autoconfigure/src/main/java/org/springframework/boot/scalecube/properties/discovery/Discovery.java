package org.springframework.boot.scalecube.properties.discovery;

import static org.springframework.boot.scalecube.properties.ScalecubeProperties.setProperty;

import io.scalecube.cluster.ClusterConfig;
import io.scalecube.cluster.metadata.MetadataDecoder;
import io.scalecube.cluster.metadata.MetadataEncoder;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.UnaryOperator;
import lombok.SneakyThrows;

/**
 * Properties for configuration discovery service.
 */
public class Discovery implements UnaryOperator<ClusterConfig> {

  private DiscoveryTransport transport = new DiscoveryTransport();
  private DiscoveryMembership membership = new DiscoveryMembership();
  private DiscoveryGossip gossip = new DiscoveryGossip();
  private DiscoveryFailureDetector failureDetector = new DiscoveryFailureDetector();
  private DiscoveryTransportType type = DiscoveryTransportType.LAN;

  private Map<String, String> metadata = new HashMap<>();

  private String memberHost;
  private Integer memberPort;


  // fixme metadata and type network
  @Override
  public ClusterConfig apply(ClusterConfig clusterConfig) {
    // FIXME: 16.07.2019 https://github.com/scalecube/scalecube-services/issues/631
    ClusterConfig tempConfig = clusterConfig;
//    switch (getType()) {
//      case WAN:
//        tempConfig = ClusterConfig
//            .defaultWanConfig();
//        break;
//      case LAN:
//        tempConfig = ClusterConfig.defaultLanConfig();
//        break;
//      case LOCAL:
//        tempConfig = ClusterConfig.defaultLocalConfig();
//        break;
//      default:
//        tempConfig = ClusterConfig.defaultConfig();
//    }
    tempConfig = tempConfig.transport(getTransport());
    tempConfig = tempConfig.membership(getMembership());
    tempConfig = tempConfig.gossip(getGossip());
    tempConfig = tempConfig.failureDetector(getFailureDetector());
    tempConfig = setProperty(tempConfig, tempConfig::memberHost, this::getMemberHost);
    tempConfig = setProperty(tempConfig, tempConfig::memberPort, this::getMemberPort);
    metadata.put("unic", UUID.randomUUID().toString());
    tempConfig = tempConfig.metadata(getMetadata());
    tempConfig = tempConfig.metadataDecoder(new MetadataDecoder() {


      @SneakyThrows
      @Override
      public Object decode(ByteBuffer buffer) {
        return Collections.singletonMap("unic", UUID.randomUUID().toString());
      }
    });

    tempConfig = tempConfig.metadataEncoder(new MetadataEncoder() {
      @SneakyThrows
      @Override
      public ByteBuffer encode(Object metadata) {
        Map<String, String> map = (Map<String, String>) metadata;
        String unic = map.get("unic");
        return ByteBuffer.wrap(("{\"unic\" : \"" + unic + "\"}").getBytes());
      }
    });
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

  public Map<String, String> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, String> metadata) {
    this.metadata = metadata;
  }
}
