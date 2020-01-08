package org.springframework.boot.scalecube;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.scalecube.beans.EnableScalecube;
import org.springframework.boot.scalecube.codec.JacksonCodecAutoConfiguration;
import org.springframework.boot.scalecube.codec.MessageCodecConfiguration;
import org.springframework.boot.scalecube.discovery.ScalecubeDiscoveryConfiguration;
import org.springframework.boot.scalecube.properties.ScalecubePropertiesConfiguration;
import org.springframework.boot.scalecube.transport.RSocketTransportAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@Import({
    ScalecubePropertiesConfiguration.class,
    JacksonCodecAutoConfiguration.class,
    MessageCodecConfiguration.class,
    ScalecubeDiscoveryConfiguration.class,
    RSocketTransportAutoConfiguration.class
})
@EnableScalecube
public class ScalecubeAutoConfiguration {

}
