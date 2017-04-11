package com.github.hualuomoli.test.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class MockTest {

	private static final Logger logger = LoggerFactory.getLogger(MockTest.class);

	private static final String characterEncoding = "UTF-8";

	// get请求
	public static final MockHttpServletRequestBuilder get(String url) {
		return MockMvcRequestBuilders.get(url)//
				.characterEncoding(characterEncoding);
	}

	// get请求
	public static final MockHttpServletRequestBuilder get(String url, Object... urlParams) {
		return MockMvcRequestBuilders.get(url, urlParams)//
				.characterEncoding(characterEncoding);
	}

	// delete
	public static final MockHttpServletRequestBuilder delete(String url) {
		return MockMvcRequestBuilders.delete(url);
	}

	// delete
	public static final MockHttpServletRequestBuilder delete(String url, Object... urlParams) {
		return MockMvcRequestBuilders.delete(url, urlParams);
	}

	// post
	public static final MockHttpServletRequestBuilder post(String url) {
		return MockMvcRequestBuilders.post(url)//
				.characterEncoding(characterEncoding);
	}

	// post
	public static final MockHttpServletRequestBuilder post(String url, Object... urlParams) {
		return MockMvcRequestBuilders.post(url, urlParams)//
				.characterEncoding(characterEncoding);
	}

	// urlEncoded
	public static final MockHttpServletRequestBuilder urlEncoded(String url) {
		return MockTest.post(url) //
				.contentType(MediaType.APPLICATION_FORM_URLENCODED);
	}

	// urlEncoded
	public static final MockHttpServletRequestBuilder urlEncoded(String url, Object... urlParams) {
		return MockTest.post(url, urlParams) //
				.contentType(MediaType.APPLICATION_FORM_URLENCODED);
	}

	// json
	public static final MockHttpServletRequestBuilder json(String url) {
		return MockTest.post(url) //
				.contentType(MediaType.APPLICATION_JSON);
	}

	// json
	public static final MockHttpServletRequestBuilder json(String url, Object... urlParams) {
		return MockTest.post(url, urlParams) //
				.contentType(MediaType.APPLICATION_JSON);
	}

	// fileUpload
	public static final MockMultipartHttpServletRequestBuilder fileUpload(String url) {
		return MockMvcRequestBuilders.fileUpload(url);
	}

	// fileUpload
	public static final MockMultipartHttpServletRequestBuilder fileUpload(String url, Object... urlParams) {
		return MockMvcRequestBuilders.fileUpload(url, urlParams);
	}

	// 是否成功
	public static final ResultMatcher isOk() {
		return MockMvcResultMatchers.status().isOk();
	}

	// 打印响应信息
	public static final ResultHandler print() {
		return MockMvcResultHandlers.print();
	}

	// 打印响应内容
	public static final ResultHandler content() {
		return new ResultHandler() {

			@Override
			public void handle(MvcResult result) throws Exception {
				byte[] bytes = result.getResponse().getContentAsByteArray();
				logger.debug("返回内容={}", new String(bytes, characterEncoding));
			}
		};
	}

	// 打印响应内容
	public static final <T> ResultHandler content(final Dealer dealer) {
		return new ResultHandler() {

			@Override
			public void handle(MvcResult result) throws Exception {
				byte[] bytes = result.getResponse().getContentAsByteArray();
				dealer.deal(new String(bytes, characterEncoding));
			}
		};
	}

	public static interface Dealer {

		void deal(String content);

	}

	// 打印响应内容
	public static final ResultHandler forwardedUrl() {
		return new ResultHandler() {

			@Override
			public void handle(MvcResult result) throws Exception {
				String forwardedUrl = result.getResponse().getForwardedUrl();
				logger.debug("重定向URL={}", forwardedUrl);
			}
		};
	}

}
