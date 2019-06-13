package io.github.eutkin.scalecube;

import io.github.eutkin.scalecube.properties.ScalecubeProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ScalecubeAutoConfiguration.class)
public class ScalecubeAutoConfigurationTests {

	@Autowired
	private ScalecubeProperties properties;

	@Test
	public void contextLoads() {
		assertNotNull(properties);
		assertNotNull(properties.getTransport());
		assertNotNull(properties.getDiscovery());
	}

}
