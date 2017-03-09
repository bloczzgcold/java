package com.github.hualuomoli.tool;

import org.junit.Assert;
import org.junit.Test;

public class ConfigurationTest {

	@Test
	public void test01Init() {
		Configuration.init(new String[] { "config2.properties", "config3.properties" });
		String name = Configuration.get("project.name");
		String version = Configuration.get("project.version");
		Assert.assertNotNull(name);
		Assert.assertEquals("hualuomoli", name);
		Assert.assertNotNull(version);
		Assert.assertEquals("1.5.0", version);
	}

	@Test
	public void test02InitSort() {
		Configuration.init(new String[] { "config3.properties", "config2.properties" });
		String name = Configuration.get("project.name");
		String version = Configuration.get("project.version");
		Assert.assertNotNull(name);
		Assert.assertEquals("hualuomoli", name);
		Assert.assertNotNull(version);
		Assert.assertEquals("2.0.0", version);
	}

	@Test
	public void test03AddResource() {
		Configuration.init(new String[] { "config2.properties", "config3.properties" });
		String name = Configuration.get("project.name");
		String version = Configuration.get("project.version");
		Assert.assertNotNull(name);
		Assert.assertEquals("hualuomoli", name);
		Assert.assertNotNull(version);
		Assert.assertEquals("1.5.0", version);

		// add
		Configuration.addResource("config4.properties");
		name = Configuration.get("project.name");
		version = Configuration.get("project.version");
		Assert.assertNotNull(name);
		Assert.assertEquals("abcd", name);
		Assert.assertNotNull(version);
		Assert.assertEquals("1.0.0", version);
	}

	@Test
	public void test04InitJar() {
		// add libs/config.zip to build path
		Configuration.init(new String[] { "classpath*:config.properties", "classpath*:config1.properties" });
		String name = Configuration.get("project.name");
		String version = Configuration.get("project.version");
		String type = Configuration.get("project.type");
		Assert.assertNotNull(name);
		Assert.assertEquals("hualuomoli", name);
		Assert.assertNotNull(version);
		Assert.assertEquals("1.0.0", version);
		Assert.assertNotNull(type);
		Assert.assertEquals("war", type);
	}

}
