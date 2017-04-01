package com.github.hualuomoli.tool.http;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hualuomoli.tool.util.ReflectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

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

	public static enum Method {
		/** POST */
		POST,
		/** GET */
		GET;
	}

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

		List<HttpParam> paramList = Lists.newArrayList();
		StringBuilder buffer = new StringBuilder();
		for (Param param : params) {
			paramList.addAll(this.getHttpParams(param, charset));
		}

		if (paramList == null || paramList.size() == 0) {
			return StringUtils.EMPTY;
		}

		for (HttpParam httpParam : paramList) {
			logger.debug("[urlencoded] {}={}", httpParam.name, httpParam.value);
			buffer.append("&").append(httpParam.name).append("=").append(this.encoded(httpParam.value, charset));
		}

		return buffer.substring(1).toString();
	}

	class HttpParam {
		String name;
		String value;

		private HttpParam(String name, String value) {
			this.name = name;
			this.value = value;
		}
	}

	@SuppressWarnings("unchecked")
	private List<HttpParam> getHttpParams(Param param, String charset) {
		String name = param.getName();
		Object value = param.getValue();
		if (value == null) {
			return Lists.newArrayList();
		}
		Class<?> clazz = value.getClass();

		// 字符串
		if (String.class.isAssignableFrom(clazz)) {
			return Lists.newArrayList(new HttpParam(name, (String) value));
		}

		// 数值
		if (Integer.class.isAssignableFrom(clazz) || Long.class.isAssignableFrom(clazz)//
				|| Float.class.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz)) {
			return Lists.newArrayList(new HttpParam(name, String.valueOf(value)));
		}

		// 日期
		if (Date.class.isAssignableFrom(clazz)) {
			return Lists.newArrayList(new HttpParam(name, new SimpleDateFormat(datePattern).format((Date) value)));
		}

		// 枚举
		if (Enum.class.isAssignableFrom(clazz)) {
			return Lists.newArrayList(new HttpParam(name, ((Enum<?>) value).name()));
		}

		// map
		if (Map.class.isAssignableFrom(clazz)) {
			Map<String, Object> map = (Map<String, Object>) value;
			List<HttpParam> paramList = Lists.newArrayList();

			for (String key : map.keySet()) {
				paramList.addAll(this.getHttpParams(new Param(name + "[" + key + "]", map.get(key)), charset));
			}
			return paramList;
		}

		// list
		if (List.class.isAssignableFrom(clazz)) {
			List<?> list = (List<?>) value;
			List<HttpParam> paramList = Lists.newArrayList();

			for (int i = 0; i < list.size(); i++) {
				Object obj = list.get(i);
				paramList.addAll(this.getHttpParams(new Param(name + "[" + i + "]", obj), charset));
			}
			return paramList;
		}

		// 实体类
		Set<String> fieldNames = this.getClassFieldNames(clazz);
		List<HttpParam> paramList = Lists.newArrayList();

		for (String fieldName : fieldNames) {
			try {
				Object val = ReflectionUtils.getFieldValue(value, fieldName);
				paramList.addAll(this.getHttpParams(new Param(name + "[" + fieldName + "]", val), charset));
			} catch (Exception e) {
				logger.debug("", e);
			}
			// end for
		}

		return paramList;
	}

	/**
	 * 获取类型的属性名称
	 * @param clazz 类型
	 * @return 属性名称
	 */
	private Set<String> getClassFieldNames(Class<?> clazz) {
		Set<String> sets = Sets.newHashSet();

		if (clazz == null) {
			return sets;
		}

		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			int modifiers = field.getModifiers();
			if (Modifier.isFinal(modifiers)) {
				// 不可改变的属性
				continue;
			}
			sets.add(field.getName());
		}

		// add super class
		sets.addAll(this.getClassFieldNames(clazz.getSuperclass()));

		return sets;
	}

	/**
	 * 数据编码
	 * @param value 值
	 * @param charset 编码
	 * @return 编码后的值
	 */
	private String encoded(String value, String charset) {
		try {
			return URLEncoder.encode(value, charset);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
