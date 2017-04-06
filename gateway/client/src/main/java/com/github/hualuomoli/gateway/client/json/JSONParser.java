package com.github.hualuomoli.gateway.client.json;

import java.util.List;

/**
 * JSON转换器
 * @author lbq
 *
 */
public interface JSONParser {

	/**
	 * 转换成字符串
	 * @param obj 实体类
	 * @return 字符串
	 */
	String toJsonString(Object obj);

	/**
	 * 转换成实体类
	 * @param text 字符串
	 * @param clazz 类型
	 * @return 实体类
	 */
	<T> T parseObject(String text, Class<T> clazz);

	/**
	 * 转换成数组
	 * @param text 字符串
	 * @param clazz 类型
	 * @return 数组
	 */
	<T> List<T> parseArray(String text, Class<T> clazz);

}
