package com.github.hualuomoli.demo.gateway.server.biz.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "/test/type")
@Controller(value = "com.github.hualuomoli.demo.gateway.server.biz.controller.RequestTypeController")
public class RequestTypeController {

	private static final Logger logger = LoggerFactory.getLogger(RequestTypeController.class);

	@RequestMapping(value = "object")
	@ResponseBody
	public String object(User user) {

		logger.debug("username={}", user.getUsername());

		return "success";
	}

	@RequestMapping(value = "list", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	@ResponseBody
	public String list(@RequestBody List<User> users) {

		for (User user : users) {
			logger.debug("username={}", user.getUsername());
		}

		return "success";
	}

	public static final class User {
		private String username;
		private String nickname;
		private Integer age;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}
	}

}
