package com.github.hualuomoli.gateway.client.http;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hualuomoli.gateway.client.util.Utils;

public abstract class HttpCleintAdaptor implements HttpClient {

	private final Logger logger = LoggerFactory.getLogger(HttpCleintAdaptor.class);
	private String datePattern;

	public HttpCleintAdaptor() {
		this("yyyy-MM-dd HH:mm:ss");
	}

	public HttpCleintAdaptor(String datePattern) {
		this.datePattern = datePattern;
	}

	@Override
	public String urlencoded(String url, Charset charset, Object object) throws IOException {
		return this.urlencoded(url, charset, object, null, null);
	}

	@Override
	public String urlencoded(String url, Charset charset, Map<String, Object> paramMap) throws IOException {
		return this.urlencoded(url, charset, paramMap, null, null);
	}

	@Override
	public String urlencoded(String url, Charset charset, Object object, List<Header> requestHeaders, List<Header> responseHeaders) throws IOException {

		// 发送的内容
		String content = null;
		List<Utils.UrlencodedParam> paramList = Utils.getUrlencodedParams(object, datePattern);

		if (paramList == null || paramList.size() == 0) {
			content = "";
		} else {
			// 排序
			Utils.sort(paramList);

			StringBuilder buffer = new StringBuilder();

			for (Utils.UrlencodedParam param : paramList) {
				logger.debug("[urlencoded] {}={}", param.name, param.value);
				buffer.append("&").append(param.name).append("=").append(this.encoded(param.value, charset.name()));
			}

			content = buffer.substring(1).toString();

		}

		// headers(添加Content-Type)
		if (requestHeaders == null) {
			requestHeaders = new ArrayList<Header>();
		}
		if (responseHeaders == null) {
			requestHeaders = new ArrayList<Header>();
		}
		requestHeaders.add(new Header("Content-Type", "application/x-www-form-urlencoded"));

		// 执行
		return this.execute(url, content, charset, Method.POST, requestHeaders, responseHeaders);
	}

	@Override
	public String urlencoded(String url, Charset charset, Map<String, Object> paramMap, List<Header> requestHeaders, List<Header> responseHeaders) throws IOException {
		// 发送的内容
		String content = null;
		List<Utils.UrlencodedParam> paramList = Utils.getUrlencodedParams(paramMap, datePattern);

		if (paramList == null || paramList.size() == 0) {
			content = "";
		} else {
			// 排序
			Utils.sort(paramList);

			StringBuilder buffer = new StringBuilder();

			for (Utils.UrlencodedParam param : paramList) {
				logger.debug("[urlencoded] {}={}", param.name, param.value);
				buffer.append("&").append(param.name).append("=").append(this.encoded(param.value, charset.name()));
			}

			content = buffer.substring(1).toString();

		}

		// headers(添加Content-Type)
		if (requestHeaders == null) {
			requestHeaders = new ArrayList<Header>();
		}
		if (responseHeaders == null) {
			requestHeaders = new ArrayList<Header>();
		}
		requestHeaders.add(new Header("Content-Type", "application/x-www-form-urlencoded"));

		// 执行
		return this.execute(url, content, charset, Method.POST, requestHeaders, responseHeaders);
	}

	@Override
	public String json(String url, Charset charset, String content) throws IOException {
		return this.json(url, charset, content, null, null);
	}

	@Override
	public String json(String url, Charset charset, String content, List<Header> requestHeaders, List<Header> responseHeaders) throws IOException {

		logger.debug("[json] content={}", content);

		// headers(添加Content-Type)
		if (requestHeaders == null) {
			requestHeaders = new ArrayList<Header>();
		}
		if (responseHeaders == null) {
			responseHeaders = new ArrayList<Header>();
		}
		requestHeaders.add(new Header("Content-Type", "application/json"));

		return this.execute(url, content, charset, Method.POST, requestHeaders, responseHeaders);
	}

	/**
	 * 执行
	 * @param urlStr 请求URL
	 * @param content 请求内容
	 * @param charset 编码集
	 * @param method 请求方法
	 * @param requestHeaders 请求header信息
	 * @param responseHeaders 响应的header信息,将响应的header信息放到这个集合中.如果集合为空不放置
	 * @return 执行结果
	 * @throws IOException 处理异常
	 */
	protected abstract String execute(String urlStr, String content, Charset charset, Method method, List<Header> requestHeaders, List<Header> responseHeaders) throws IOException;

	// 调用方式
	public static enum Method {
		/** POST */
		POST,
		/** GET */
		GET;
	}

	/**
	 * 对值进行编码
	 * @param value 值
	 * @param charset 编码类型
	 * @return 编码后的值
	 */
	public String encoded(String value, String charset) {
		if (value == null) {
			return null;
		}

		try {
			return URLEncoder.encode(value, charset);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
