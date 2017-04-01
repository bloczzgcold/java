package com.github.hualuomoli.gateway.client.json;

import java.util.List;

/**
 * JSON转换器
 * @author lbq
 *
 */
public interface JSON {

	/**
	 * 转换成字符串
	 * @param object 需要转换的数据
	 * @return 转换后的字符串
	 */
	String toJSONString(Object object);

	/**
	 * 字符串转换成数据
	 * @param text 字符串
	 * @param clazz 数据的类型
	 * @return 数据
	 */
	<T> T parseObject(String text, Class<T> clazz);

	/**
	 * 字符串转换成数据集合
	 * @param text 字符串
	 * @param clazz 数据的类型
	 * @return 数据集合
	 */
	<T> List<T> parseArray(String text, Class<T> clazz);

//	/**
//	 * 字符串转换成JSON
//	 * @param text 字符串
//	 * @return 数据
//	 */
//	JSONObject parse(String text);

//	interface JSONObject {
//		/**
//		 * 获取String数据
//		 * @param key 键
//		 * @return 值
//		 */
//		String getString(String key);
//
//		/**
//		 * 获取数据
//		 * @param key 键
//		 * @return 值
//		 */
//		Object get(String key);
//
//	}

}
