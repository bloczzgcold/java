package com.github.hualuomoli.framework.util;

import java.io.IOException;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

public class ScanUtilsTest {

	private static final Logger logger = LoggerFactory.getLogger(ScanUtils.class);

	@Test
	public void testFindPackageAnnotationClass() throws ClassNotFoundException, IOException {
		Set<Class<?>> clazzSet = ScanUtils.findByAnnotation("com.wizhong.ln", Configuration.class);
		for (Class<?> clazz : clazzSet) {
			logger.debug("class={}", clazz.getName());
		}
	}

}
