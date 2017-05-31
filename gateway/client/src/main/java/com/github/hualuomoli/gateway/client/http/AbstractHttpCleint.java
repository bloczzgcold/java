package com.github.hualuomoli.gateway.client.http;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hualuomoli.gateway.client.util.Utils;
import com.github.hualuomoli.gateway.client.util.Utils.DateFormat;

/**
 * 抽象
 * @author hualuomoli
 *
 */
public abstract class AbstractHttpCleint implements HttpClient {

	private final Logger logger = LoggerFactory.getLogger(AbstractHttpCleint.class);

	private DateFormat dateFormat;

	public AbstractHttpCleint() {
		super();
		dateFormat = new DefaultDateFormat();
	}

	public AbstractHttpCleint(DateFormat dateFormat) {
		super();
		this.dateFormat = dateFormat;
	}

	@Override
	public String urlencoded(Charset charset, Object object) throws IOException {

		// 发送的内容
		String content = null;
		List<Utils.UrlencodedParam> paramList = Utils.getUrlencodedParams(object, dateFormat);

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
		List<Header> addRequestHeaders = new ArrayList<Header>();
		addRequestHeaders.add(new Header("Content-Type", "application/x-www-form-urlencoded"));

		// 执行
		return this.execute(content, charset, Method.POST, addRequestHeaders);
	}

	@Override
	public String urlencoded(Charset charset, Map<String, Object> paramMap) throws IOException {
		// 发送的内容
		String content = null;
		List<Utils.UrlencodedParam> paramList = Utils.getUrlencodedParams(paramMap, dateFormat);

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
		List<Header> addRequestHeaders = new ArrayList<Header>();
		addRequestHeaders.add(new Header("Content-Type", "application/x-www-form-urlencoded"));

		// 执行
		return this.execute(content, charset, Method.POST, addRequestHeaders);
	}

	@Override
	public String json(Charset charset, String content) throws IOException {

		logger.debug("[json] content={}", content);

		// headers(添加Content-Type)
		List<Header> addRequestHeaders = new ArrayList<Header>();
		addRequestHeaders.add(new Header("Content-Type", "application/json"));

		return this.execute(content, charset, Method.POST, addRequestHeaders);
	}

	/**
	 * 执行
	 * @param content 请求内容
	 * @param charset 编码集
	 * @param method 请求方法
	 * @param addRequestHeaders 增加的请求header信息
	 * @return 执行结果
	 * @throws IOException 处理异常
	 */
	protected abstract String execute(String content, Charset charset, Method method, List<Header> addRequestHeaders) throws IOException;

	// 默认日期转换
	private static class DefaultDateFormat implements Utils.DateFormat {

		private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		@Override
		public String format(Date date, Field field) {
			if (date == null) {
				return null;
			}
			return sdf.format(date);
		}
	};

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
