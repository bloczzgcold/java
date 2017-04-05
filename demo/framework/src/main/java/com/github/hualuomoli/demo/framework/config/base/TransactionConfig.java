package com.github.hualuomoli.demo.framework.config.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启用注解事务管理，使用CGLib代理
 * @author lbq
 *
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class TransactionConfig {

	private static final Logger logger = LoggerFactory.getLogger(TransactionConfig.class);

	static {
		logger.info("初始化spring transaction");
	}

}
