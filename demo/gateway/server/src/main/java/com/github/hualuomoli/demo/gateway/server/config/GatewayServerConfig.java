package com.github.hualuomoli.demo.gateway.server.config;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSON;
import com.github.hualuomoli.demo.gateway.server.biz.service.GatewayPartnerLoader;
import com.github.hualuomoli.framework.mvc.annotation.ApiVersion;
import com.github.hualuomoli.gateway.server.GatewayServer;
import com.github.hualuomoli.gateway.server.auth.AuthExecution;
import com.github.hualuomoli.gateway.server.auth.RSAAuthExecution;
import com.github.hualuomoli.gateway.server.business.BusinessHandler;
import com.github.hualuomoli.gateway.server.business.SpringBusinessHandler;
import com.github.hualuomoli.gateway.server.parser.JSONParser;
import com.github.hualuomoli.gateway.server.processor.ExceptionProcessor;
import com.google.common.collect.Lists;

@Configuration
public class GatewayServerConfig {

	private static final Logger logger = LoggerFactory.getLogger(GatewayServerConfig.class);

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
		String privateKeyBase64 = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ9hCYj2rCi3sywghwnX1nF4N1qNQHKPEo2fnORPx1Oyuo4zUz89jlO36Ww3HNZAWmUSU3d17YsowYbpDc1D2ocZa6xMtU5bVylp8dxq0PAhZXwxnQT5KAIyC8gltTdlQ4bcpkSBWQD2qIv3egwwpEqgA+jgtLiBIpBWIeQA8qgHAgMBAAECgYA//svAViPcmZ51Cn6Ogj8WAspfLraBCVfOzBdISYxk3DAyZwN3MjVTAlbU3/BRSzfAu7IOFqkSJoRGso+bKzK0SCc1RiQD8n/Vw0f29zsgzPSrwUKIri6eO/BKmqS8R6vKn1VowYcYoLPb74SNewk+dqmoAog/CHZCLkEJvYZy6QJBANPpLRQTUaKltzbdNCeDt4vLW4dHWYOVwBku4X0ktVYoEYVCOa2tt2WVw6hpquYdt9NRYjFviV3FaWv70Jaqd30CQQDAieff8mQC7o6B+tOF0CTiPybEA7geWDSWveu8vNcycw8MEvTDgIosmsSeP14iJ+wJwryT2nqOAKis4zadypzTAkEAl8c/Pk3H/tLqsyUkodi5sirpV69G8fRkLqVhZBzStO7l/ag9X6Q4402tYgatHTzT2UtFJVtZ7AvlQi6ObBuUkQJBAK2l8vXcY+ztAKQj90/RWOKgeDMC87SScuOdaJYxbpi2gtSt6AjGzlfKQhhDKH//p3dqJa/ntO6Lk5VR2zlWujcCQB2K8BFD3SSH2wnYWELbn35E+wR4oskG4cW/5mliov1lLTtGVlJabULZ5vK2owbfgyJ6SO1stBYXcQwMaghQCug=";
		authExecutions.add(new RSAAuthExecution(privateKeyBase64));

		List<AuthExecution.Filter> filters = Lists.newArrayList();
		filters.add(new AuthExecution.Filter() {

			@Override
			public void preHandler(String partnerId, String apiMethod) throws Exception {
				logger.debug("partnerId={},apiMethod={}", partnerId, apiMethod);
			};
		});

		return new GatewayServer(partnerLoader, exceptionProcessor, businessHandler, jsonParser, authExecutions, filters);
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
