package com.github.hualuomoli.demo.gateway.server.config.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * 配置视图
 * @author lbq
 *
 */
@Configuration
public class ViewConfig {

	private static final Logger logger = LoggerFactory.getLogger(ViewConfig.class);

	// 视图
	@Bean
	public ViewResolver viewResolver() {
		logger.info("初始化视图处理器");

		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

}
