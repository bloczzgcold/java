package com.github.hualuomoli.gateway.client;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hualuomoli.gateway.client.entity.Page;
import com.github.hualuomoli.gateway.client.json.JSON;
import com.github.hualuomoli.gateway.client.lang.DealException;
import com.github.hualuomoli.gateway.client.lang.GatewayException;
import com.github.hualuomoli.tool.http.HttpClient;
import com.github.hualuomoli.tool.util.ObjectUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 网管客户端默认实现
 * @author lbq
 *
 */
public abstract class GatewayClientAdaptor implements GatewayClient {

	protected static final Logger logger = LoggerFactory.getLogger(GatewayClientAdaptor.class);

	/** 日期格式化 */
	protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S", Locale.CHINA);
	protected static final Charset CHARSET = Charset.forName("UTF-8"); // 默认编码集

	private JSON json;

	public GatewayClientAdaptor(JSON json) {
		super();
		this.json = json;
	}

	@Override
	public <T> T callObject(String method, String bizContent, Class<T> clazz) throws DealException, GatewayException {
		Validate.notBlank(method, "method is blank.");
		Validate.notNull(bizContent, "bizContent is nuil.");
		Validate.notNull(clazz, "clazz is null.");

		String result = this.call(method, bizContent);

		return json.parseObject(result, clazz);
	}

	@Override
	public <T> List<T> callArray(String method, String bizContent, Class<T> clazz) throws DealException, GatewayException {
		Validate.notBlank(method, "method is blank.");
		Validate.notNull(bizContent, "bizContent is nuil.");
		Validate.notNull(clazz, "clazz is null.");

		String result = this.call(method, bizContent);

		return json.parseArray(result, clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Page<T> callPage(String method, String bizContent, Class<T> clazz) throws DealException, GatewayException {
		Validate.notBlank(method, "method is blank.");
		Validate.notNull(bizContent, "bizContent is nuil.");
		Validate.notNull(clazz, "clazz is null.");

		String result = this.call(method, bizContent);

		Page<T> page = new Page<T>();
		return json.parseObject(result, page.getClass());
	}

	@Override
	public void call(String method, String bizContent, Callback callback) {
		Validate.notBlank(method, "method is blank.");
		Validate.notNull(bizContent, "bizContent is nuil.");
		Validate.notNull(callback, "callback is null.");

		try {
			String result = this.call(method, bizContent);
			this.onSuccess(callback, result);
		} catch (DealException e) {
			callback.onDealError(e.getSubCode(), e.getSubMessage(), e);
		} catch (GatewayException e) {
			callback.onFailt(e.getCode(), e.getMessage(), e);
		}

	}

	/**
	 * 调用成功 - 字符串
	 * @param callback 字符串回调函数
	 * @param result 数据
	 */
	protected <T> void onSuccess(CallbackString callback, String result) {
		callback.onSuccess(result);
	}

	/**
	 * 调用成功 - Object
	 * @param callback Object回调函数
	 * @param result 数据
	 */
	protected <T> void onSuccess(CallbackObject<T> callback, String result) {
		Class<T> clazz = this.getGenericClass(callback.getClass());
		callback.onSuccess(json.parseObject(result, clazz));
	}

	/**
	 * 调用成功 - 数组
	 * @param callback 数组回调函数
	 * @param result 数据
	 */
	protected <T> void onSuccess(CallbackArray<T> callback, String result) {
		Class<T> clazz = this.getGenericClass(callback.getClass());
		callback.onSuccess(json.parseArray(result, clazz));
	}

	/**
	 * 调用成功 - 分页
	 * @param callback 分页回调函数
	 * @param result 数据
	 */
	@SuppressWarnings("unchecked")
	protected <T> void onSuccess(CallbackPage<T> callback, String result) {
		Class<T> clazz = this.getGenericClass(callback.getClass());
		Page<T> page = new Page<T>();
		page = json.parseObject(result, page.getClass());
		page.setDataList(json.parseArray(json.toJSONString(page.getDataList()), clazz));
		callback.onSuccess(page.getPageNo(), page.getPageSize(), page.getCount(), page.getDataList());
	}

	/**
	 * 调用成功 - 字符串
	 * @param callback 字符串回调函数
	 * @param result 数据
	 */
	protected <T> void onSuccess(Callback callback, String result) {
		throw new RuntimeException();
	}

	/**
	 * 获取泛型类型
	 * @param clazz 类
	 * @return 泛型类型
	 */
	@SuppressWarnings("unchecked")
	private <T> Class<T> getGenericClass(Class<?> clazz) {
		ParameterizedType pt = null;
		try {
			pt = (ParameterizedType) clazz.getGenericInterfaces()[0];
		} catch (Exception e) {
			pt = (ParameterizedType) clazz.getGenericSuperclass();
		}
		return (Class<T>) pt.getActualTypeArguments()[0];
	}

	/**
	 * 获取时间戳 
	 * @return 时间戳 
	 */
	protected String getTimeStamp() {
		return sdf.format(new Date());
	}

	/**
	 * 获取请求参数
	 * @param obj 请求
	 * @return 参数
	 */
	protected List<HttpClient.Param> parseParam(Object obj) {
		Validate.notNull(obj, "obj is null.");

		List<HttpClient.Param> params = Lists.newArrayList();
		Class<?> clazz = obj.getClass();
		List<Field> fields = ObjectUtils.getFields(clazz);
		for (Field field : fields) {
			try {
				String name = field.getName();
				String methodNae = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
				Object value = clazz.getMethod(methodNae).invoke(obj);
				params.add(new HttpClient.Param(name, value));
			} catch (Exception e) {
				// 
			}
		}
		return params;
	}

	/**
	 * 实体类属性按照ASCII方式排序的签名原文
	 * @param object 实体类
	 * @param ignores 忽略的请求参数
	 * @return 按照ASCII方式拼接的签名源数据
	 */
	protected String getASCIISignOrigin(Object object, String... ignores) {
		Set<String> ignoreSets = Sets.newHashSet();
		for (String ignore : ignores) {
			ignoreSets.add(ignore);
		}
		return this.getASCIISignOrigin(object, ignoreSets);
	}

	/**
	 * 实体类属性按照ASCII方式排序的签名原文
	 * @param object 实体类
	 * @param ignores 忽略的请求参数
	 * @return 按照ASCII方式拼接的签名源数据
	 */
	protected String getASCIISignOrigin(Object object, Set<String> ignores) {
		Class<?> clazz = object.getClass();
		List<Field> fields = ObjectUtils.getFields(clazz);

		List<ASCII> asciiList = new ArrayList<ASCII>();

		for (Field field : fields) {
			String name = field.getName();
			int modify = field.getModifiers();
			if (Modifier.isPublic(modify) && Modifier.isStatic(modify) && Modifier.isFinal(modify)) {
				// public static fianl
				continue;
			}
			if (ignores.contains(name)) {
				// 需要忽略的参数
				continue;
			}
			try {
				String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
				String value = (String) clazz.getMethod(methodName).invoke(object);
				if (StringUtils.isBlank(value)) {
					// 为空的数据不参与签名 
					continue;
				}
				ASCII ascii = new ASCII();
				ascii.name = name;
				ascii.value = value;
				asciiList.add(ascii);
			} catch (Exception e) {
				if (logger.isDebugEnabled()) {
					logger.debug("", e);
				}
			}
		}

		// 没有参数
		if (asciiList.size() == 0) {
			return "";
		}

		// ASCII排序
		Collections.sort(asciiList, new Comparator<ASCII>() {

			@Override
			public int compare(ASCII o1, ASCII o2) {
				String name1 = o1.name;
				String name2 = o2.name;
				int len1 = name1.length();
				int len2 = name2.length();
				int len = len1 > len2 ? len2 : len1;
				for (int i = 0; i < len; i++) {
					int differ = name1.charAt(i) - name2.charAt(i);
					if (differ == 0) {
						continue;
					}
					return differ;
				}
				return len1 - len2;
			}
		});

		StringBuilder buffer = new StringBuilder();
		// 拼接参数
		for (int i = 0; i < asciiList.size(); i++) {
			ASCII ascii = asciiList.get(i);
			buffer.append("&").append(ascii.name).append("=").append(ascii.value);
		}

		String origin = buffer.toString().substring(1);
		if (logger.isDebugEnabled()) {
			logger.debug("sign origin={}", origin);
		}

		return origin;

	}

	// ASCII
	private class ASCII {
		private String name;
		private String value;
	}

}
