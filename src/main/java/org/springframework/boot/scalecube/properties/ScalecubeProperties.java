package org.springframework.boot.scalecube.properties;


import static io.scalecube.cluster.ClusterConfig.DEFAULT_GOSSIP_FANOUT;
import static io.scalecube.cluster.ClusterConfig.DEFAULT_GOSSIP_INTERVAL;
import static io.scalecube.cluster.ClusterConfig.DEFAULT_GOSSIP_REPEAT_MULT;
import static io.scalecube.cluster.ClusterConfig.DEFAULT_MEMBER_HOST;
import static io.scalecube.cluster.ClusterConfig.DEFAULT_MEMBER_PORT;
import static io.scalecube.cluster.ClusterConfig.DEFAULT_METADATA_TIMEOUT;
import static io.scalecube.cluster.ClusterConfig.DEFAULT_PING_INTERVAL;
import static io.scalecube.cluster.ClusterConfig.DEFAULT_PING_REQ_MEMBERS;
import static io.scalecube.cluster.ClusterConfig.DEFAULT_PING_TIMEOUT;
import static io.scalecube.cluster.ClusterConfig.DEFAULT_SUSPICION_MULT;
import static io.scalecube.cluster.ClusterConfig.DEFAULT_SYNC_GROUP;
import static io.scalecube.cluster.ClusterConfig.DEFAULT_SYNC_INTERVAL;
import static io.scalecube.cluster.ClusterConfig.DEFAULT_SYNC_TIMEOUT;
import static io.scalecube.cluster.transport.api.TransportConfig.DEFAULT_CONNECT_TIMEOUT;
import static io.scalecube.cluster.transport.api.TransportConfig.DEFAULT_MAX_FRAME_LENGTH;
import static io.scalecube.cluster.transport.api.TransportConfig.DEFAULT_MESSAGE_CODEC;
import static io.scalecube.cluster.transport.api.TransportConfig.DEFAULT_PORT;
import static java.util.stream.Collectors.toList;

import io.scalecube.cluster.transport.api.MessageCodec;
import io.scalecube.net.Address;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
        private TransportType type;

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

    public static class Discovery {

        private Map<String, String> metadata = new HashMap<>();
        private List<String> seedMembers = Collections.emptyList();
        private int port = DEFAULT_PORT;
        private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
        private MessageCodec messageCodec = DEFAULT_MESSAGE_CODEC;
        private int maxFrameLength = DEFAULT_MAX_FRAME_LENGTH;

        private int syncInterval = DEFAULT_SYNC_INTERVAL;
        private int syncTimeout = DEFAULT_SYNC_TIMEOUT;
        private String syncGroup = DEFAULT_SYNC_GROUP;
        private int suspicionMult = DEFAULT_SUSPICION_MULT;
        private int metadataTimeout = DEFAULT_METADATA_TIMEOUT;

        private int pingInterval = DEFAULT_PING_INTERVAL;
        private int pingTimeout = DEFAULT_PING_TIMEOUT;
        private int pingReqMembers = DEFAULT_PING_REQ_MEMBERS;

        private long gossipInterval = DEFAULT_GOSSIP_INTERVAL;
        private int gossipFanout = DEFAULT_GOSSIP_FANOUT;
        private int gossipRepeatMult = DEFAULT_GOSSIP_REPEAT_MULT;

        private String memberHost = DEFAULT_MEMBER_HOST;
        private Integer memberPort = DEFAULT_MEMBER_PORT;

        public Map<String, String> getMetadata() {
            return Collections.unmodifiableMap(metadata);
        }

        public List<Address> getSeedMembers() {
            return seedMembers.stream().map(Address::from).collect(toList());
        }

        public int getPort() {
            return port;
        }

        public int getConnectTimeout() {
            return connectTimeout;
        }

        public MessageCodec getMessageCodec() {
            return messageCodec;
        }

        public int getMaxFrameLength() {
            return maxFrameLength;
        }

        public int getSyncInterval() {
            return syncInterval;
        }

        public int getSyncTimeout() {
            return syncTimeout;
        }

        public String getSyncGroup() {
            return syncGroup;
        }

        public int getSuspicionMult() {
            return suspicionMult;
        }

        public int getMetadataTimeout() {
            return metadataTimeout;
        }

        public int getPingInterval() {
            return pingInterval;
        }

        public int getPingTimeout() {
            return pingTimeout;
        }

        public int getPingReqMembers() {
            return pingReqMembers;
        }

        public long getGossipInterval() {
            return gossipInterval;
        }

        public int getGossipFanout() {
            return gossipFanout;
        }

        public int getGossipRepeatMult() {
            return gossipRepeatMult;
        }

        public String getMemberHost() {
            return memberHost;
        }

        public Integer getMemberPort() {
            return memberPort;
        }

        public void setMetadata(Map<String, String> metadata) {
            this.metadata = metadata;
        }

        public void setSeedMembers(List<String> seedMembers) {
            this.seedMembers = seedMembers;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public void setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public void setMessageCodec(MessageCodec messageCodec) {
            this.messageCodec = messageCodec;
        }

        public void setMaxFrameLength(int maxFrameLength) {
            this.maxFrameLength = maxFrameLength;
        }

        public void setSyncInterval(int syncInterval) {
            this.syncInterval = syncInterval;
        }

        public void setSyncTimeout(int syncTimeout) {
            this.syncTimeout = syncTimeout;
        }

        public void setSyncGroup(String syncGroup) {
            this.syncGroup = syncGroup;
        }

        public void setSuspicionMult(int suspicionMult) {
            this.suspicionMult = suspicionMult;
        }

        public void setMetadataTimeout(int metadataTimeout) {
            this.metadataTimeout = metadataTimeout;
        }

        public void setPingInterval(int pingInterval) {
            this.pingInterval = pingInterval;
        }

        public void setPingTimeout(int pingTimeout) {
            this.pingTimeout = pingTimeout;
        }

        public void setPingReqMembers(int pingReqMembers) {
            this.pingReqMembers = pingReqMembers;
        }

        public void setGossipInterval(long gossipInterval) {
            this.gossipInterval = gossipInterval;
        }

        public void setGossipFanout(int gossipFanout) {
            this.gossipFanout = gossipFanout;
        }

        public void setGossipRepeatMult(int gossipRepeatMult) {
            this.gossipRepeatMult = gossipRepeatMult;
        }

        public void setMemberHost(String memberHost) {
            this.memberHost = memberHost;
        }

        public void setMemberPort(Integer memberPort) {
            this.memberPort = memberPort;
        }
    }
}
