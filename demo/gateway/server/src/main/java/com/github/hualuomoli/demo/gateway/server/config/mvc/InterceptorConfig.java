package com.github.hualuomoli.demo.gateway.server.config.mvc;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 配置HTTP拦截器
 * @author lbq
 *
 */
@Configuration
@EnableWebMvc
public class InterceptorConfig extends WebMvcConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(InterceptorConfig.class);

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		logger.info("初始化mvc 拦截器");

		registry.addInterceptor(logInterceptor());
	}

	@Bean
	public HandlerInterceptor logInterceptor() {

		logger.info("初始化mvc 日志拦截器");

		return new LogInterceptor();
	}

	// 日志拦截器
	private static class LogInterceptor extends HandlerInterceptorAdapter {

		private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

		private static final String TAB = "  ";

		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
			if (logger.isDebugEnabled()) {
				this.showRequestInformation(request);
			}
			return true;
		}

		@Override
		public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		}

		/** 输出请求信息 */
		private void showRequestInformation(HttpServletRequest req) {
			// show request
			logger.debug("request message");
			logger.debug(TAB + "characterEncoding {}", req.getCharacterEncoding());
			logger.debug(TAB + "contextPath {}", req.getContextPath());
			logger.debug(TAB + "servletPath {}", req.getServletPath());
			logger.debug(TAB + "requestedSessionId {}", req.getRequestedSessionId());
			logger.debug(TAB + "url {}", req.getRequestURL());
			logger.debug(TAB + "uri {}", req.getRequestURI());
			logger.debug(TAB + "method {}", req.getMethod());
			logger.debug(TAB + "headers");
			Enumeration<String> headerNames = req.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				logger.debug(TAB + TAB + "{} = {}", name, req.getHeader(name));
			}
			logger.debug(TAB + "parameters");
			Enumeration<String> parameterNames = req.getParameterNames();
			while (parameterNames.hasMoreElements()) {
				String name = parameterNames.nextElement();
				String[] values = req.getParameterValues(name);
				logger.debug(TAB + TAB + "{} = {}", name, values);
			}
		}

	}

}
