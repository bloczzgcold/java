package com.github.hualuomoli.demo.gateway.server.biz.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.alibaba.fastjson.JSON;
import com.github.hualuomoli.demo.gateway.server.biz.entity.User;
import com.github.hualuomoli.demo.gateway.server.controller.MockControllerTest;
import com.github.hualuomoli.gateway.server.enums.CodeEnum;
import com.github.hualuomoli.gateway.server.enums.SignatureTypeEnum;
import com.github.hualuomoli.test.mock.MockTest;
import com.github.hualuomoli.tool.security.RSA;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MockGatewayControllerTest extends MockControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(MockGatewayControllerTest.class);

	private static final String privateKeyBase64 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJDbtC/LQthh7uaFsD7ExqYQKWWhye3r/J1ifLqngLmTT+V1jwJdokrPG7hSzB50h6NQzlEZ4PFl2LGuGYr0yJJdahxdaz6kfaDBro5a8DO7KmE4fAGEhqFXEjvvf+Gs/R90CayMHK5BUBM4xpQtwMbTq0QbNMfzcBhEsD/Lc/g7AgMBAAECgYBfalglox1Eqj1SWnzc24B9oeeiqg74SJj8kgLWb766fe4Cloy8YjCkVgdMQj1xUhCF4pQDl6gzWYKChssMXHA/9b0YvSRFsCc32e/cqqApSGdeHKzhVS418ojc4LckCgq3fTtZPKxt8S1HG4QcJRu6sMtU5shjLxe1WioDWX/eAQJBAOtDIC4yjLVS42SNR6zrp0ntskYUGNT9n80znPbJjdZ5yymlPTQRdzR6n6UubjtQxEkYq22X6DTeWRW0EORXENkCQQCdoIq8bZ/I8lSakd2UaugM8IwBVSIsgGZ969BBRoZPTyIk5Vht1XJ2X9b2xxIoADyJZfd0sqNysQhULw5uZmUzAkBtfccrWQFdnl8QPCSAmQg5gvO2Y8IO1p8Z3IyP2sw1ZmekUTAD3KES/oLwWISa/ILt1hpqnglHGbhyPmSiMNc5AkBNRyH9UzldCQFVbmHVm7v8bAoXtSc17hVRcsT825iJVWCF+jKqVlTxl/cJsXtDRSpoqibxfYsIdaaBrzhCA81lAkEA6fDs2dWBXDVcnTREr71y6UMyKubxE0aGJMsoE/FTkOo8y0z/EDewZoVnK5qFSOPZUZoknCQiVP3kln15BZvWQg==";
	private static final String publicKeyBase64 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCfYQmI9qwot7MsIIcJ19ZxeDdajUByjxKNn5zkT8dTsrqOM1M/PY5Tt+lsNxzWQFplElN3de2LKMGG6Q3NQ9qHGWusTLVOW1cpafHcatDwIWV8MZ0E+SgCMgvIJbU3ZUOG3KZEgVkA9qiL93oMMKRKoAPo4LS4gSKQViHkAPKoBwIDAQAB";

	private String apiVersion = "1.0.0";
	private Request req;
	private Response res;

	@Before
	public void before() {

		User user = new User();
		user.setUsername("hualuomoli");

		req = new Request();
		req.partnerId = "tester";
		req.apiMethod = "test.user.find";
		req.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S").format(new Date());
		req.bizContent = JSON.toJSONString(user);
		req.signType = SignatureTypeEnum.RSA.name();

		StringBuilder buffer = new StringBuilder();
		buffer.append("&apiMethod=").append(req.apiMethod);
		buffer.append("&bizContent=").append(req.bizContent);
		buffer.append("&gatewayVersion=").append(req.gatewayVersion);
		buffer.append("&partnerId=").append(req.partnerId);
		buffer.append("&signType=").append(req.signType);
		buffer.append("&timestamp=").append(req.timestamp);
		String origin = buffer.toString().substring(1);

		logger.debug("签名原文={}", origin);

		req.sign = RSA.signBase64(privateKeyBase64, origin);
	}

	@After
	public void after() {
		Assert.assertNotNull(res);

		StringBuilder buffer = new StringBuilder();
		buffer.append("&code=").append(res.code);
		buffer.append("&gatewayVersion=").append(res.gatewayVersion);
		buffer.append("&message=").append(res.message);
		buffer.append("&partnerId=").append(res.partnerId);
		buffer.append("&signType=").append(res.signType);
		if (StringUtils.isNotBlank(res.subCode)) {
			buffer.append("&subCode=").append(res.subCode);
			buffer.append("&subMessage=").append(res.subMessage);
		}
		buffer.append("&timestamp=").append(res.timestamp);
		buffer.append("&result=").append(res.result);

		String origin = buffer.toString().substring(1);

		logger.debug("签名原文={}", origin);
		RSA.verify(publicKeyBase64, origin, res.sign);

	}

	// 低于最低版本
	@Test
	public void test01LessFirst() throws Exception {
		apiVersion = "0.0.0.1";
		this.runner() //
				.andExpect(MockTest.isOk()) //
				.andDo(MockTest.content(new MockTest.Dealer<Response>() {
					@Override
					public void deal(Response res) {
						MockGatewayControllerTest.this.res = res;

						Assert.assertEquals(CodeEnum.NO_BUSINESS_HANDLER_METHOD.value(), res.code);
					}
				}, Response.class));
	}

	// 等于第一个版本
	@Test
	public void test02EqualFirst() throws Exception {
		apiVersion = "0.0.1";
		this.runner() //
				.andExpect(MockTest.isOk()) //
				.andDo(MockTest.content(new MockTest.Dealer<Response>() {
					@Override
					public void deal(Response res) {
						MockGatewayControllerTest.this.res = res;

						Assert.assertEquals("0000", res.code);
						User user = JSON.parseObject(res.result, User.class);
						Assert.assertEquals("测试描述信息", user.getRemark());
					}
				}, Response.class));
	}

	// 高于最低版本,比第二个版本低
	@Test
	public void test03GreaterFirstLessSecond() throws Exception {
		apiVersion = "0.9.0";
		this.runner() //
				.andExpect(MockTest.isOk()) //
				.andDo(MockTest.content(new MockTest.Dealer<Response>() {
					@Override
					public void deal(Response res) {
						MockGatewayControllerTest.this.res = res;

						Assert.assertEquals("0000", res.code);
						User user = JSON.parseObject(res.result, User.class);
						Assert.assertEquals("测试描述信息", user.getRemark());
					}
				}, Response.class));
	}

	// 等于第二个版本
	@Test
	public void test04EqualSecond() throws Exception {
		apiVersion = "1.0.0";
		this.runner() //
				.andExpect(MockTest.isOk()) //
				.andDo(MockTest.content(new MockTest.Dealer<Response>() {
					@Override
					public void deal(Response res) {
						MockGatewayControllerTest.this.res = res;

						Assert.assertEquals("0000", res.code);
						User user = JSON.parseObject(res.result, User.class);
						Assert.assertEquals("花落寞离", user.getNickname());
					}
				}, Response.class));
	}

	// 高于第一个、第二个版本,低于第三个版本
	@Test
	public void test05GreaterSecondLessThird() throws Exception {
		apiVersion = "1.0.0.1";
		this.runner() //
				.andExpect(MockTest.isOk()) //
				.andDo(MockTest.content(new MockTest.Dealer<Response>() {
					@Override
					public void deal(Response res) {
						MockGatewayControllerTest.this.res = res;

						Assert.assertEquals("0000", res.code);
						User user = JSON.parseObject(res.result, User.class);
						Assert.assertEquals("花落寞离", user.getNickname());
					}
				}, Response.class));
	}

	// 等于第三个版本
	@Test
	public void test06EqualsThird() throws Exception {
		apiVersion = "1.0.1";
		this.runner() //
				.andExpect(MockTest.isOk()) //
				.andDo(MockTest.content(new MockTest.Dealer<Response>() {
					@Override
					public void deal(Response res) {
						MockGatewayControllerTest.this.res = res;

						Assert.assertEquals("0000", res.code);
						User user = JSON.parseObject(res.result, User.class);
						Assert.assertEquals(20, user.getAge().intValue());
					}
				}, Response.class));
	}

	// 高于所有版本
	@Test
	public void test07GreaterThird() throws Exception {
		apiVersion = "5.0";
		this.runner() //
				.andExpect(MockTest.isOk()) //
				.andDo(MockTest.content(new MockTest.Dealer<Response>() {
					@Override
					public void deal(Response res) {
						MockGatewayControllerTest.this.res = res;

						Assert.assertEquals("0000", res.code);
						User user = JSON.parseObject(res.result, User.class);
						Assert.assertEquals(20, user.getAge().intValue());
					}
				}, Response.class));
	}

	// 没有指定版本号
	@Test
	public void test08NoVersion() throws Exception {
		apiVersion = null;
		this.runner() //
				.andExpect(MockTest.isOk()) //
				.andDo(MockTest.content(new MockTest.Dealer<Response>() {
					@Override
					public void deal(Response res) {
						MockGatewayControllerTest.this.res = res;

						Assert.assertEquals("0000", res.code);
						User user = JSON.parseObject(res.result, User.class);
						Assert.assertEquals(20, user.getAge().intValue());
					}
				}, Response.class));
	}

	// 指定版本号为空值
	@Test
	public void test09EmptyVersion() throws Exception {
		apiVersion = "";
		this.runner() //
				.andExpect(MockTest.isOk()) //
				.andDo(MockTest.content(new MockTest.Dealer<Response>() {
					@Override
					public void deal(Response res) {
						MockGatewayControllerTest.this.res = res;

						Assert.assertEquals("0000", res.code);
						User user = JSON.parseObject(res.result, User.class);
						Assert.assertEquals(20, user.getAge().intValue());
					}
				}, Response.class));
	}

	private ResultActions runner() throws Exception {
		MockHttpServletRequestBuilder builder = MockTest.urlEncoded("/test/gateway");

		if (apiVersion != null) {
			builder.header("apiVersion", apiVersion);
		}
		return mockMvc
				.perform(builder//
						.param("partnerId", req.partnerId) //
						.param("apiMethod", req.apiMethod) //
						.param("timestamp", req.timestamp) //
						.param("bizContent", req.bizContent) //
						.param("signType", req.signType) //
						.param("sign", req.sign)) //
				.andExpect(MockTest.isOk()) //
				.andDo(MockTest.content());
	}

	public static class Request {
		/** 网关版本号 */
		private final String gatewayVersion = "1.0.0";
		/** 合作伙伴ID */
		protected String partnerId;
		/** 请求的业务方法 */
		protected String apiMethod;
		/** 时间戳 yyyyMMddHHmmss */
		protected String timestamp;
		/** 业务内容 */
		protected String bizContent;
		/** 签名类型 */
		private String signType;
		/** 签名数据 */
		private String sign;

		public String getPartnerId() {
			return partnerId;
		}

		public String getApiMethod() {
			return apiMethod;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public String getBizContent() {
			return bizContent;
		}

		public String getSignType() {
			return signType;
		}

		public String getSign() {
			return sign;
		}
	}

	public static class Response {
		/** 调用结果编码 #CodeEnum */
		private String code;
		/** 调用结果信息 */
		private String message;
		/** 业务处理编码 #CodeEnum */
		private String subCode;
		/** 业务处理信息 */
		private String subMessage;
		/** 网关版本号 */
		private String gatewayVersion;
		/** 合作伙伴ID */
		private String partnerId;
		/** 时间戳 yyyyMMddHHmmss */
		private String timestamp;
		/** 响应内容 */
		private String result;
		/** 签名类型 */
		private String signType;
		/** 签名数据 */
		private String sign;

		public void setCode(String code) {
			this.code = code;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public void setSubCode(String subCode) {
			this.subCode = subCode;
		}

		public void setSubMessage(String subMessage) {
			this.subMessage = subMessage;
		}

		public void setGatewayVersion(String gatewayVersion) {
			this.gatewayVersion = gatewayVersion;
		}

		public void setPartnerId(String partnerId) {
			this.partnerId = partnerId;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public void setSignType(String signType) {
			this.signType = signType;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}

	}

}
