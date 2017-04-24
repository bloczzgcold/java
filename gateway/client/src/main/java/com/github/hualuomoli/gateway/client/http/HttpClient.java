package com.github.hualuomoli.gateway.client.http;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * HTTP发起请求
 * 
 * @author lbq
 *
 */
public interface HttpClient {

	/**
	 * 执行http请求(urlencoded)
	 * @param url 请求的url
	 * @param charset 数据编码集
	 * @param object 请求的参数
	 * @return 执行结果
	 * @throws IOException 执行错误
	 */
	String urlencoded(String url, Charset charset, Object object) throws IOException;

	/**
	 * 执行http请求(urlencoded)
	 * @param url 请求的url
	 * @param charset 数据编码集
	 * @param paramMap 请求的参数
	 * @return 执行结果
	 * @throws IOException 执行错误
	 */
	String urlencoded(String url, Charset charset, Map<String, Object> paramMap) throws IOException;

	/**
	 * 执行http请求(urlencoded)
	 * @param url 请求的url
	 * @param charset 数据编码集
	 * @param content 请求的内容
	 * @return 执行结果
	 * @throws IOException 执行错误
	 */
	String json(String url, Charset charset, String content) throws IOException;

	// HTTP header
	class Header {

		/** 名称 */
		private String name;
		/** 值 */
		private String[] value;

		public Header(String name, String... value) {
			super();
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public String[] getValue() {
			return value;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}

			if (!Header.class.isAssignableFrom(obj.getClass())) {
				return false;
			}

			Header header = (Header) obj;

			return this.name.equals(header.name);
		}

		@Override
		public String toString() {
			return "Header [name=" + name + ", value=" + value + "]";
		}

	}

}
