package com.github.hualuomoli.test.framework.biz.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.hualuomoli.framework.base.entity.Page;
import com.github.hualuomoli.framework.constants.DataStatus;
import com.github.hualuomoli.framework.thread.Current;
import com.github.hualuomoli.test.framework.biz.entity.User;
import com.github.hualuomoli.test.framework.service.ServiceTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest extends ServiceTest {

	@Autowired
	private UserService userService;
	private static String id = UUID.randomUUID().toString().replaceAll("[-]", "");

	@Before
	public void before() {
		Current.setUsername("hualuomoli");
		Current.setDate(new Date());
	}

	@Test
	public void test00Clear() {
		List<User> userList = userService.findList(new User());
		for (User user : userList) {
			userService.delete(user.getId());
		}
	}

	@Test
	public void test01Insert() {
		User user = new User();
		user.setId(id);
		user.setUsername("hualuomoli");
		user.setNickname("花落寞离");
		int updated = userService.insert(user);
		Assert.assertEquals(1, updated);
	}

	@Test
	public void test02Update() {
		User user = new User();
		user.setId(id);
		user.setNickname("测试");
		user.setAge(20);
		int updated = userService.update(user);
		Assert.assertEquals(1, updated);
	}

	@Test
	public void test03Get() {
		User user = userService.get(id);
		Assert.assertNotNull(user);
		Assert.assertEquals("hualuomoli", user.getUsername());
		Assert.assertEquals("测试", user.getNickname());
	}

	@Test
	public void test04LogicDelete() {
		int updated = userService.logicDelete(id);
		Assert.assertEquals(1, updated);
		User user = userService.get(id);
		Assert.assertNotNull(user);
		Assert.assertEquals(DataStatus.DELETED.value(), user.getDataStatus());
	}

	@Test
	public void test05Delete() {
		int updated = userService.delete(id);
		Assert.assertEquals(1, updated);
		User user = userService.get(id);
		Assert.assertNull(user);
	}

	@Test
	public void test06FindList() {

		// add
		for (int i = 0; i < 20; i++) {
			User u = new User();
			u.setUsername("tester");
			u.setNickname("测试" + (i % 3));
			u.setAge(10 + (i % 8));
			userService.insert(u);
		}

		User user = new User();
		user.setUsername("tester");
		List<User> userList = userService.findList(user);
		Assert.assertEquals(20, userList.size());

		user = new User();
		user.setNickname("测试0");
		userList = userService.findList(user);
		Assert.assertEquals(7, userList.size());

		user = new User();
		user.setNickname("测试2");
		userList = userService.findList(user);
		Assert.assertEquals(6, userList.size());

		user = new User();
		user.setNickname("测试2");
		user.setAge(12);
		userList = userService.findList(user);
		Assert.assertEquals(1, userList.size());

		this.test00Clear();
	}

	@Test
	public void test07FindPage() {

		// add
		for (int i = 0; i < 20; i++) {
			User u = new User();
			u.setUsername("tester");
			u.setNickname("测试" + (i % 3));
			userService.insert(u);
		}

		User user = new User();
		user.setNickname("测试0");
		Page page = userService.findPage(user, 2, 3, "id");
		Assert.assertEquals(7, page.getCount().intValue());
		Assert.assertEquals(3, page.getDataList().size());

		this.test00Clear();
	}

}
