package com.github.hualuomoli.demo.gateway.server.biz.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.alibaba.fastjson.JSON;
import com.github.hualuomoli.demo.gateway.server.biz.entity.User;
import com.github.hualuomoli.demo.gateway.server.controller.ClientControllerTest;
import com.github.hualuomoli.gateway.client.http.HttpClient;
import com.github.hualuomoli.gateway.client.lang.DealException;
import com.github.hualuomoli.gateway.client.lang.GatewayException;
import com.github.hualuomoli.gateway.server.enums.SignatureTypeEnum;
import com.google.common.collect.Lists;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientGatewayControllerTest extends ClientControllerTest {

	private String apiVersion = "1.0.0";

	// 低于最低版本
	@Test(expected = GatewayException.class)
	public void test01LessFirst() throws IOException, DealException, GatewayException {
		apiVersion = "0.0.0.1";
		this.runner();
	}

	// 等于第一个版本
	@Test
	public void test02EqualFirst() throws IOException, DealException, GatewayException {
		apiVersion = "0.0.1";
		User user = this.runner();
		Assert.assertEquals("测试描述信息", user.getRemark());
	}

	// 高于最低版本,比第二个版本低
	@Test
	public void test03GreaterFirstLessSecond() throws IOException, DealException, GatewayException {
		apiVersion = "0.9.0";
		User user = this.runner();
		Assert.assertEquals("测试描述信息", user.getRemark());
	}

	// 等于第二个版本
	@Test
	public void test04EqualSecond() throws IOException, DealException, GatewayException {
		apiVersion = "1.0.0";
		User user = this.runner();
		Assert.assertEquals("花落寞离", user.getNickname());
	}

	// 高于第一个、第二个版本,低于第三个版本
	@Test
	public void test05GreaterSecondLessThird() throws IOException, DealException, GatewayException {
		apiVersion = "1.0.0.1";
		User user = this.runner();
		Assert.assertEquals("花落寞离", user.getNickname());
	}

	// 等于第三个版本
	@Test
	public void test06EqualsThird() throws IOException, DealException, GatewayException {
		apiVersion = "1.0.1";
		User user = this.runner();
		Assert.assertEquals(20, user.getAge().intValue());
	}

	// 高于所有版本
	@Test
	public void test07GreaterThird() throws IOException, DealException, GatewayException {
		apiVersion = "5.0";
		User user = this.runner();
		Assert.assertEquals(20, user.getAge().intValue());
	}

	// 没有指定版本号
	@Test
	public void test08NoVersion() throws IOException, DealException, GatewayException {
		apiVersion = null;
		User user = this.runner();
		Assert.assertEquals(20, user.getAge().intValue());
	}

	// 指定版本号为空值
	@Test
	public void test09EmptyVersion() throws IOException, DealException, GatewayException {
		apiVersion = "";
		User user = this.runner();
		Assert.assertEquals(20, user.getAge().intValue());
	}

	private User runner() throws IOException, DealException, GatewayException {

		User user = new User();
		user.setUsername("hualuomoli");

		Request req = new Request();
		req.partnerId = "tester";
		req.apiMethod = "test.user.find";
		req.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S").format(new Date());
		req.bizContent = JSON.toJSONString(user);
		req.signType = SignatureTypeEnum.RSA.name();

		List<HttpClient.Header> requestHeaders = Lists.newArrayList();
		requestHeaders.add(new HttpClient.Header("apiVersion", apiVersion));

		// 添加header头信息
		client.addHeader("apiVersion", apiVersion);

		return client.callObject("test.user.find", JSON.toJSONString(user), User.class);
	}

	public static class Request {
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

}
