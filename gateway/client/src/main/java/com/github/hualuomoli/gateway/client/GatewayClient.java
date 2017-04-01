package com.github.hualuomoli.gateway.client;

import java.util.List;

import com.github.hualuomoli.gateway.client.entity.Page;
import com.github.hualuomoli.gateway.client.lang.DealException;
import com.github.hualuomoli.gateway.client.lang.GatewayException;

/**
 * 网关
 * @author lbq
 *
 */
public interface GatewayClient {

	/**
	 * 调用
	 * @param method 请求的方法
	 * @param bizContent 请求的业务内容
	 * @return 返回的业务结果
	 * @throws DealException 业务处理错误
	 * @throws GatewayException 网关错误
	 */
	String call(String method, String bizContent) throws DealException, GatewayException;

	/**
	 * 调用
	 * @param method 请求的方法
	 * @param bizContent 请求的业务内容
	 * @return 返回的业务结果
	 * @throws DealException 业务处理错误
	 * @throws GatewayException 网关错误
	 */
	<T> T callObject(String method, String bizContent, Class<T> clazz) throws DealException, GatewayException;

	/**
	 * 调用
	 * @param method 请求的方法
	 * @param bizContent 请求的业务内容
	 * @return 返回的业务结果
	 * @throws DealException 业务处理错误
	 * @throws GatewayException 网关错误
	 */
	<T> List<T> callArray(String method, String bizContent, Class<T> clazz) throws DealException, GatewayException;

	/**
	 * 调用
	 * @param method 请求的方法
	 * @param bizContent 请求的业务内容
	 * @return 返回的业务结果
	 * @throws DealException 业务处理错误
	 * @throws GatewayException 网关错误
	 */
	<T> Page<T> callPage(String method, String bizContent, Class<T> clazz) throws DealException, GatewayException;

	/**
	 * 调用
	 * @param method 请求的方法
	 * @param bizContent 业务内容
	 * @param callback 回调
	 */
	void call(String method, String bizContent, Callback callback);

	// 回调
	interface Callback {

		/**
		 * 业务处理失败
		 * @param code 错误编码
		 * @param message 错误信息
		 * @param t 业务处理错误异常
		 */
		void onDealError(String code, String message, Throwable t);

		/**
		 * 网关错误
		 * @param code 错误编码
		 * @param message 错误信息
		 * @param t 网管处理异常
		 */
		void onFailt(String code, String message, Throwable t);

	}

	// String回调
	interface CallbackString extends Callback {

		/**
		 * 成功
		 * @param result 返回结果
		 */
		void onSuccess(String result);

	}

	// Object回调
	interface CallbackObject<T> extends Callback {

		/**
		 * 成功
		 * @param result 返回结果
		 */
		void onSuccess(T result);

	}

	// array回调
	interface CallbackArray<T> extends Callback {

		/**
		 * 成功
		 * @param results 返回结果集合
		 */
		void onSuccess(List<T> results);

	}

	// page回调
	interface CallbackPage<T> extends Callback {

		/**
		 * 成功
		 * @param pageNo 当前数据页码
		 * @param pageSize 每页数据数
		 * @param count 数据总数
		 * @param results 数据集合
		 */
		void onSuccess(Integer pageNo, Integer pageSize, Integer count, List<T> results);

	}

}
