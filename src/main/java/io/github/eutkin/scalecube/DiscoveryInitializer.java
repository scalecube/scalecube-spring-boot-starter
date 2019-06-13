package io.github.eutkin.scalecube;

import io.scalecube.services.ServiceEndpoint;
import io.scalecube.services.discovery.api.ServiceDiscovery;

import java.util.function.Function;

@FunctionalInterface
interface DiscoveryInitializer extends Function<ServiceEndpoint, ServiceDiscovery> {
}