package com.github.hualuomoli.test.framework.config.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 配置MVC切面
 * @author lbq
 *
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MvcAspectConfig {

	private static final Logger logger = LoggerFactory.getLogger(MvcAspectConfig.class);

	static {
		logger.info("初始化mvc aspect");
	}

}
