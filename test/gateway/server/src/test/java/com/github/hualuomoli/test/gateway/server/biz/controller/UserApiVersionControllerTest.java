package com.github.hualuomoli.test.gateway.server.biz.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.github.hualuomoli.test.gateway.server.biz.entity.User;

public class UserApiVersionControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(UserApiVersionControllerTest.class);

	private String version = "1.0.0";

	@Test
	public void test() throws IOException {

		User user = null;

		// 低于最低版本
		version = "0.0.0.1";
		user = this.runner();

		// 使用最低版本
		version = "0.0.1";
		user = this.runner();
		Assert.assertEquals("测试描述信息", user.getRemark());

		// 高于最低版本,比第二个版本低.使用最低版本
		version = "0.9.0";
		user = this.runner();
		Assert.assertEquals("测试描述信息", user.getRemark());

		// 等于第二个版本,使用第二个版本
		version = "1.0.0";
		user = this.runner();
		Assert.assertEquals("花落寞离", user.getNickname());

		// 高于第一个、第二个版本,低于第三个版本.使用第二个版本
		version = "1.0.0.1";
		user = this.runner();
		Assert.assertEquals("花落寞离", user.getNickname());

		// 等于第三个版本,使用第三个版本
		version = "1.0.1";
		user = this.runner();
		Assert.assertEquals(20, user.getAge().intValue());

		// 高于所有版本,使用第三个版本
		version = "5.0";
		user = this.runner();
		Assert.assertEquals(20, user.getAge().intValue());

		// 没有指定版本号,使用最大版本
		version = null;
		user = this.runner();
		Assert.assertEquals(20, user.getAge().intValue());

		// 指定版本号为空值,使用最大版本
		version = "";
		user = this.runner();
		Assert.assertEquals(20, user.getAge().intValue());

	}

	public User runner() throws IOException {

		User user = new User();
		user.setUsername("hualuomoli");

		byte[] data = this.invoke(this.parseEntity(user, "utf-8").getBytes("UTF-8"));
		String result = new String(data, "UTF-8");

		logger.debug("响应内容={}", result);

		return JSON.parseObject(result, User.class);
	}

	private HttpURLConnection getConnection() throws IOException {
		URL url = new URL("http://localhost/test/user/find");
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
			String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
			try {
				String value = (String) clazz.getMethod(methodName).invoke(entity);
				if (StringUtils.isBlank(value)) {
					continue;
				}
				buffer.append("&").append(name).append("=").append(encoded(value, charset));
			} catch (Exception e) {
			}
		}
		return buffer.toString().substring(1);
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
			conn.setRequestProperty("version", version);
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
