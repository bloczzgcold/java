package com.github.hualuomoli.gateway.client;

import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.github.hualuomoli.gateway.client.entity.Page;
import com.github.hualuomoli.gateway.client.json.JSONParser;
import com.github.hualuomoli.gateway.client.lang.DealException;
import com.github.hualuomoli.gateway.client.lang.GatewayException;
import com.github.hualuomoli.gateway.client.util.Validate;

/**
 * 网管客户端默认实现
 * @author lbq
 *
 */
public abstract class AbstractGatewayClient implements GatewayClient {

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");

	private JSONParser jsonParser;
	private GatewayClient.ObjectParser objectParser;

	public AbstractGatewayClient(JSONParser jsonParser, GatewayClient.ObjectParser objectParser) {
		super();
		this.jsonParser = jsonParser;
		this.objectParser = objectParser;
		Validate.notNull(jsonParser, "jsonParser is nuil.");
		Validate.notNull(objectParser, "objectParser is nuil.");
	}

	@Override
	public <T> T callObject(String method, String bizContent, Class<T> clazz) throws DealException, GatewayException {
		Validate.notBlank(method, "method is blank.");
		Validate.notNull(bizContent, "bizContent is nuil.");
		Validate.notNull(clazz, "clazz is null.");

		String result = this.call(method, bizContent);

		return jsonParser.parseObject(result, clazz);
	}

	@Override
	public <T> List<T> callArray(String method, String bizContent, Class<T> clazz) throws DealException, GatewayException {
		Validate.notBlank(method, "method is blank.");
		Validate.notNull(bizContent, "bizContent is nuil.");
		Validate.notNull(clazz, "clazz is null.");

		String result = this.call(method, bizContent);

		return jsonParser.parseArray(result, clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Page<T> callPage(String method, String bizContent, Class<T> clazz) throws DealException, GatewayException {
		Validate.notBlank(method, "method is blank.");
		Validate.notNull(bizContent, "bizContent is nuil.");
		Validate.notNull(clazz, "clazz is null.");

		String result = this.call(method, bizContent);

		Page<T> page = new Page<T>();
		return jsonParser.parseObject(result, page.getClass());
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

	@Override
	public String call(Object object) throws DealException, GatewayException {
		Validate.notNull(object, "object is blank.");

		return this.call(this.getRequestMethod(object), this.getRequestContent(object));
	}

	@Override
	public <T> T callObject(Object object, Class<T> clazz) throws DealException, GatewayException {
		Validate.notNull(object, "object is blank.");
		Validate.notNull(clazz, "clazz is nuil.");

		return this.callObject(this.getRequestMethod(object), this.getRequestContent(object), clazz);
	}

	@Override
	public <T> List<T> callArray(Object object, Class<T> clazz) throws DealException, GatewayException {
		Validate.notNull(object, "object is blank.");
		Validate.notNull(clazz, "clazz is nuil.");

		return this.callArray(this.getRequestMethod(object), this.getRequestContent(object), clazz);
	}

	@Override
	public <T> Page<T> callPage(Object object, Class<T> clazz) throws DealException, GatewayException {
		Validate.notNull(object, "object is blank.");
		Validate.notNull(clazz, "clazz is nuil.");

		return this.callPage(this.getRequestMethod(object), this.getRequestContent(object), clazz);
	}

	@Override
	public void call(Object object, Callback callback) {
		Validate.notNull(object, "object is blank.");
		Validate.notNull(callback, "callback is nuil.");

		this.call(this.getRequestMethod(object), this.getRequestContent(object), callback);
	}

	/**
	 * 获取请求方法
	 * @param object 请求内容
	 * @return 请求方法名
	 */
	private String getRequestMethod(Object object) {
		if (object == null) {
			throw new IllegalArgumentException("object must not be null or use call(String, String).");
		}
		// Collection
		if (Collection.class.isAssignableFrom(object.getClass())) {
			Collection<?> collection = (List<?>) object;
			if (collection.size() == 0) {
				throw new IllegalArgumentException("collection must not be empty or use call(String, String)");
			}

			Object obj = null;
			for (Object c : collection) {
				obj = c;
				break;
			}

			return objectParser.getMethod(obj);
		}

		// object
		return objectParser.getMethod(object);
	}

	/**
	 * 获取请求内容
	 * @param object 请求内容
	 * @return 请求内容JSON
	 */
	private String getRequestContent(Object object) {
		if (object == null) {
			throw new IllegalArgumentException("object must not be null or use call(String, String).");
		}

		// object
		return objectParser.getBizContent(object);
	}

	/**
	 * 调用成功 - 字符串
	 * @param callback 字符串回调函数
	 * @param result 数据
	 */
	@SuppressWarnings("unchecked")
	protected <T> void onSuccess(Callback callback, String result) {
		Class<?> clazz = callback.getClass();
		if (CallbackString.class.isAssignableFrom(clazz)) {
			this.onStringSuccess((CallbackString) callback, result);
		} else if (CallbackObject.class.isAssignableFrom(clazz)) {
			this.onObjectSuccess((CallbackObject<T>) callback, result);
		} else if (CallbackArray.class.isAssignableFrom(clazz)) {
			this.onArraySuccess((CallbackArray<T>) callback, result);
		} else if (CallbackPage.class.isAssignableFrom(clazz)) {
			this.onPageSuccess((CallbackPage<T>) callback, result);
		} else {
			throw new RuntimeException();
		}
	}

	/**
	 * 调用成功 - 字符串
	 * @param callback 字符串回调函数
	 * @param result 数据
	 */
	protected <T> void onStringSuccess(CallbackString callback, String result) {
		callback.onSuccess(result);
	}

	/**
	 * 调用成功 - Object
	 * @param callback Object回调函数
	 * @param result 数据
	 */
	protected <T> void onObjectSuccess(CallbackObject<T> callback, String result) {
		Class<T> clazz = this.getGenericClass(callback.getClass());
		callback.onSuccess(jsonParser.parseObject(result, clazz));
	}

	/**
	 * 调用成功 - 数组
	 * @param callback 数组回调函数
	 * @param result 数据
	 */
	protected <T> void onArraySuccess(CallbackArray<T> callback, String result) {
		Class<T> clazz = this.getGenericClass(callback.getClass());
		callback.onSuccess(jsonParser.parseArray(result, clazz));
	}

	/**
	 * 调用成功 - 分页
	 * @param callback 分页回调函数
	 * @param result 数据
	 */
	@SuppressWarnings("unchecked")
	protected <T> void onPageSuccess(CallbackPage<T> callback, String result) {
		Class<T> clazz = this.getGenericClass(callback.getClass());
		Page<T> page = new Page<T>();
		page = jsonParser.parseObject(result, page.getClass());
		page.setDataList(jsonParser.parseArray(jsonParser.toJsonString(page.getDataList()), clazz));
		callback.onSuccess(page.getPageNo(), page.getPageSize(), page.getCount(), page.getDataList());
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
		return simpleDateFormat.format(new Date());
	}

}
