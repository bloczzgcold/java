package com.github.hualuomoli.demo.framework.config.mvc;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

/**
 * 配置异常处理
 * @see #HandlerExceptionResolver
 * @author lbq
 *
 */
@Configuration
@ControllerAdvice
public class ExceptionConfig {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionConfig.class);

	static {
		logger.info("初始化mvc 错误处理");
	}

	// 请求方法不允许
	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public void methodNotAllowed(HttpServletResponse response, HttpRequestMethodNotSupportedException e) {
		this.flushJson(response, HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage());
	}

	// 请求协议不允许
	@ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public void unsupportedMediaType(HttpServletResponse response, HttpMediaTypeNotSupportedException e) {
		this.flushJson(response, HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage());
	}

	// 运行时异常
	@ExceptionHandler(value = RuntimeException.class)
	@ResponseStatus(HttpStatus.OK)
	public void runtimeException(HttpServletResponse response, RuntimeException e) {

		if (logger.isWarnEnabled()) {
			logger.warn("{}", e);
			this.flushJson(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		} else {
			logger.error("{}", e);
			this.flushJson(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统错误");
		}

	}

	// 异常
	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void exception(HttpServletResponse response, Exception e) {
		this.flushHtml(response, e);
	}

	// 输出html
	private void flushHtml(HttpServletResponse response, Exception e) {

		logger.error("{}", e);

		// 设置响应头
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");

		try {
			e.printStackTrace(response.getWriter());
		} catch (IOException e1) {
		}

	}

	// 输出JSON
	private void flushJson(HttpServletResponse response, Integer code, String message) {

		// 设置响应头
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");

		// 设置数据
		Map<String, Object> map = Maps.newHashMap();
		map.put("code", code);
		map.put("msg", message);
		String data = JSON.toJSONString(map);
		// 输出
		try {
			response.getOutputStream().write(data.getBytes());
		} catch (IOException e1) {
		}
	}

}
