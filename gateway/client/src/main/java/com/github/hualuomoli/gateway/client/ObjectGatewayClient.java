package com.github.hualuomoli.gateway.client;

import java.util.List;

import com.github.hualuomoli.gateway.client.GatewayClient.Callback;
import com.github.hualuomoli.gateway.client.entity.Page;
import com.github.hualuomoli.gateway.client.lang.DealException;
import com.github.hualuomoli.gateway.client.lang.GatewayException;

/**
 * 网关
 * @author lbq
 *
 */
public interface ObjectGatewayClient {

	/**
	 * 调用
	 * @param object 请求参数
	 * @param parser 参数解析器
	 * @return 调用结果
	 * @throws DealException 业务处理错误
	 * @throws GatewayException 网管错误
	 */
	String call(Object object) throws DealException, GatewayException;

	/**
	 * 调用
	 * @param object 请求参数
	 * @param parser 参数解析器
	 * @param clazz 返回的数据类型
	 * @return 调用结果
	 * @throws DealException 业务处理错误
	 * @throws GatewayException 网管错误
	 */
	<T> T callObject(Object object, Class<T> clazz) throws DealException, GatewayException;

	/**
	 * 调用
	 * @param object 请求参数
	 * @param parser 参数解析器
	 * @param clazz 返回的数据类型
	 * @return 调用结果
	 * @throws DealException 业务处理错误
	 * @throws GatewayException 网管错误
	 */
	<T> List<T> callArray(Object object, Class<T> clazz) throws DealException, GatewayException;

	/**
	 * 调用
	 * @param object 请求参数
	 * @param parser 参数解析器
	 * @param clazz 返回的数据类型
	 * @return 调用结果
	 * @throws DealException 业务处理错误
	 * @throws GatewayException 网管错误
	 */
	<T> Page<T> callPage(Object object, Class<T> clazz) throws DealException, GatewayException;

	/**
	 * 调用
	 * @param object 请求参数
	 * @param parser 参数解析器
	 * @param callback 也处理回调
	 */
	void call(Object object, Callback callback);

	// 转换器
	interface ObejctParser {

		/**
		 * 获取请求方法
		 * @param object obj
		 * @return 请求方法
		 */
		String getMethod(Object object);

		/**
		 * 获取请求内容
		 * @param object obj
		 * @return 请求内容
		 */
		String getBizContent(Object object);

	}

}
