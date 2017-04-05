package com.github.hualuomoli.demo.framework.biz.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.hualuomoli.demo.framework.biz.entity.User;
import com.github.hualuomoli.demo.framework.biz.service.UserService;

@Controller(value = "com.github.hualuomoli.demo.framework.biz.controller.UserController")
@RequestMapping(value = "/test/user")
public class UserController {

	@Autowired
	private UserService userService;

	// http://localhost/test/user/json?username=hualuomoil&nickname=花落寞离
	@RequestMapping(value = "/json")
	@ResponseBody
	public String json(User user, HttpServletRequest req) {
		return JSON.toJSONString(user);
	}

	// http://localhost/test/user/view
	@RequestMapping(value = "/view")
	public String view() {
		return "show";
	}

	// http://localhost/test/user/find?username=hualuomoil
	@RequestMapping(value = "/find")
	@ResponseBody
	public String find(User user, HttpServletRequest req) {
		User u = new User();
		u.setUsername(user.getUsername());
		List<User> list = userService.findList(u);

		if (list != null && list.size() > 0) {
			u = list.get(0);
		}

		return JSON.toJSONString(u);
	}

}
