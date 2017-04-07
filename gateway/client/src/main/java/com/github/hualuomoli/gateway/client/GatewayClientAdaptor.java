package com.github.hualuomoli.gateway.client;

import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
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
public abstract class GatewayClientAdaptor implements GatewayClient, ObjectGatewayClient {

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");

	private JSONParser jSONParser;
	private ObjectGatewayClient.ObejctParser obejctParser;

	public GatewayClientAdaptor(JSONParser jSONParser, ObjectGatewayClient.ObejctParser obejctParser) {
		super();
		this.jSONParser = jSONParser;
		this.obejctParser = obejctParser;
	}

	@Override
	public <T> T callObject(String method, String bizContent, Class<T> clazz) throws DealException, GatewayException {
		Validate.notBlank(method, "method is blank.");
		Validate.notNull(bizContent, "bizContent is nuil.");
		Validate.notNull(clazz, "clazz is null.");

		String result = this.call(method, bizContent);

		return jSONParser.parseObject(result, clazz);
	}

	@Override
	public <T> List<T> callArray(String method, String bizContent, Class<T> clazz) throws DealException, GatewayException {
		Validate.notBlank(method, "method is blank.");
		Validate.notNull(bizContent, "bizContent is nuil.");
		Validate.notNull(clazz, "clazz is null.");

		String result = this.call(method, bizContent);

		return jSONParser.parseArray(result, clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Page<T> callPage(String method, String bizContent, Class<T> clazz) throws DealException, GatewayException {
		Validate.notBlank(method, "method is blank.");
		Validate.notNull(bizContent, "bizContent is nuil.");
		Validate.notNull(clazz, "clazz is null.");

		String result = this.call(method, bizContent);

		Page<T> page = new Page<T>();
		return jSONParser.parseObject(result, page.getClass());
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

		return this.call(obejctParser.getMethod(object), obejctParser.getBizContent(object));
	}

	@Override
	public <T> T callObject(Object object, Class<T> clazz) throws DealException, GatewayException {
		Validate.notNull(object, "object is blank.");
		Validate.notNull(clazz, "clazz is nuil.");

		return this.callObject(obejctParser.getMethod(object), obejctParser.getBizContent(object), clazz);
	}

	@Override
	public <T> List<T> callArray(Object object, Class<T> clazz) throws DealException, GatewayException {
		Validate.notNull(object, "object is blank.");
		Validate.notNull(clazz, "clazz is nuil.");

		return this.callArray(obejctParser.getMethod(object), obejctParser.getBizContent(object), clazz);
	}

	@Override
	public <T> Page<T> callPage(Object object, Class<T> clazz) throws DealException, GatewayException {
		Validate.notNull(object, "object is blank.");
		Validate.notNull(clazz, "clazz is nuil.");

		return this.callPage(obejctParser.getMethod(object), obejctParser.getBizContent(object), clazz);
	}

	@Override
	public void call(Object object, Callback callback) {
		Validate.notNull(object, "object is blank.");
		Validate.notNull(callback, "callback is nuil.");

		this.call(obejctParser.getMethod(object), obejctParser.getBizContent(object), callback);
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
		callback.onSuccess(jSONParser.parseObject(result, clazz));
	}

	/**
	 * 调用成功 - 数组
	 * @param callback 数组回调函数
	 * @param result 数据
	 */
	protected <T> void onSuccess(CallbackArray<T> callback, String result) {
		Class<T> clazz = this.getGenericClass(callback.getClass());
		callback.onSuccess(jSONParser.parseArray(result, clazz));
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
		page = jSONParser.parseObject(result, page.getClass());
		page.setDataList(jSONParser.parseArray(jSONParser.toJsonString(page.getDataList()), clazz));
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
		return simpleDateFormat.format(new Date());
	}

}
