package com.github.hualuomoli.framework.util;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.github.hualuomoli.tool.util.PropertyUtils;

/**
 * 项目配置
 */
public class ProjectConfig {

	private static boolean init = false;
	private static Properties prop = null;

	/**
	 * 初始化,需要初始化后才可使用
	 * @param resources 资源
	 */
	public synchronized static void init(String... resources) {
		if (init) {
			throw new RuntimeException("there is already init.");
		}

		init = true;
		prop = PropertyUtils.loadCover(resources);
	}

	/**
	 * 获取字符串
	 * @param key Key
	 * @return 值
	 */
	public static final String getString(String key) {
		if (prop == null) {
			throw new RuntimeException("please use PropertiesUtils.init(String...) to instance.");
		}

		return prop.getProperty(key);
	}

	/**
	 * 获取整数
	 * @param key Key
	 * @return 值
	 */
	public static final Integer getInteger(String key) {
		String value = getString(key);
		if (StringUtils.isBlank(value)) {
			return null;
		}
		return Integer.parseInt(value);
	}

}
