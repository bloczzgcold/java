package com.github.hualuomoli.demo.dubbo.provider.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.hualuomoli.demo.dubbo.api.service.MyDubboService;

@Service(value = "com.github.hualuomoli.demo.dubbo.provider.service.MyService")
// 如果实现多个接口,需要指定暴露的接口
@com.alibaba.dubbo.config.annotation.Service(interfaceClass = MyDubboService.class)
public class MyService implements MyBusinessService, MyDubboService {

	private static final Logger logger = LoggerFactory.getLogger(MyService.class);

	@Override
	public String say(String message) {

		logger.info("[say]");

		return "say " + message;
	}

	@Override
	public String call(String name) {
		return name;
	}

}
