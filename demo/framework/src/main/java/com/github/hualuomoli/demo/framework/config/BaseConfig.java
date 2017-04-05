package com.github.hualuomoli.demo.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.hualuomoli.demo.framework.config.base.BaseAspectConfig;
import com.github.hualuomoli.demo.framework.config.base.BaseComponentConfig;
import com.github.hualuomoli.demo.framework.config.base.DataSourceConfig;
import com.github.hualuomoli.demo.framework.config.base.MybatisConfig;
import com.github.hualuomoli.demo.framework.config.base.MybatisScannerConfig;
import com.github.hualuomoli.demo.framework.config.base.TransactionConfig;

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
