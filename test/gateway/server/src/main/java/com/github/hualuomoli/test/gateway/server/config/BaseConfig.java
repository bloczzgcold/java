package com.github.hualuomoli.test.gateway.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.hualuomoli.test.gateway.server.config.base.BaseAspectConfig;
import com.github.hualuomoli.test.gateway.server.config.base.BaseComponentConfig;
import com.github.hualuomoli.test.gateway.server.config.base.DataSourceConfig;
import com.github.hualuomoli.test.gateway.server.config.base.MybatisConfig;
import com.github.hualuomoli.test.gateway.server.config.base.MybatisScannerConfig;
import com.github.hualuomoli.test.gateway.server.config.base.TransactionConfig;

/**
 * 切面
 * @author lbq
 *
 */
@Configuration
@Import(value = { DataSourceConfig.class //
		, MybatisScannerConfig.class //
		, MybatisConfig.class //
		, BaseComponentConfig.class //
		, TransactionConfig.class //
		, BaseAspectConfig.class })
public class BaseConfig {

}
