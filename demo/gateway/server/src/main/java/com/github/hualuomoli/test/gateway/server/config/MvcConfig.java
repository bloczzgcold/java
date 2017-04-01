package com.github.hualuomoli.test.gateway.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.hualuomoli.test.gateway.server.config.mvc.ConvertConfig;
import com.github.hualuomoli.test.gateway.server.config.mvc.ExceptionConfig;
import com.github.hualuomoli.test.gateway.server.config.mvc.FileuploadConfig;
import com.github.hualuomoli.test.gateway.server.config.mvc.InterceptorConfig;
import com.github.hualuomoli.test.gateway.server.config.mvc.MvcAspectConfig;
import com.github.hualuomoli.test.gateway.server.config.mvc.MvcComponentConfig;
import com.github.hualuomoli.test.gateway.server.config.mvc.StaticResourceConfig;
import com.github.hualuomoli.test.gateway.server.config.mvc.ViewConfig;

/**
 * 切面
 * @author lbq
 *
 */
@Configuration
@Import(value = { ConvertConfig.class //
		, ExceptionConfig.class //
		, FileuploadConfig.class //
		, InterceptorConfig.class //
		, MvcAspectConfig.class //
		, MvcComponentConfig.class//
		, StaticResourceConfig.class //
		, ViewConfig.class })
public class MvcConfig {

}
