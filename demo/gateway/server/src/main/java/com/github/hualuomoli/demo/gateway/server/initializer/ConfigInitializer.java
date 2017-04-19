package com.github.hualuomoli.demo.gateway.server.initializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;

import com.github.hualuomoli.framework.base.config.Log4jInitializer;

/**
 * 初始化
 * @author lbq
 *
 */
@Order(1)
public class ConfigInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		// 设置log4j
		Log4jInitializer.init();
	}

}
