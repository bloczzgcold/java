package com.github.hualuomoli.demo.gateway.server.biz.controller;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.github.hualuomoli.demo.gateway.server.biz.entity.User;
import com.github.hualuomoli.demo.gateway.server.controller.MockControllerTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MockUserApiVersionControllerTest extends MockControllerTest {

	private String apiVersion = "1.0.0";

	// 低于最低版本
	@Test
	public void test01LessFirst() throws Exception {
		apiVersion = "0.0.0.1";
		this.runner()//
				.andExpect(new ResultMatcher() {

					@Override
					public void match(MvcResult result) throws Exception {
						MockMvcResultMatchers.status().isNotFound();
					}
				});
	}

	// 等于第一个版本
	@Test
	public void test02EqualFirst() throws Exception {
		apiVersion = "0.0.1";
		this.runner()//
				.andExpect(this.isOk()) //
				.andDo(this.content(new Dealer<User>() {
					@Override
					public void deal(User user) {
						Assert.assertEquals("测试描述信息", user.getRemark());
					}
				}, User.class));
	}

	// 高于最低版本,比第二个版本低
	@Test
	public void test03GreaterFirstLessSecond() throws Exception {
		apiVersion = "0.9.0";
		this.runner()//
				.andExpect(this.isOk()) //
				.andDo(this.content(new Dealer<User>() {
					@Override
					public void deal(User user) {
						Assert.assertEquals("测试描述信息", user.getRemark());
					}
				}, User.class));
	}

	// 等于第二个版本
	@Test
	public void test04EqualSecond() throws Exception {
		apiVersion = "1.0.0";
		this.runner()//
				.andExpect(this.isOk()) //
				.andDo(this.content(new Dealer<User>() {
					@Override
					public void deal(User user) {
						Assert.assertEquals("花落寞离", user.getNickname());
					}
				}, User.class));
	}

	// 高于第一个、第二个版本,低于第三个版本
	@Test
	public void test05GreaterSecondLessThird() throws Exception {
		apiVersion = "1.0.0.1";
		this.runner()//
				.andExpect(this.isOk()) //
				.andDo(this.content(new Dealer<User>() {
					@Override
					public void deal(User user) {
						Assert.assertEquals("花落寞离", user.getNickname());
					}
				}, User.class));
	}

	// 等于第三个版本
	@Test
	public void test06EqualsThird() throws Exception {
		apiVersion = "1.0.1";
		this.runner()//
				.andExpect(this.isOk()) //
				.andDo(this.content(new Dealer<User>() {
					@Override
					public void deal(User user) {
						Assert.assertEquals(20, user.getAge().intValue());
					}
				}, User.class));
	}

	// 高于所有版本
	@Test
	public void test07GreaterThird() throws Exception {
		apiVersion = "5.0";
		this.runner()//
				.andExpect(this.isOk()) //
				.andDo(this.content(new Dealer<User>() {
					@Override
					public void deal(User user) {
						Assert.assertEquals(20, user.getAge().intValue());
					}
				}, User.class));
	}

	// 没有指定版本号
	@Test
	public void test08NoVersion() throws Exception {
		apiVersion = null;
		this.runner()//
				.andExpect(this.isOk()) //
				.andDo(this.content(new Dealer<User>() {
					@Override
					public void deal(User user) {
						Assert.assertEquals(20, user.getAge().intValue());
					}
				}, User.class));
	}

	// 指定版本号为空值
	@Test
	public void test09EmptyVersion() throws Exception {
		apiVersion = "";
		this.runner()//
				.andExpect(this.isOk()) //
				.andDo(this.content(new Dealer<User>() {
					@Override
					public void deal(User user) {
						Assert.assertEquals(20, user.getAge().intValue());
					}
				}, User.class));
	}

	private ResultActions runner() throws Exception {
		MockHttpServletRequestBuilder builder = this.urlEncoded("/test/user/find");

		if (apiVersion != null) {
			builder.header("apiVersion", apiVersion);
		}
		return mockMvc
				.perform(builder//
						.param("username", "hualuomoli"))//
				.andDo(this.content());
	}

}
