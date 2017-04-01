package com.github.hualuomoli.test.gateway.server.biz.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.github.hualuomoli.gateway.server.constants.CodeEnum;
import com.github.hualuomoli.gateway.server.constants.SignatureTypeEnum;
import com.github.hualuomoli.test.gateway.server.biz.entity.User;
import com.github.hualuomoli.tool.security.RSA;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GatewayControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(GatewayControllerTest.class);

	private static final String privateKeyBase64 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIjM6DZ+1QSDCgWmCPLqHd006iMhUulpxzAcQ8V3as5eJus+AzZ9PnLjqxOikZLa2TJUj2HqD55Z9QGaXXZIHXuW4WBVVwOlvJqafqlJooUyLBf+oDVuuR6KE7PksT7wXwBrt/3A/c5i9qAm4KAVBoPjVBo2CRoXbwm96MWw8vPpAgMBAAECgYAUKxF5J03EcoVBu+7J0VZBzJFQSJNusheZsjETpb1rW1oliGj02ZYhXgtUw7zuh7FJyEmzR8yxrdV/PQDWdV9TJ6EYlq70Rx3GTAB3oaZ8liknBCAm9tt4yYCYWZQwhM7PNqplp89T7hxi4EkO/op7ndqMC3o658oWyFDy1YbTAQJBANMNDKvKv8Esi/+JinLCdvI2SSM6pjHMzS6DUqgUtYehougJUwUNERt2UDtoPSKBTQRVMxoXhnlveWeNjNFJi7kCQQCl7473FgQ/py09dgEpRfOGcfzs5+OFI3Aeo/2OTzhb/JVGYcSxvXNssU2EdRExVCPxwA7aEYUmbI0lqiX/CqGxAkEAsNg3bN/4K6LrMFWMyDqAttacFEP5rLMCnt31bdfkgGEFg08E3K+lTRCnjA/9YiNcaKwHpO5fhPPOk+G9REVPKQJAM334XmJ9pQcKueTRjW5AmLrsotN+vgT2OPvope2dbqYd6H5UXFeO5u7KQE1XrXgPw2g286K2L+tFjaeULOWusQJAIq3bsGWoxNVRpMuCbYpXdxoFVNsT+m1z3DMpmxbLY5/1it14Izdv+7a3BrCxADbeA+/spWfIM3azNvLPFbRSkg==";
	private String apiVersion = "1.0.0";

	// 低于最低版本
	@Test
	public void test01LessFirst() throws IOException {
		apiVersion = "0.0.0.1";
		Response res = this.runner();
		Assert.assertEquals(CodeEnum.NO_BUSINESS_HANDLER_FOUND.value(), res.code);
	}

	// 等于第一个版本
	@Test
	public void test02EqualFirst() throws IOException {
		apiVersion = "0.0.1";
		Response res = this.runner();
		Assert.assertEquals("0000", res.code);
		User user = JSON.parseObject(res.result, User.class);
		Assert.assertEquals("测试描述信息", user.getRemark());
	}

	// 高于最低版本,比第二个版本低
	@Test
	public void test03GreaterFirstLessSecond() throws IOException {
		apiVersion = "0.9.0";
		Response res = this.runner();
		Assert.assertEquals("0000", res.code);
		User user = JSON.parseObject(res.result, User.class);
		Assert.assertEquals("测试描述信息", user.getRemark());
	}

	// 等于第二个版本
	@Test
	public void test04EqualSecond() throws IOException {
		apiVersion = "1.0.0";
		Response res = this.runner();
		Assert.assertEquals("0000", res.code);
		User user = JSON.parseObject(res.result, User.class);
		Assert.assertEquals("花落寞离", user.getNickname());
	}

	// 高于第一个、第二个版本,低于第三个版本
	@Test
	public void test05GreaterSecondLessThird() throws IOException {
		apiVersion = "1.0.0.1";
		Response res = this.runner();
		Assert.assertEquals("0000", res.code);
		User user = JSON.parseObject(res.result, User.class);
		Assert.assertEquals("花落寞离", user.getNickname());
	}

	// 等于第三个版本
	@Test
	public void test06EqualsThird() throws IOException {
		apiVersion = "1.0.1";
		Response res = this.runner();
		Assert.assertEquals("0000", res.code);
		User user = JSON.parseObject(res.result, User.class);
		Assert.assertEquals(20, user.getAge().intValue());
	}

	// 高于所有版本
	@Test
	public void test07GreaterThird() throws IOException {
		apiVersion = "5.0";
		Response res = this.runner();
		Assert.assertEquals("0000", res.code);
		User user = JSON.parseObject(res.result, User.class);
		Assert.assertEquals(20, user.getAge().intValue());
	}

	// 没有指定版本号
	@Test
	public void test08NoVersion() throws IOException {
		apiVersion = null;
		Response res = this.runner();
		Assert.assertEquals("0000", res.code);
		User user = JSON.parseObject(res.result, User.class);
		Assert.assertEquals(20, user.getAge().intValue());
	}

	// 指定版本号为空值
	@Test
	public void test09EmptyVersion() throws IOException {
		apiVersion = "";
		Response res = this.runner();
		Assert.assertEquals("0000", res.code);
		User user = JSON.parseObject(res.result, User.class);
		Assert.assertEquals(20, user.getAge().intValue());
	}

	private Response runner() throws IOException {

		User user = new User();
		user.setUsername("hualuomoli");

		Request req = new Request();
		req.partnerId = "123456789";
		req.apiMethod = "test.user.find";
		req.timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		req.bizContent = JSON.toJSONString(user);
		req.signType = SignatureTypeEnum.RSA.name();

		StringBuilder buffer = new StringBuilder();
		buffer.append("&apiMethod=").append(req.apiMethod);
		buffer.append("&bizContent=").append(req.bizContent);
		buffer.append("&partnerId=").append(req.partnerId);
		buffer.append("&signType=").append(req.signType);
		buffer.append("&timestamp=").append(req.timestamp);
		String origin = buffer.toString().substring(1);

		logger.debug("签名原文={}", origin);

		req.signData = RSA.signBase64(privateKeyBase64, origin);

		byte[] data = this.invoke(this.parseEntity(req, "utf-8").getBytes("UTF-8"));
		String result = new String(data, "UTF-8");

		logger.debug("响应内容={}", result);

		return JSON.parseObject(result, Response.class);
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
		private String signData;

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

		public String getSignData() {
			return signData;
		}
	}

	@SuppressWarnings("unused")
	public static class Response {
		/** 调用结果编码 #CodeEnum */
		private String code;
		/** 调用结果信息 */
		private String message;
		/** 业务处理编码 #CodeEnum */
		private String subCode;
		/** 业务处理信息 */
		private String subMessage;
		/** 合作伙伴ID */
		private String partnerId;
		/** 时间戳 yyyyMMddHHmmss */
		private String timestamp;
		/** 响应内容 */
		private String result;
		/** 签名类型 */
		private String signType;
		/** 签名数据 */
		private String signData;

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

		public void setSignData(String signData) {
			this.signData = signData;
		}

	}

	private HttpURLConnection getConnection() throws IOException {
		URL url = new URL("http://localhost/test/gateway");
		return (HttpURLConnection) url.openConnection();
	}

	/**
	 * 转换实体类
	 * @param entity 实体类
	 * @param charset 编码集
	 * @return 转换后的文本
	 */
	private String parseEntity(Object entity, String charset) {
		StringBuilder buffer = new StringBuilder();

		Class<?> clazz = entity.getClass();

		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			String name = field.getName();
			int mod = field.getModifiers();
			if (Modifier.isPublic(mod) && Modifier.isStatic(mod) && Modifier.isFinal(mod)) {
				// public  fanal
				continue;
			}
			String apiMethodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
			try {
				String value = (String) clazz.getMethod(apiMethodName).invoke(entity);
				if (StringUtils.isBlank(value)) {
					continue;
				}
				buffer.append("&").append(name).append("=").append(encoded(value, charset));
			} catch (Exception e) {
			}
		}
		String data = buffer.toString().substring(1);
		logger.debug("请求数据={}", data);
		return data;
	}

	/**
	 * 编码
	 * @param value 值
	 * @param charset 编码方式
	 * @return 编码后的值
	 */
	private String encoded(String value, String charset) {
		try {
			return URLEncoder.encode(value, charset);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	private byte[] invoke(byte[] content) throws IOException {
		HttpURLConnection conn = null;
		OutputStream os = null;
		InputStream is = null;

		try {

			conn = this.getConnection();

			// method
			conn.setRequestMethod("POST");

			conn.setDoInput(true); // input
			conn.setDoOutput(true); // output

			// header
			conn.setRequestProperty("apiVersion", apiVersion);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			// output data
			os = conn.getOutputStream();
			os.write(content);

			// get response
			if (conn.getResponseCode() == 200) {
				// 成功
				is = conn.getInputStream();
				return IOUtils.toByteArray(is);
			} else if (conn.getResponseCode() == 404) {
				// 未找到
				throw new IOException("404");
			} else if (conn.getResponseCode() == 500) {
				// 失败
				is = conn.getErrorStream();
				throw new IOException(IOUtils.toString(is));
			} else {
				// 其他错误
				throw new IOException(conn.getResponseCode() + "");
			}

		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
