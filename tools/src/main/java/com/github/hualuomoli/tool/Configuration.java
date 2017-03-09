package com.github.hualuomoli.tool;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.Validate;

import com.github.hualuomoli.tool.PropertiesLoader;
import com.google.common.collect.Maps;

/**
 * 读取配置信息
 * @author lbq
 *
 */
public class Configuration {

	private static final Map<String, String> map = Maps.newHashMap();

	/**
	 * 初始化资源
	 * @param propFileNames 资源名称
	 */
	public static void init(String... propFileNames) {
		map.clear();
		map.putAll(loadResources(propFileNames));
	}

	/**
	 * 添加加载的资源
	 * @param propFileNames 资源名称
	 */
	public static void addResource(String... propFileNames) {
		map.putAll(loadResources(propFileNames));
	}

	/**
	 * 获取值
	 * @param key 键
	 * @return 值
	 */
	public static String get(String key) {
		return map.get(key);
	}

	/**
	 * 加载资源
	 * @param propFileNames 资源名称
	 * @return 资源信息
	 */
	private static Map<String, String> loadResources(String... propFileNames) {

		Validate.notEmpty(propFileNames, "propFileNames is empty.");

		Properties prop = new Properties();

		for (String propFileName : propFileNames) {
			PropertiesLoader.loadProperties(propFileName, prop);
		}

		return PropertiesLoader.parse(prop);
	}

}
