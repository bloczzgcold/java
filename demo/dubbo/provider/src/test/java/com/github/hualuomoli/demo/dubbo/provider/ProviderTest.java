package com.github.hualuomoli.demo.dubbo.provider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.dubbo.container.Main;
import com.github.hualuomoli.demo.dubbo.provider.config.BaseComponentConfig;
import com.github.hualuomoli.demo.dubbo.provider.config.ProviderConfig;

@WebAppConfiguration
@ContextConfiguration(classes = { BaseComponentConfig.class, ProviderConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class ProviderTest {

	protected static final Logger logger = LoggerFactory.getLogger(ProviderTest.class);

	@Autowired
	protected WebApplicationContext wac;

	@Test
	public void test() {

		logger.info("dubbo provider staretd.");

		Main.main(new String[] {});

	}

}
