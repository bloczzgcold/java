package com.github.hualuomoli.test.gateway.server.biz.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.hualuomoli.framework.mvc.annotation.ApiVersion;
import com.github.hualuomoli.test.gateway.server.biz.entity.User;
import com.github.hualuomoli.test.gateway.server.biz.service.UserService;

@Controller(value = "com.github.hualuomoli.test.gateway.server.biz.controller.UserController")
@RequestMapping(value = "/test/user")
@ApiVersion(value = "0.0.1")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/find")
	@ResponseBody
	public String find_old(User user, HttpServletRequest req) {
		userService.findList(user);

		User u = new User();
		u.setUsername(user.getUsername());
		u.setRemark("测试描述信息");
		return JSON.toJSONString(u);
	}

	@RequestMapping(value = "/find")
	@ResponseBody
	@ApiVersion(value = "1.0.0")
	public String find(User user, HttpServletRequest req) {
		userService.findList(user);

		User u = new User();
		u.setUsername(user.getUsername());
		u.setNickname("花落寞离");
		return JSON.toJSONString(u);
	}

	@RequestMapping(value = "/find")
	@ResponseBody
	@ApiVersion(value = "1.0.1")
	public String find_new(User user, HttpServletRequest req) {
		userService.findList(user);

		User u = new User();
		u.setUsername(user.getUsername());
		u.setAge(20);
		return JSON.toJSONString(u);
	}

}
