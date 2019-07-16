package org.springframework.boot.scalecube.properties.discovery;

import static org.springframework.boot.scalecube.properties.discovery.Discovery.setProperty;

import io.scalecube.cluster.membership.MembershipConfig;
import io.scalecube.net.Address;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class DiscoveryMembership implements UnaryOperator<MembershipConfig> {

  private List<Address> seedMembers;
  private Integer syncInterval;
  private Integer syncTimeout;
  private Integer suspicionMult;
  private String syncGroup;

  public Optional<List<Address>> getSeedMembers() {
    return Optional.ofNullable(seedMembers);
  }

  public void setSeedMembers(List<Address> seedMembers) {
    this.seedMembers = seedMembers;
  }

  public Optional<Integer> getSyncInterval() {
    return Optional.ofNullable(syncInterval);
  }

  public void setSyncInterval(Integer syncInterval) {
    this.syncInterval = syncInterval;
  }

  public Optional<Integer> getSyncTimeout() {
    return Optional.ofNullable(syncTimeout);
  }

  public void setSyncTimeout(Integer syncTimeout) {
    this.syncTimeout = syncTimeout;
  }

  public Optional<Integer> getSuspicionMult() {
    return Optional.ofNullable(suspicionMult);
  }

  public void setSuspicionMult(Integer suspicionMult) {
    this.suspicionMult = suspicionMult;
  }

  public Optional<String> getSyncGroup() {
    return Optional.ofNullable(syncGroup);
  }

  public void setSyncGroup(String syncGroup) {
    this.syncGroup = syncGroup;
  }

  @Override
  public MembershipConfig apply(MembershipConfig membershipConfig) {
    MembershipConfig tempConfig = membershipConfig;
    tempConfig = setProperty(tempConfig, tempConfig::seedMembers, this::getSeedMembers);
    tempConfig = setProperty(tempConfig, tempConfig::syncGroup, this::getSyncGroup);
    tempConfig = setProperty(tempConfig, tempConfig::suspicionMult, this::getSuspicionMult);
    tempConfig = setProperty(tempConfig, tempConfig::syncInterval, this::getSyncInterval);
    tempConfig = setProperty(tempConfig, tempConfig::syncTimeout, this::getSyncTimeout);
    return tempConfig;
  }

}
