package com.github.hualuomoli.demo.gateway.server.initializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.github.hualuomoli.framework.base.config.Log4jInitializer;
import com.github.hualuomoli.demo.gateway.server.config.BaseConfig;
import com.github.hualuomoli.demo.gateway.server.config.GatewayServerConfig;
import com.github.hualuomoli.demo.gateway.server.config.MvcConfig;

/**
 * 初始化
 * @author lbq
 *
 */
@Order(1)
public class BaseInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		// 设置log4j
		Log4jInitializer.init();

		AnnotationConfigWebApplicationContext rootContext = _getRootContext();

		// 设置Spring监听器
		servletContext.addListener(new ContextLoaderListener(rootContext));
		// RequestContextListener

		// 设置转发servlet
		// rootContext可以不使用上面定义的,可以重新获取一个
		// rootContext = _getRootContext();
		servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext)).addMapping("/");

	}

	// 获取Spring实例
	private AnnotationConfigWebApplicationContext _getRootContext() {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(BaseConfig.class, MvcConfig.class, GatewayServerConfig.class);
		return rootContext;
	}

}
