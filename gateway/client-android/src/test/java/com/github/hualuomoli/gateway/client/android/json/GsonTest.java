package com.github.hualuomoli.gateway.client.android.json;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GsonTest {

	private static final Logger logger = LoggerFactory.getLogger(GsonTest.class);

	private static final Gson gson = new Gson();

	@Test
	public void testToJsonString() {
		User user = new User();
		user.setUsername("hualuomoli");
		user.setNickname("花落寞离");
		logger.debug("user={}", gson.toJsonString(user));
	}

	@Test
	public void testListToJsonString() {
		User user1 = new User();
		user1.setUsername("hualuomoli");
		user1.setNickname("花落寞离");

		User user2 = new User();
		user2.setUsername("tester");
		user2.setNickname("测试");

		List<User> userList = new ArrayList<User>();
		userList.add(user1);
		userList.add(user2);

		logger.debug("userList={}", gson.toJsonString(userList));

	}

	@Test
	public void testParseObject() {
		String text = "{\"username\":\"hualuomoli\",\"nickname\":\"花落寞离\"}";
		User user = gson.parseObject(text, User.class);
		Assert.assertEquals("hualuomoli", user.getUsername());
		Assert.assertEquals("花落寞离", user.getNickname());
	}

	@Test
	public void testParseArray() {
		String text = "[{\"username\":\"hualuomoli\",\"nickname\":\"花落寞离\"},{\"username\":\"tester\",\"nickname\":\"测试\"}]";
		List<User> userList = gson.parseArray(text, User.class);
		Assert.assertEquals("hualuomoli", userList.get(0).getUsername());
		Assert.assertEquals("花落寞离", userList.get(0).getNickname());

		Assert.assertEquals("tester", userList.get(1).getUsername());
		Assert.assertEquals("测试", userList.get(1).getNickname());

	}

	public static final class User {
		private String username;
		private String nickname;

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
	}

}
