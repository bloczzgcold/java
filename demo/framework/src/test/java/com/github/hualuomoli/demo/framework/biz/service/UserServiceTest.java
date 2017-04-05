package com.github.hualuomoli.demo.framework.biz.service;

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
import com.github.hualuomoli.framework.constants.Status;
import com.github.hualuomoli.framework.thread.Current;
import com.github.hualuomoli.demo.framework.biz.entity.User;
import com.github.hualuomoli.demo.framework.service.ServiceTest;
import com.google.common.collect.Lists;

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
	public void test01Clear() {
		// clear
		this.clear();
	}

	@Test
	public void test02Insert() {
		User user = new User();
		user.setId(id);
		user.setUsername("hualuomoli");
		user.setNickname("花落寞离");
		int updated = userService.insert(user);
		Assert.assertEquals(1, updated);

		// get
		user = userService.get(id);
		Assert.assertNotNull(user);
		Assert.assertEquals("hualuomoli", user.getUsername());
		Assert.assertEquals("花落寞离", user.getNickname());
	}

	@Test
	public void test03Update() {
		User user = new User();
		user.setId(id);
		user.setNickname("测试");
		user.setAge(20);
		int updated = userService.update(user);
		Assert.assertEquals(1, updated);

		// get
		user = userService.get(id);
		Assert.assertNotNull(user);
		Assert.assertEquals("测试", user.getNickname());
		Assert.assertEquals(20, user.getAge().intValue());
	}

	@Test
	public void test04Get() {
		User user = userService.get(id);
		Assert.assertNotNull(user);
		Assert.assertEquals("hualuomoli", user.getUsername());
	}

	@Test
	public void test05LogicDelete() {
		int updated = userService.logicDelete(id);
		Assert.assertEquals(1, updated);

		// get
		User user = userService.get(id);
		Assert.assertNotNull(user);
		Assert.assertEquals(Status.DELETED.value(), user.getStatus());
	}

	@Test
	public void test06Delete() {
		int updated = userService.delete(id);
		Assert.assertEquals(1, updated);

		// get
		User user = userService.get(id);
		Assert.assertNull(user);
	}

	@Test
	public void test07BatchInsertList() {
		int size = 20;
		List<User> userList = Lists.newArrayList();
		// add
		for (int i = 0; i < size; i++) {
			User u = new User();
			u.setUsername("tester");
			u.setNickname("测试" + (i % 3));
			u.setAge(10 + (i % 8));
			userList.add(u);
		}
		int updated = userService.batchInsert(userList);
		Assert.assertEquals(size, updated);

		// get
		userList = userService.findList(new User());
		Assert.assertEquals(size, userList.size());
	}

	@Test
	public void test08BatchDeleteList() {
		List<User> userList = userService.findList(new User());
		List<String> ids = Lists.newArrayList();
		for (User user : userList) {
			ids.add(user.getId());
		}
		int updated = userService.batchDelete(ids);
		Assert.assertEquals(userList.size(), updated);

		// get
		userList = userService.findList(new User());
		Assert.assertEquals(0, userList.size());

	}

	@Test
	public void test09BatchInsertArray() {
		int size = 20;
		User[] users = new User[size];
		// add
		for (int i = 0; i < 20; i++) {
			User u = new User();
			u.setUsername("tester");
			u.setNickname("测试" + (i % 3));
			u.setAge(10 + (i % 8));
			users[i] = u;
		}
		int updated = userService.batchInsert(users);
		Assert.assertEquals(size, updated);

		// get
		List<User> userList = userService.findList(new User());
		Assert.assertEquals(size, userList.size());
	}

	@Test
	public void test10BatchDeleteArray() {
		List<User> userList = userService.findList(new User());
		String[] ids = new String[userList.size()];
		for (int i = 0; i < userList.size(); i++) {
			ids[i] = userList.get(i).getId();
		}
		int updated = userService.batchDelete(ids);
		Assert.assertEquals(userList.size(), updated);

		// get
		userList = userService.findList(new User());
		Assert.assertEquals(0, userList.size());

	}

	@Test
	public void test11FindList() {

		// add
		int size = 20;
		List<User> userList = Lists.newArrayList();
		// add
		for (int i = 0; i < size; i++) {
			User u = new User();
			u.setUsername("tester");
			u.setNickname("测试" + (i % 3));
			u.setAge(10 + (i % 8));
			userList.add(u);
		}
		userService.batchInsert(userList);

		// find by username
		User user = new User();
		user.setUsername("tester");
		userList = userService.findList(user);
		Assert.assertEquals(20, userList.size());

		// find by nickname
		user = new User();
		user.setNickname("测试0");
		userList = userService.findList(user);
		Assert.assertEquals(7, userList.size());

		// find by nickname
		user = new User();
		user.setNickname("测试2");
		userList = userService.findList(user);
		Assert.assertEquals(6, userList.size());

		// find by nickname
		user = new User();
		user.setNickname("测试2");
		user.setAge(12);
		userList = userService.findList(user);
		Assert.assertEquals(1, userList.size());

		// clear
		this.clear();

	}

	@Test
	public void test12FindPage() {

		// add
		int size = 20;
		List<User> userList = Lists.newArrayList();
		// add
		for (int i = 0; i < size; i++) {
			User u = new User();
			u.setUsername("tester");
			u.setNickname("测试" + (i % 3));
			u.setAge(10 + (i % 8));
			userList.add(u);
		}
		userService.batchInsert(userList);

		// find by page
		User user = new User();
		user.setNickname("测试0");
		Page page = userService.findPage(user, 2, 3, "id");
		Assert.assertEquals(7, page.getCount().intValue());
		Assert.assertEquals(3, page.getDataList().size());

		// clear
		this.clear();
	}

	private void clear() {
		List<User> userList = userService.findList(new User());
		List<String> ids = Lists.newArrayList();
		for (User u : userList) {
			ids.add(u.getId());
		}
		userService.batchDelete(ids);
	}

}
