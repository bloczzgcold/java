package com.github.hualuomoli.demo.gateway.server.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.github.hualuomoli.demo.gateway.server.config.BaseConfig;
import com.github.hualuomoli.demo.gateway.server.config.MvcConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({ //
		@ContextConfiguration(name = "parent", classes = BaseConfig.class), //
		@ContextConfiguration(name = "child", classes = MvcConfig.class) //
})
public class ControllerTest {

	// http://www.csdn123.com/html/mycsdn20140110/a7/a75383fcc7d869a7627583ada5e76e46.html
	// perform：执行一个RequestBuilder请求，会自动执行SpringMVC的流程并映射到相应的控制器执行处理；
	// andDo：添加ResultHandler结果处理器，比如调试时打印结果到控制台；
	// andExpect：添加ResultMatcher验证规则，验证控制器执行完成后结果是否正确；
	// andReturn：最后返回相应的MvcResult；然后进行自定义验证/进行下一步的异步处理；

	protected static final Logger logger = LoggerFactory.getLogger(ControllerTest.class);

	private static final String characterEncoding = "UTF-8";

	@Autowired
	private WebApplicationContext wac;
	protected MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	// get请求
	public final MockHttpServletRequestBuilder get(String url, Object... urlParams) {
		return MockMvcRequestBuilders.get(url, urlParams)//
				.characterEncoding(characterEncoding);
	}

	// delete
	public final MockHttpServletRequestBuilder delete(String url, Object... urlParams) {
		return MockMvcRequestBuilders.delete(url, urlParams);
	}

	// post
	public final MockHttpServletRequestBuilder post(String url, Object... urlParams) {
		return MockMvcRequestBuilders.post(url, urlParams)//
				.characterEncoding(characterEncoding);
	}

	// urlEncoded
	public final MockHttpServletRequestBuilder urlEncoded(String relativeUrl, Object... urlParams) {
		return this.post(relativeUrl, urlParams) //
				.contentType(MediaType.APPLICATION_FORM_URLENCODED);
	}

	// json
	public final MockHttpServletRequestBuilder json(String url, Object... urlParams) {
		return this.post(url, urlParams) //
				.contentType(MediaType.APPLICATION_JSON);
	}

	// fileUpload
	public final MockMultipartHttpServletRequestBuilder fileUpload(String url, Object... urlParams) {
		return MockMvcRequestBuilders.fileUpload(url, urlParams);
	}

	// 是否成功
	public final ResultMatcher isOk() {
		return MockMvcResultMatchers.status().isOk();
	}

	// 打印响应信息
	public final ResultHandler print() {
		return MockMvcResultHandlers.print();
	}

	// 打印响应内容
	public final ResultHandler content() {
		return new ResultHandler() {

			@Override
			public void handle(MvcResult result) throws Exception {
				byte[] bytes = result.getResponse().getContentAsByteArray();
				System.out.println("返回内容: " + new String(bytes, characterEncoding));
			}
		};
	}

	// 打印响应内容
	public final <T> ResultHandler content(final Dealer<T> dealer, final Class<T> clazz) {
		return new ResultHandler() {

			@Override
			public void handle(MvcResult result) throws Exception {
				byte[] bytes = result.getResponse().getContentAsByteArray();
				T t = JSON.parseObject(new String(bytes, characterEncoding), clazz);
				dealer.deal(t);
			}
		};
	}

	protected interface Dealer<T> {

		void deal(T t);

	}

	// 打印响应内容
	public final ResultHandler forwardedUrl() {
		return new ResultHandler() {

			@Override
			public void handle(MvcResult result) throws Exception {
				String forwardedUrl = result.getResponse().getForwardedUrl();
				System.out.println("重定向URL: " + forwardedUrl);
			}
		};
	}

}
