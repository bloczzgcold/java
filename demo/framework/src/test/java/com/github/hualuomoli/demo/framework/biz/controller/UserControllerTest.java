package com.github.hualuomoli.demo.framework.biz.controller;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.hualuomoli.demo.framework.controller.ControllerTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest extends ControllerTest {

	@Test
	public void testJson() throws Exception {
		mockMvc.perform(this.urlEncoded("/test/user/json")//
				.param("id", "1234"))//
				.andDo(this.print()) //
				.andDo(this.content())//
				.andExpect(this.isOk());
	}

	@Test
	public void testView() throws Exception {
		mockMvc.perform(this.get("/test/user/view"))//
				.andDo(this.print()) //
				.andDo(this.forwardedUrl()) //
				.andExpect(this.isOk());
	}

	@Test
	public void testFind() throws Exception {
		mockMvc.perform(this.urlEncoded("/test/user/find")//
				.param("username", "hualuomoli"))//
				.andDo(this.print()) //
				.andDo(this.content())//
				.andExpect(this.isOk());
	}

}
