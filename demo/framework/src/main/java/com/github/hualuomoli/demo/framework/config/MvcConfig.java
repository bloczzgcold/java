package com.github.hualuomoli.demo.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.hualuomoli.demo.framework.config.mvc.ConvertConfig;
import com.github.hualuomoli.demo.framework.config.mvc.ExceptionConfig;
import com.github.hualuomoli.demo.framework.config.mvc.FileuploadConfig;
import com.github.hualuomoli.demo.framework.config.mvc.InterceptorConfig;
import com.github.hualuomoli.demo.framework.config.mvc.MvcAspectConfig;
import com.github.hualuomoli.demo.framework.config.mvc.MvcComponentConfig;
import com.github.hualuomoli.demo.framework.config.mvc.StaticResourceConfig;
import com.github.hualuomoli.demo.framework.config.mvc.ViewConfig;

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
