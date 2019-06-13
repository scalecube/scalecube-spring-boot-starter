package io.github.eutkin.scalecube;

import io.scalecube.services.Microservices;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface ScalecubeTransportCustomizer extends UnaryOperator<Microservices.ServiceTransportBootstrap> {

   default ScalecubeTransportCustomizer andThen(ScalecubeTransportCustomizer customizer) {
        return t -> customizer.apply(this.apply(t));
    }

}
