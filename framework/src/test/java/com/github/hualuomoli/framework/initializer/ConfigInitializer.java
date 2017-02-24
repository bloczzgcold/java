package com.github.hualuomoli.framework.initializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;

import com.github.hualuomoli.tool.PropertiesLoader;

/**
 * 初始化配置文件
 * @author lbq
 *
 */
@Order(1)
public class ConfigInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// 运行环境
		String environment = System.getenv("environment");
		initLogger(environment, "log4j.properties");
	}

	private static void initLogger(String environment, String filename) {
		PropertyConfigurator.configure(PropertiesLoader.load(filename, environment));
	}

}
