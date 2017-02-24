package com.github.hualuomoli.framework.test;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import com.github.hualuomoli.framework.config.BaseAspectConfig;
import com.github.hualuomoli.framework.config.ComponentConfig;
import com.github.hualuomoli.framework.config.DataSourceConfig;
import com.github.hualuomoli.framework.config.MybatisConfig;
import com.github.hualuomoli.framework.config.MybatisScannerConfig;
import com.github.hualuomoli.framework.config.TransactionConfig;

@WebAppConfiguration
@ContextConfiguration(classes = { DataSourceConfig.class //
		, MybatisScannerConfig.class //
		, MybatisConfig.class //
		, ComponentConfig.class //
		, TransactionConfig.class //
		, BaseAspectConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceTest {

	protected static final Logger logger = LoggerFactory.getLogger(ServiceTest.class);

	@Autowired
	protected WebApplicationContext wac;

}
