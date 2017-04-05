package com.github.hualuomoli.demo.gateway.server.config.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * 配置Controller自动注入
 * @author lbq
 *
 */
@Configuration
@ComponentScan(//
		basePackages = { "com.github.hualuomoli" }, //
		useDefaultFilters = false, //
		includeFilters = { //
				@ComponentScan.Filter(type = FilterType.ANNOTATION, value = { Controller.class }), //
				@ComponentScan.Filter(type = FilterType.ANNOTATION, value = { RestController.class }) //
		}//
)
@Import({ ConvertConfig.class, InterceptorConfig.class, MvcAspectConfig.class, ViewConfig.class, FileuploadConfig.class, ExceptionConfig.class })
public class MvcComponentConfig {

	private static final Logger logger = LoggerFactory.getLogger(MvcComponentConfig.class);

	static {
		logger.info("初始化spring mvc component");
	}

}
