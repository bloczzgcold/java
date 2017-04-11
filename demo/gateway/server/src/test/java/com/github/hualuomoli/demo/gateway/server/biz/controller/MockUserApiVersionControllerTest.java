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
import com.github.hualuomoli.test.mock.MockTest;

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
				.andExpect(MockTest.isOk()) //
				.andDo(MockTest.content(new MockControllerTest.Dealer<User>() {
					@Override
					public void deal(User user) {
						Assert.assertEquals("测试描述信息", user.getRemark());
					}
				}));
	}

	// 高于最低版本,比第二个版本低
	@Test
	public void test03GreaterFirstLessSecond() throws Exception {
		apiVersion = "0.9.0";
		this.runner()//
				.andExpect(MockTest.isOk()) //
				.andDo(MockTest.content(new MockControllerTest.Dealer<User>() {
					@Override
					public void deal(User user) {
						Assert.assertEquals("测试描述信息", user.getRemark());
					}
				}));
	}

	// 等于第二个版本
	@Test
	public void test04EqualSecond() throws Exception {
		apiVersion = "1.0.0";
		this.runner()//
				.andExpect(MockTest.isOk()) //
				.andDo(MockTest.content(new MockControllerTest.Dealer<User>() {
					@Override
					public void deal(User user) {
						Assert.assertEquals("花落寞离", user.getNickname());
					}
				}));
	}

	// 高于第一个、第二个版本,低于第三个版本
	@Test
	public void test05GreaterSecondLessThird() throws Exception {
		apiVersion = "1.0.0.1";
		this.runner()//
				.andExpect(MockTest.isOk()) //
				.andDo(MockTest.content(new MockControllerTest.Dealer<User>() {
					@Override
					public void deal(User user) {
						Assert.assertEquals("花落寞离", user.getNickname());
					}
				}));
	}

	// 等于第三个版本
	@Test
	public void test06EqualsThird() throws Exception {
		apiVersion = "1.0.1";
		this.runner()//
				.andExpect(MockTest.isOk()) //
				.andDo(MockTest.content(new MockControllerTest.Dealer<User>() {
					@Override
					public void deal(User user) {
						Assert.assertEquals(20, user.getAge().intValue());
					}
				}));
	}

	// 高于所有版本
	@Test
	public void test07GreaterThird() throws Exception {
		apiVersion = "5.0";
		this.runner()//
				.andExpect(MockTest.isOk()) //
				.andDo(MockTest.content(new MockControllerTest.Dealer<User>() {
					@Override
					public void deal(User user) {
						Assert.assertEquals(20, user.getAge().intValue());
					}
				}));
	}

	// 没有指定版本号
	@Test
	public void test08NoVersion() throws Exception {
		apiVersion = null;
		this.runner()//
				.andExpect(MockTest.isOk()) //
				.andDo(MockTest.content(new MockControllerTest.Dealer<User>() {
					@Override
					public void deal(User user) {
						Assert.assertEquals(20, user.getAge().intValue());
					}
				}));
	}

	// 指定版本号为空值
	@Test
	public void test09EmptyVersion() throws Exception {
		apiVersion = "";
		this.runner()//
				.andExpect(MockTest.isOk()) //
				.andDo(MockTest.content(new MockControllerTest.Dealer<User>() {
					@Override
					public void deal(User user) {
						Assert.assertEquals(20, user.getAge().intValue());
					}
				}));
	}

	private ResultActions runner() throws Exception {
		MockHttpServletRequestBuilder builder = MockTest.urlEncoded("/test/user/find");

		if (apiVersion != null) {
			builder.header("apiVersion", apiVersion);
		}
		return mockMvc
				.perform(builder//
						.param("username", "hualuomoli"))//
				.andDo(MockTest.content());
	}

}
