package com.github.hualuomoli.test.gateway.server.config;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSON;
import com.github.hualuomoli.framework.mvc.annotation.ApiVersion;
import com.github.hualuomoli.gateway.server.GatewayServer;
import com.github.hualuomoli.gateway.server.auth.AuthExecution;
import com.github.hualuomoli.gateway.server.auth.RSAAuthExecution;
import com.github.hualuomoli.gateway.server.business.BusinessHandler;
import com.github.hualuomoli.gateway.server.business.SpringBusinessHandler;
import com.github.hualuomoli.gateway.server.parser.JSONParser;
import com.github.hualuomoli.gateway.server.processor.ExceptionProcessor;
import com.github.hualuomoli.test.gateway.server.biz.service.GatewayPartnerLoader;
import com.google.common.collect.Lists;

@Configuration
public class GatewayServerConfig {

	@Autowired
	private GatewayPartnerLoader partnerLoader;
	@Resource(name = "exceptionProcessor")
	private ExceptionProcessor exceptionProcessor;
	@Resource(name = "businessHandler")
	private BusinessHandler businessHandler;
	@Resource(name = "jsonParser")
	private JSONParser jsonParser;

	@Bean
	public GatewayServer initGateway() {

		List<AuthExecution> authExecutions = Lists.newArrayList();
		authExecutions.add(new RSAAuthExecution());

		return new GatewayServer(partnerLoader, exceptionProcessor, businessHandler, jsonParser, authExecutions);
	}

	@Bean(name = "exceptionProcessor")
	public ExceptionProcessor exceptionProcessor() {
		return new ExceptionProcessor() {

			@Override
			public Message process(Throwable t) {
				// TODO
				return new Message("8888", t.getMessage(), t.getMessage());
			}
		};
	}

	@Bean(name = "businessHandler")
	public BusinessHandler businessHandler() {
		return new SpringBusinessHandler(ApiVersion.class, "com.github.hualuomoli");
	}

	@Bean(name = "jsonParser")
	public JSONParser jsonParser() {
		return new JSONParser() {

			@Override
			public String toJsonString(Object obj) {
				return JSON.toJSONString(obj);
			}

			@Override
			public <T> T parseObject(String text, Class<T> clazz) {
				return JSON.parseObject(text, clazz);
			}

			@Override
			public <T> List<T> parseArray(String text, Class<T> clazz) {
				return JSON.parseArray(text, clazz);
			}
		};
	}

}
