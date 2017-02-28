package com.github.hualuomoli.plugin.mq;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import com.github.hualuomoli.plugin.mq.config.BaseConfig;

@WebAppConfiguration
@ContextConfiguration(classes = { BaseConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceTest {

	protected static final Logger logger = LoggerFactory.getLogger(ServiceTest.class);

	@Autowired
	protected WebApplicationContext wac;

}
