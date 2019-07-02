package org.springframework.boot.scalecube.discovery;

import io.scalecube.services.ServiceEndpoint;
import io.scalecube.services.discovery.api.ServiceDiscovery;

import java.util.function.Function;

@FunctionalInterface
public interface DiscoveryInitializer extends Function<ServiceEndpoint, ServiceDiscovery> {
}
