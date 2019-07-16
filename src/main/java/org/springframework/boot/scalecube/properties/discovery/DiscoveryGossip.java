package org.springframework.boot.scalecube.properties.discovery;

import static org.springframework.boot.scalecube.properties.discovery.Discovery.setProperty;

import io.scalecube.cluster.gossip.GossipConfig;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class DiscoveryGossip implements UnaryOperator<GossipConfig> {

  private Integer fanout;
  private Long interval;
  private Integer repeatMult;

  @Override
  public GossipConfig apply(GossipConfig gossipConfig) {
    GossipConfig tempConfig = gossipConfig;
    tempConfig = setProperty(tempConfig, tempConfig::gossipFanout, this::getFanout);
    tempConfig = setProperty(tempConfig, tempConfig::gossipInterval, this::getInterval);
    tempConfig = setProperty(tempConfig, tempConfig::gossipRepeatMult, this::getRepeatMult);
    return tempConfig;
  }

  public Optional<Integer> getFanout() {
    return Optional.ofNullable(fanout);
  }

  public void setFanout(Integer fanout) {
    this.fanout = fanout;
  }

  public Optional<Long> getInterval() {
    return Optional.ofNullable(interval);
  }

  public void setInterval(Long interval) {
    this.interval = interval;
  }

  public Optional<Integer> getRepeatMult() {
    return Optional.ofNullable(repeatMult);
  }

  public void setRepeatMult(Integer repeatMult) {
    this.repeatMult = repeatMult;
  }
}
