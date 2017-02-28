package com.github.hualuomoli.test.gateway.server.biz.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.hualuomoli.gateway.server.GatewayServer;

@Controller(value = "com.github.hualuomoli.test.gateway.server.biz.controller.GatewayController")
@RequestMapping(value = "/test/gateway")
public class GatewayController {

	private static final Logger logger = LoggerFactory.getLogger(GatewayController.class);

	@Autowired
	private GatewayServer gatewayServer;

	@RequestMapping(value = "")
	public void transfer(HttpServletRequest req, HttpServletResponse res) {
		String start = new SimpleDateFormat("HH:mm:ss S").format(new Date());
		gatewayServer.invoke(req, res);
		String end = new SimpleDateFormat("HH:mm:ss S").format(new Date());

		logger.debug("{} ~ {} - {}", start, end, gatewayServer);
	}

}