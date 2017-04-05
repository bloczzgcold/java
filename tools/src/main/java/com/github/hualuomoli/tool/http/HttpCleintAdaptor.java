package com.github.hualuomoli.tool.http;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hualuomoli.tool.util.HttpUtils;
import com.google.common.collect.Lists;

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
	public String urlencoded(String url, Charset charset, List<Param> params) throws IOException {
		return this.urlencoded(url, charset, params, null, null);
	}

	@Override
	public String urlencoded(String url, Charset charset, List<Param> params, List<Header> requestHeaders, List<Header> responseHeaders) throws IOException {
		Validate.notBlank(url, "url is blank.");
		Validate.notNull(charset, "charset is null");

		// 发送的内容
		String content = this.getParams(params, charset.name());
		// headers(添加Content-Type)
		if (requestHeaders == null) {
			requestHeaders = Lists.newArrayList();
		}
		if (responseHeaders == null) {
			requestHeaders = Lists.newArrayList();
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
		Validate.notBlank(url, "url is blank.");
		Validate.notNull(charset, "charset is null");
		Validate.notNull(content, "content is null");

		logger.debug("[json] content={}", content);

		// headers(添加Content-Type)
		if (requestHeaders == null) {
			requestHeaders = Lists.newArrayList();
		}
		if (responseHeaders == null) {
			responseHeaders = Lists.newArrayList();
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

	/**
	 * 获取参数
	 * @param params 参数信息
	 * @param charset 编码集
	 * @return 参数
	 */
	private String getParams(List<Param> params, String charset) {
		if (params == null || params.size() == 0) {
			return StringUtils.EMPTY;
		}

		List<HttpUtils.Param> paramList = Lists.newArrayList();
		for (Param param : params) {
			List<HttpUtils.Param> ps = HttpUtils.getUrlencodedParams(param.getName(), param.getValue(), datePattern);
			paramList.addAll(ps);
		}

		if (paramList == null || paramList.size() == 0) {
			return StringUtils.EMPTY;
		}

		// 排序
		Collections.sort(paramList, new Comparator<HttpUtils.Param>() {

			@Override
			public int compare(com.github.hualuomoli.tool.util.HttpUtils.Param o1, com.github.hualuomoli.tool.util.HttpUtils.Param o2) {
				return com.github.hualuomoli.tool.util.StringUtils.compare(o1.name, o2.name);
			}
		});

		StringBuilder buffer = new StringBuilder();

		for (HttpUtils.Param param : paramList) {
			logger.debug("[urlencoded] {}={}", param.name, param.value);
			buffer.append("&").append(param.name).append("=").append(HttpUtils.encoded(param.value, charset));
		}

		return buffer.substring(1).toString();
	}

	// 调用方式
	public static enum Method {
		/** POST */
		POST,
		/** GET */
		GET;
	}

}
