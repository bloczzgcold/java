package com.github.hualuomoli.demo.framework.biz.controller;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.hualuomoli.demo.framework.controller.ControllerTest;
import com.github.hualuomoli.test.mock.MockTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest extends ControllerTest {

	@Test
	public void testJson() throws Exception {
		mockMvc.perform(MockTest.urlEncoded("/test/user/json")//
				.param("id", "1234"))//
				.andDo(MockTest.print()) //
				.andDo(MockTest.content())//
				.andExpect(MockTest.isOk());
	}

	@Test
	public void testView() throws Exception {
		mockMvc.perform(MockTest.get("/test/user/view"))//
				.andDo(MockTest.print()) //
				.andDo(MockTest.forwardedUrl()) //
				.andExpect(MockTest.isOk());
	}

	@Test
	public void testFind() throws Exception {
		mockMvc.perform(MockTest.urlEncoded("/test/user/find")//
				.param("username", "hualuomoli"))//
				.andDo(MockTest.print()) //
				.andDo(MockTest.content())//
				.andExpect(MockTest.isOk());
	}

}
