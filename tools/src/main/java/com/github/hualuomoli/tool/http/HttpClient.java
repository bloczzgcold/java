package com.github.hualuomoli.tool.http;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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
	 * @param params 请求的参数
	 * @return 执行结果
	 * @throws IOException 执行错误
	 */
	String urlencoded(String url, Charset charset, List<Param> params) throws IOException;

	/**
	 * 执行http请求(urlencoded)
	 * @param url 请求的url
	 * @param charset 数据编码集
	 * @param params 请求的参数
	 * @param requestHeaders 请求的header信息
	 * @param responseHeaders 响应的header西悉尼
	 * @return 执行结果
	 * @throws IOException 执行错误
	 */
	String urlencoded(String url, Charset charset, List<Param> params, List<Header> requestHeaders, List<Header> responseHeaders) throws IOException;

	/**
	 * 执行http请求(urlencoded)
	 * @param url 请求的url
	 * @param charset 数据编码集
	 * @param content 请求的内容
	 * @return 执行结果
	 * @throws IOException 执行错误
	 */
	String json(String url, Charset charset, String content) throws IOException;

	/**
	 * 执行http请求(urlencoded)
	 * @param url 请求的url
	 * @param charset 数据编码集
	 * @param content 请求的内容
	 * @param requestHeaders 请求的header信息
	 * @param responseHeaders 响应的header西悉尼
	 * @return 执行结果
	 * @throws IOException 执行错误
	 */
	String json(String url, Charset charset, String content, List<Header> requestHeaders, List<Header> responseHeaders) throws IOException;

	// HTTP header
	class Header {

		/** 名称 */
		private String name;
		/** 值 */
		private String value;

		public Header(String name, String value) {
			super();
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
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

			return StringUtils.equals(this.name, header.name);
		}

		@Override
		public String toString() {
			return "Header [name=" + name + ", value=" + value + "]";
		}

	}

	// HTTP param
	class Param {

		/** 名称 */
		private String name;
		/** 值 */
		private Object value;

		public Param(String name, Object value) {
			super();
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public Object getValue() {
			return value;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}

			if (!Param.class.isAssignableFrom(obj.getClass())) {
				return false;
			}

			Param param = (Param) obj;

			return StringUtils.equals(this.name, param.name);
		}

		@Override
		public String toString() {
			return "Param [name=" + name + ", value=" + value + "]";
		}
	}

}
