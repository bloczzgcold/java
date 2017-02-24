package com.github.hualuomoli.framework.initializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.github.hualuomoli.framework.config.BaseAspectConfig;
import com.github.hualuomoli.framework.config.ComponentConfig;
import com.github.hualuomoli.framework.config.DataSourceConfig;
import com.github.hualuomoli.framework.config.MybatisConfig;
import com.github.hualuomoli.framework.config.MybatisScannerConfig;
import com.github.hualuomoli.framework.config.TransactionConfig;

/**
 * 初始化
 * @author hualuomoli
 *
 */
@Order(2)
public class BaseInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

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
		rootContext.register(DataSourceConfig.class //
				, MybatisScannerConfig.class //
				, MybatisConfig.class //
				, ComponentConfig.class //
				, TransactionConfig.class //
				, BaseAspectConfig.class);
		return rootContext;
	}

}
