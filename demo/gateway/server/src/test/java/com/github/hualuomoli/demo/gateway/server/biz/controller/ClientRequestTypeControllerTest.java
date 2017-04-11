package com.github.hualuomoli.demo.gateway.server.biz.controller;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.alibaba.fastjson.JSON;
import com.github.hualuomoli.demo.gateway.server.controller.ClientControllerTest;
import com.google.common.collect.Lists;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientRequestTypeControllerTest extends ClientControllerTest {

	@Test
	public void testObject() throws Exception {
		RequestTypeController.User user = new RequestTypeController.User();
		user.setUsername("hualuomoli");

		String result = client.call("test.type.object", JSON.toJSONString(user));
		logger.debug("result={}", result);
	}

	@Test
	public void testList() throws Exception {
		RequestTypeController.User user1 = new RequestTypeController.User();
		user1.setUsername("hualuomoli");

		RequestTypeController.User user2 = new RequestTypeController.User();
		user2.setUsername("tester");

		List<RequestTypeController.User> users = Lists.newArrayList(user1, user2);

		String result = client.call("test.type.list", JSON.toJSONString(users));
		logger.debug("result={}", result);
	}

}
