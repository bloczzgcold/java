package com.github.hualuomoli.demo.gateway.server.config.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 切面
 * @author lbq
 *
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class BaseAspectConfig {

	private static final Logger logger = LoggerFactory.getLogger(BaseAspectConfig.class);

	static {
		logger.info("初始化spring aspect");
	}

}
